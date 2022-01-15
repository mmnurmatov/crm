package uz.isd.javagroup.grandcrm.controller.modules;

import com.google.zxing.WriterException;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.layout.font.FontProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseProduct;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseProductItem;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.modules.*;
import uz.isd.javagroup.grandcrm.utility.QRCodeGenerator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("module/warehouse-expense-item")
public class WarehouseExpenseItemController extends BaseController {

    @Autowired
    WarehouseProductItemService warehouseProductItemService;
    @Autowired
    WarehouseProductService warehouseProductService;

    @Autowired
    ItemStatusService itemStatusService;

    @Autowired
    ServletContext servletContext;

    private final TemplateEngine templateEngine;

    public WarehouseExpenseItemController(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @RequestMapping(path = "/{warehouseProductId}", method = RequestMethod.GET)
    public ModelAndView index(@PathVariable(name = "warehouseProductId") Optional<Long> warehouseProductId,
                              @ModelAttribute("status") String typesData,
                              @ModelAttribute("message") String messageData, ModelAndView modelAndView) throws RecordNotFoundException {
        if (!warehouseProductId.isPresent()) {
            modelAndView.addObject("status", false);
            modelAndView.addObject("message", "Invalid warehouseProductId...");
        }
        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }

        List<WarehouseProductItem> warehouseProductItems = warehouseProductItemService.findAllByWarehouseProductId(warehouseProductId.get());
        WarehouseProduct warehouseProduct = warehouseProductService.getWarehouseProductById(warehouseProductId.get());

        WarehouseProduct.Action action = warehouseProduct.getAction();

        modelAndView.addObject("warehouseProduct", warehouseProduct);
        modelAndView.addObject("warehouseProductItems", warehouseProductItems);
        modelAndView.addObject("warehouseProductAction", action.name());
        modelAndView.setViewName("/pages/modules/warehouse-expense-items/index");
        return modelAndView;
    }

    // report for expense section 1 for view

    @RequestMapping(path = "/pdf/{warehouseProductId}", method = RequestMethod.GET)
    public ModelAndView createPDF(@PathVariable(name = "warehouseProductId") Optional<Long> warehouseProductId,
                                  ModelAndView modelAndView) throws RecordNotFoundException, IOException, WriterException {

        List<WarehouseProductItem> warehouseProductItems = warehouseProductItemService.findAllByWarehouseProductId(warehouseProductId.get());
        WarehouseProduct warehouseProduct = warehouseProductService.getWarehouseProductById(warehouseProductId.get());

        double summa = 0.0;

        for (WarehouseProductItem item:warehouseProductItems){

            summa += item.getCount().doubleValue() * item.getIncomePrice().doubleValue();

        }

        QRCodeGenerator.generateQRCodeImage("http://isd-crm.uz:8080/check/" + warehouseProduct.getRegNumber(), 110, 110, "./src/main/resources/static/photo/QRCode.png");

        modelAndView.addObject("summa", summa);
        modelAndView.addObject("warehouseProduct", warehouseProduct);
        modelAndView.addObject("warehouseProductItems", warehouseProductItems);

        modelAndView.setViewName("/pages/reports/expense/page-invoice-expense");
        return modelAndView;
    }


    // report for expense section 2 for download
    @RequestMapping(path = "/warehouse-pdf/{warehouseProductId}", method = RequestMethod.GET)
    public ResponseEntity<?> getPDF(@PathVariable(name = "warehouseProductId") Optional<Long> warehouseProductId,
                                    HttpServletRequest request,
                                    HttpServletResponse response
    ) throws IOException, RecordNotFoundException, WriterException {

        /* Do Business Logic*/
        List<WarehouseProductItem> warehouseProductItems = warehouseProductItemService.findAllByWarehouseProductId(warehouseProductId.get());
        WarehouseProduct warehouseProduct = warehouseProductService.getWarehouseProductById(warehouseProductId.get());

        double summa = 0.0;

        for (WarehouseProductItem item:warehouseProductItems){

            summa += item.getCount().doubleValue() * item.getIncomePrice().doubleValue();

        }

        WebContext context = new WebContext(request, response, servletContext);
        context.setVariable("summa", summa);
        context.setVariable("warehouseProduct", warehouseProduct);
        context.setVariable("warehouseProductItems", warehouseProductItems);
        String orderHtml = templateEngine.process("/pages/reports/expense/warehouse-expense-product", context);

        /* Setup Source and target I/O streams */

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:8080");

        // pdf conversion
        FontProvider dfp = new DefaultFontProvider(true, true, false);

        /* Temporary fix for display issue with blocks (in current html2pdf version (2.0.1) */
        converterProperties.setFontProvider(dfp);

        /* Call convert method */
        HtmlConverter.convertToPdf(orderHtml, target, converterProperties);

        /* extract output as bytes */
        byte[] bytes = target.toByteArray();

        /* Send the response as downloadable PDF */

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=expense.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);

    }

    @RequestMapping(path = "/invoice/{warehouseProductId}", method = RequestMethod.GET)
//    @PreAuthorize("hasAuthority('WarehouseProductWarehouseProductRecieveItemRead')")
    public ModelAndView itemsForInvoice(@PathVariable(name = "warehouseProductId") Optional<Long> warehouseProductId,
                              @ModelAttribute("status") String typesData,
                              @ModelAttribute("message") String messageData, ModelAndView modelAndView) throws RecordNotFoundException {
        if (!warehouseProductId.isPresent()) {
            modelAndView.addObject("status", false);
            modelAndView.addObject("message", "Invalid warehouseProductId...");
        }
        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }

        List<WarehouseProductItem> warehouseProductItems = warehouseProductItemService.findAllByWarehouseProductId(warehouseProductId.get());
        WarehouseProduct warehouseProduct = warehouseProductService.getWarehouseProductById(warehouseProductId.get());

        WarehouseProduct.Action action = warehouseProduct.getAction();

        modelAndView.addObject("warehouseProduct", warehouseProduct);
        modelAndView.addObject("warehouseProductItems", warehouseProductItems);
        modelAndView.addObject("warehouseProductAction", action.name());
        modelAndView.setViewName("/pages/modules/warehouse-expense-items-invoice/index");
        return modelAndView;
    }

}

package uz.isd.javagroup.grandcrm.controller.modules;

import com.google.zxing.WriterException;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.layout.font.FontProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.directories.Product;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseProduct;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseProductItem;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.ProductService;
import uz.isd.javagroup.grandcrm.services.modules.ItemStatusService;
import uz.isd.javagroup.grandcrm.services.modules.WarehouseProductItemService;
import uz.isd.javagroup.grandcrm.services.modules.WarehouseProductService;
import uz.isd.javagroup.grandcrm.utility.QRCodeGenerator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("module/warehouse-product-item")
public class WarehouseProductItemController extends BaseController {

    @Autowired
    WarehouseProductItemService warehouseProductItemService;
    @Autowired
    WarehouseProductService warehouseProductService;
    @Autowired
    ProductService productService;
    @Autowired
    ItemStatusService itemStatusService;

    @Autowired
    ServletContext servletContext;

    private final TemplateEngine templateEngine;

    @Autowired
    ResourceLoader resourceLoader;

    public WarehouseProductItemController(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @RequestMapping(path = "/{warehouseProductId}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('WarehouseProductItemRead')")
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
        List<WarehouseProduct> warehouseProducts = warehouseProductService.findAll();
        WarehouseProduct warehouseProduct = warehouseProductService.getWarehouseProductById(warehouseProductId.get());
        List<Product> products = productService.findAll();

        WarehouseProduct.Action action = warehouseProduct.getAction();

        modelAndView.addObject("warehouseProductItems", warehouseProductItems);
        modelAndView.addObject("warehouseProducts", warehouseProducts);
        modelAndView.addObject("products", products);
        modelAndView.addObject("warehouseProductAction", action.name());
        modelAndView.addObject("warehouseProduct", warehouseProduct);
        modelAndView.setViewName("/pages/modules/warehouse-product-items/index");
        return modelAndView;
    }

    // report for prixod section 1 for view

    @RequestMapping(path = "/pdf/{warehouseProductId}", method = RequestMethod.GET)
    public ModelAndView createPDF(@PathVariable(name = "warehouseProductId") Optional<Long> warehouseProductId,
                                  ModelAndView modelAndView) throws RecordNotFoundException, IOException, WriterException {

        List<WarehouseProductItem> warehouseProductItems = warehouseProductItemService.findAllByWarehouseProductId(warehouseProductId.get());
        WarehouseProduct warehouseProduct = warehouseProductService.getWarehouseProductById(warehouseProductId.get());

        double summa = 0.0;

        for (WarehouseProductItem item:warehouseProductItems){

            summa += item.getCount().doubleValue() * item.getIncomePrice().doubleValue();

        }

//        File pathFile = new File("./src/main/resources/static/photo/" + File.separator + "QRCode.png");
//        if (pathFile.exists()) {
//            pathFile.delete();
//        }

        QRCodeGenerator.generateQRCodeImage("http://isd-crm.uz:8080/check/" + warehouseProduct.getRegNumber(), 110, 110, "./src/main/resources/static/photo/QRCode.png");

        modelAndView.addObject("summa", summa);
        modelAndView.addObject("warehouseProduct", warehouseProduct);
        modelAndView.addObject("warehouseProductItems", warehouseProductItems);

        modelAndView.setViewName("/pages/reports/prixod/page-invoice-prixod");
        return modelAndView;
    }


    // report for prixod section 2 for download
    @RequestMapping(path = "/warehouse-pdf/{warehouseProductId}", method = RequestMethod.GET)
    public ResponseEntity<?> getPDF(@PathVariable(name = "warehouseProductId") Optional<Long> warehouseProductId,
                                    HttpServletRequest request,
                                    HttpServletResponse response
    ) throws IOException, RecordNotFoundException {

        /* Do Business Logic*/
        List<WarehouseProductItem> warehouseProductItems = warehouseProductItemService.findAllByWarehouseProductId(warehouseProductId.get());
        WarehouseProduct warehouseProduct = warehouseProductService.getWarehouseProductById(warehouseProductId.get());

        double summa = 0.0;

        for (WarehouseProductItem item:warehouseProductItems){

            summa += item.getCount().doubleValue() * item.getIncomePrice().doubleValue();

        }

        /* Create HTML using Thymeleaf template Engine */

        WebContext context = new WebContext(request, response, servletContext);
        context.setVariable("summa", summa);
        context.setVariable("warehouseProduct", warehouseProduct);
        context.setVariable("warehouseProductItems", warehouseProductItems);
        String orderHtml = templateEngine.process("/pages/reports/prixod/warehouse-income-product", context);

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
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=prixod.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);

    }


    @RequestMapping(path = "/transferWaiting/{warehouseProductId}", method = RequestMethod.GET)
    public ModelAndView transferWaiting(@PathVariable(name = "warehouseProductId") Optional<Long> warehouseProductId,
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

        modelAndView.addObject("warehouseProductItems", warehouseProductItems);
        modelAndView.addObject("warehouseProduct", warehouseProduct);
        modelAndView.setViewName("/pages/modules/transfers/data");
        return modelAndView;
    }


    @RequestMapping(path = "/transferWaitingForEdit/{warehouseProductId}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('WarehouseProductItemRead')")
    public ModelAndView transferWaitingForEdit(
            @PathVariable(name = "warehouseProductId") Optional<Long> warehouseProductId,
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
        if (!warehouseProductId.isPresent()) throw new RecordNotFoundException("warehouseProductId not found");

        List<Object[]> warehouseProductItems = warehouseProductItemService.findAllByWarehouseProductIdWithRemaining(warehouseProductId.get());
        WarehouseProduct warehouseProduct = warehouseProductService.getWarehouseProductById(warehouseProductId.get());

        modelAndView.addObject("warehouseProductItems", warehouseProductItems);
        modelAndView.addObject("warehouseProduct", warehouseProduct);

        modelAndView.setViewName("/pages/modules/transfers/edit");
        return modelAndView;
    }

    @Transactional
    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('WarehouseProductItemCreate')")
    public RedirectView createWarehouseProductItem(@RequestParam(name = "warehouseProductCreateId") Long warehouseProductId,
                                                   @RequestParam(name = "productCreateId") Long productId,
                                                   @RequestParam(name = "storedCount") BigDecimal storedCount,
                                                   @RequestParam(name = "incomePrice") BigDecimal incomePrice, RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        WarehouseProductItem warehouseProductItem = new WarehouseProductItem();
        WarehouseProduct warehouseProduct = null;
        if (warehouseProductId != 0) {
            warehouseProduct = warehouseProductService.getWarehouseProductById(warehouseProductId);
            warehouseProductItem.setWarehouseProduct(warehouseProduct);
        }
        if (productId != 0) warehouseProductItem.setProduct(productService.getProductById(productId));
        warehouseProductItem.setCount(storedCount);
        warehouseProductItem.setIncomePrice(incomePrice);
        warehouseProductItem.setCreatedAt(new Date());
        warehouseProductItemService.saveWarehouseProductItem(warehouseProductItem);
        itemStatusService.countIncomeWhDoc(warehouseProduct);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/module/warehouse-product-item/" + warehouseProductId);
    }

    @Transactional
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('WarehouseProductItemUpdate')")
    public RedirectView updateWarehouseProductItem(@RequestParam(name = "id") Long id,
                                                   @RequestParam(name = "warehouseProductId") Long warehouseProductId,
                                                   @RequestParam(name = "productId") Long productId,
                                                   @RequestParam(name = "storedCountEdit") BigDecimal storedCount,
                                                   @RequestParam(name = "incomePriceEdit") BigDecimal incomePrice, RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        WarehouseProductItem warehouseProductItem = warehouseProductItemService.getWarehouseProductItemById(id);
        if (warehouseProductId != 0)
            warehouseProductItem.setWarehouseProduct(warehouseProductService.getWarehouseProductById(warehouseProductId));
        if (productId != 0) warehouseProductItem.setProduct(productService.getProductById(productId));
        warehouseProductItem.setCount(storedCount);
        warehouseProductItem.setIncomePrice(incomePrice);
        warehouseProductItem.setUpdatedAt(new Date());
        warehouseProductItemService.saveWarehouseProductItem(warehouseProductItem);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/module/warehouse-product-item/" + warehouseProductId);
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('WarehouseProductItemDelete')")
    public RedirectView deleteWarehouseProductItem(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        warehouseProductItemService.deleteWarehouseProductItem(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/module/warehouse-product-item/" + id);
    }
}

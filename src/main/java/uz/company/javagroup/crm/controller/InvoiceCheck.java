package uz.isd.javagroup.grandcrm.controller;

import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseProduct;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseProductItem;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.modules.WarehouseProductItemService;
import uz.isd.javagroup.grandcrm.services.modules.WarehouseProductService;
import uz.isd.javagroup.grandcrm.utility.QRCodeGenerator;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("check/")
public class InvoiceCheck extends BaseController{


    @Autowired
    WarehouseProductItemService warehouseProductItemService;

    @Autowired
    WarehouseProductService warehouseProductService;


    @RequestMapping(path = "/{regNumber}", method = RequestMethod.GET)
    public ModelAndView createPDF(@PathVariable(name = "regNumber") Optional<String> regNumber,
                                  ModelAndView modelAndView) throws RecordNotFoundException, IOException, WriterException {

        Optional<WarehouseProduct> warehouseProduct = warehouseProductService.findWarehouseProductByRegNumber(regNumber.get());

        if(!warehouseProduct.isPresent()){

            modelAndView.setViewName("/pages/invoice-error");

            return modelAndView;

        }

        List<WarehouseProductItem> warehouseProductItems = warehouseProductItemService.findAllByWarehouseProductId(warehouseProduct.get().getId());

        double summa = 0.0;

        for (WarehouseProductItem item:warehouseProductItems){

            summa += item.getCount().doubleValue() * item.getIncomePrice().doubleValue();

        }

        QRCodeGenerator.generateQRCodeImage("http://isd-crm.uz:8080/check/" + warehouseProduct.get().getRegNumber(), 110, 110, "./src/main/resources/static/photo/QRCode.png");

        modelAndView.addObject("summa", summa);
        modelAndView.addObject("warehouseProduct", warehouseProduct.get());
        modelAndView.addObject("warehouseProductItems", warehouseProductItems);


        modelAndView.setViewName("/pages/reports/transfer-receive/warehouse-transfer-receive-product");

        return modelAndView;
    }

}

package uz.isd.javagroup.grandcrm.controller.reports;

import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.modules.*;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.modules.MonthlyConversionItemRepository;
import uz.isd.javagroup.grandcrm.repository.modules.ProductionExpenseRepository;
import uz.isd.javagroup.grandcrm.services.modules.MonthlyConversionService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("report/monthly-conversion-report-print")
public class MonthlyConversionReportController extends BaseController {

    @Autowired
    MonthlyConversionItemRepository monthlyConversionItemRepository;

    @Autowired
    ProductionExpenseRepository productionExpenseRepository;

    @Autowired
    MonthlyConversionService monthlyConversionService;


    @RequestMapping(path = "/pdf/{monthlyConversionId}", method = RequestMethod.GET)
    public ModelAndView createPDF(@PathVariable(name = "monthlyConversionId") Optional<Long> monthlyConversionId, ModelAndView modelAndView) throws RecordNotFoundException, IOException, WriterException {

        List<MonthlyConversionItem> monthlyConversionItems = monthlyConversionItemRepository.findAllByMonthlyConversionId(monthlyConversionId.get());
        List<ProductionExpense> productionExpenses = productionExpenseRepository.findAllByMonthlyConversionId(monthlyConversionId.get());

        MonthlyConversion monthlyConversion = monthlyConversionService.getMonthlyConversionById(monthlyConversionId.get());

        double monthlyConversionItemTotalAmount = 0.0;
        double ProductionExpenseTotalAmount = 0.0;

        for (MonthlyConversionItem item: monthlyConversionItems){

            monthlyConversionItemTotalAmount +=  item.getAmount().doubleValue();

        }

        for (ProductionExpense item: productionExpenses){

            ProductionExpenseTotalAmount +=  item.getOverAll().doubleValue();

        }

        double totalPrice = monthlyConversion.getAmount().doubleValue() - (monthlyConversionItemTotalAmount + ProductionExpenseTotalAmount);
        double taxPrice = totalPrice * 0.12;
        double overAllPrice = totalPrice - taxPrice;

        modelAndView.addObject("totalPrice",    totalPrice);
        modelAndView.addObject("taxPrice",      taxPrice);
        modelAndView.addObject("overAllPrice",  overAllPrice);
        modelAndView.addObject("monthlyConversion",  monthlyConversion);
        modelAndView.addObject("productionExpenses",     productionExpenses);
        modelAndView.addObject("monthlyConversionItems", monthlyConversionItems);

        modelAndView.setViewName("/pages/reports/monthly-convertion-report/page-invoice-monthly-convertion");
        return modelAndView;
    }
}

package uz.isd.javagroup.grandcrm.controller.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseRequest;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseRequestItem;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.modules.WarehouseRequestItemRepository;
import uz.isd.javagroup.grandcrm.repository.modules.WarehouseRequestRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("module/production-request-item")
public class ProductionRequestItemController extends BaseController {

    @Autowired
    WarehouseRequestItemRepository warehouseRequestItemRepository;
    @Autowired
    WarehouseRequestRepository warehouseRequestRepository;

    @RequestMapping(path = "/show/{productionRequestId}", method = RequestMethod.GET)
    public ModelAndView requestItemsShow(@PathVariable(name = "productionRequestId") Optional<Long> productionRequestId,
                                         @ModelAttribute("status") String typesData,
                                         @ModelAttribute("message") String messageData,
                                         ModelAndView modelAndView) throws RecordNotFoundException {

        if (!productionRequestId.isPresent()) throw new RecordNotFoundException("Production Request not found...");
        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }
        List<WarehouseRequestItem> productionRequestItems = warehouseRequestItemRepository.findAllByWarehouseRequestIdAndRemainingGreaterThanNative(productionRequestId.get(), BigDecimal.ZERO);
        Optional<WarehouseRequest> productionRequest = warehouseRequestRepository.findById(productionRequestId.get());
        if (!productionRequest.isPresent()) throw new RecordNotFoundException("Production Request not found...");

        modelAndView.addObject("productionRequestItems", productionRequestItems);
        modelAndView.addObject("productionRequest", productionRequest.get());
        modelAndView.setViewName("/pages/modules/production-request-items/show");
        return modelAndView;
    }

}

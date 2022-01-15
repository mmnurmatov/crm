package uz.isd.javagroup.grandcrm.controller.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

import static uz.isd.javagroup.grandcrm.entity.modules.WarehouseRequest.DocumentStatus.PENDING;
import static uz.isd.javagroup.grandcrm.entity.modules.WarehouseRequest.DocumentStatus.READY;

@Controller
@RequestMapping("module/warehouse-request-item")
public class WarehouseRequestItemController extends BaseController {
    @Autowired
    WarehouseRequestItemRepository warehouseRequestItemRepository;
    @Autowired
    WarehouseRequestRepository warehouseRequestRepository;

    @RequestMapping(path = "/{warehouseRequestId}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('WarehouseRequestItemRead')")
    public ModelAndView index(@PathVariable(name = "warehouseRequestId") Optional<Long> warehouseRequestId,
                              @ModelAttribute("status") String typesData,
                              @ModelAttribute("message") String messageData,
                              ModelAndView modelAndView) throws RecordNotFoundException {

        if (!warehouseRequestId.isPresent()) throw new RecordNotFoundException("warehouseRequest not found...");
        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }
        Optional<WarehouseRequest> warehouseRequest = warehouseRequestRepository.findById(warehouseRequestId.get());
        if (!warehouseRequest.isPresent()) throw new RecordNotFoundException("warehouseRequest not found...");
        List<Object[]> warehouseRequestItems;
        if (warehouseRequest.get().getDocumentStatus().equals(PENDING))
            warehouseRequestItems = warehouseRequestItemRepository.findAllByWarehouseRequestIdAndRemainingGreaterThan(warehouseRequestId.get(), BigDecimal.ZERO);
        else
            warehouseRequestItems = warehouseRequestItemRepository.findAllByWarehouseRequestIdReadyStatus(warehouseRequestId.get());

        modelAndView.addObject("warehouseRequestItems", warehouseRequestItems);
        modelAndView.addObject("warehouseRequest", warehouseRequest.get());
        modelAndView.setViewName("/pages/modules/warehouse-request-items/index");
        return modelAndView;
    }

    @RequestMapping(path = "/show/{warehouseRequestId}", method = RequestMethod.GET)
    public ModelAndView requestItemsShow(@PathVariable(name = "warehouseRequestId") Optional<Long> warehouseRequestId,
                                         @ModelAttribute("status") String typesData,
                                         @ModelAttribute("message") String messageData,
                                         ModelAndView modelAndView) throws RecordNotFoundException {

        if (!warehouseRequestId.isPresent()) throw new RecordNotFoundException("warehouseRequest not found...");
        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }
        List<WarehouseRequestItem> warehouseRequestItems = warehouseRequestItemRepository.findAllByWarehouseRequestIdAndRemainingGreaterThanNative(warehouseRequestId.get(), BigDecimal.ZERO);
        Optional<WarehouseRequest> warehouseRequest = warehouseRequestRepository.findById(warehouseRequestId.get());
        if (!warehouseRequest.isPresent()) throw new RecordNotFoundException("warehouseRequest not found...");

        modelAndView.addObject("warehouseRequestItems", warehouseRequestItems);
        modelAndView.addObject("warehouseRequest", warehouseRequest.get());
        modelAndView.setViewName("/pages/modules/warehouse-request-items/show");
        return modelAndView;
    }
}

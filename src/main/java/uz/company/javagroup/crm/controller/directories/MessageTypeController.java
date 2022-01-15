package uz.isd.javagroup.grandcrm.controller.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.directories.MessageType;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.MessageTypeService;

import java.util.List;

@Controller
@RequestMapping("directory/message-types")
public class MessageTypeController extends BaseController {

    @Autowired
    MessageTypeService messageTypeService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('MessageTypeRead')")
    public ModelAndView index(ModelAndView modelAndView, @ModelAttribute("status") String typesData, @ModelAttribute("message") String messageData) {

        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }

        List<MessageType> messageTypes = messageTypeService.findAll();
        modelAndView.setViewName("/pages/directories/message-types/index");
        modelAndView.addObject("messageTypes", messageTypes);
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('MessageTypeCreate')")
    public RedirectView createMessageType(@RequestParam(name = "nameUz") String nameUz,
                                          @RequestParam(name = "nameRu") String nameRu,
                                          @RequestParam(name = "nameEn") String nameEn,
                                          RedirectAttributes redirectAttributes) {
        MessageType messageType = new MessageType();
        messageType.setNameUz(nameUz);
        messageType.setNameRu(nameRu);
        messageType.setNameEn(nameEn);
        messageTypeService.saveMessageType(messageType);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/directory/message-types/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('MessageTypeUpdate')")
    public RedirectView updateMessageType(@RequestParam(name = "id") Long id,
                                          @RequestParam(name = "nameUzEdit") String nameUz,
                                          @RequestParam(name = "nameRuEdit") String nameRu,
                                          @RequestParam(name = "nameEnEdit") String nameEn,
                                          RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        MessageType messageType = messageTypeService.getMessageTypeById(id);
        messageType.setId(id);
        messageType.setNameRu(nameRu);
        messageType.setNameUz(nameUz);
        messageType.setNameEn(nameEn);
        messageTypeService.saveMessageType(messageType);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/directory/message-types/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('MessageTypeDelete')")
    public RedirectView deleteMessageType(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        messageTypeService.deleteMessageType(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/directory/message-types/");
    }
}

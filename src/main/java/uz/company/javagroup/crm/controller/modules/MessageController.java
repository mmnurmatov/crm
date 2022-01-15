package uz.isd.javagroup.grandcrm.controller.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import uz.isd.javagroup.grandcrm.entity.modules.Message;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.MessageTypeService;
import uz.isd.javagroup.grandcrm.services.modules.MessageService;

import java.awt.*;
import java.util.List;

@Controller
@RequestMapping("module/message")
public class MessageController extends BaseController {

    @Autowired
    MessageService messageService;

    @Autowired
    MessageTypeService messageTypeService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('MessageRead')")
    public ModelAndView index(ModelAndView modelAndView, @ModelAttribute("status") String typesData, @ModelAttribute("message") String messageData) {

        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }

        List<Message> messages = messageService.findAll();
        List<MessageType> messageTypes = messageTypeService.findAll();
        modelAndView.addObject("messages", messages);
        modelAndView.addObject("messageTypes", messageTypes);
        modelAndView.setViewName("/pages/modules/messages/index");
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('MessageCreate')")
    public RedirectView createMessage(@RequestParam(name = "messageType") Long messageTypeId,
                                      @RequestParam(name = "code") String code,
                                      @RequestParam(name = "titleUz") String titleUz,
                                      @RequestParam(name = "titleRu") String titleRu,
                                      @RequestParam(name = "titleEn") String titleEn,
                                      @RequestParam(name = "textUz") String textUz,
                                      @RequestParam(name = "textRu") String textRu,
                                      @RequestParam(name = "textEn") String textEn,
                                      RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        Message message = new Message();
        message.setCode(code);
        message.setTitleUz(titleUz);
        message.setTitleRu(titleRu);
        message.setTitleEn(titleEn);
        message.setTextUz(textUz);
        message.setTextRu(textRu);
        message.setTextEn(textEn);
        message.setMessageType(messageTypeService.getMessageTypeById(messageTypeId));
        messageService.saveMessage(message);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/module/message/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('MessageUpdate')")
    public RedirectView updateMessage(@RequestParam(name = "id") Long id,
                                      @RequestParam(name = "messageTypeId") Long messageTypeId,
                                      @RequestParam(name = "codeEdit") String code,
                                      @RequestParam(name = "titleUzEdit") String titleUz,
                                      @RequestParam(name = "titleRuEdit") String titleRu,
                                      @RequestParam(name = "titleEnEdit") String titleEn,
                                      @RequestParam(name = "textUzEdit") String textUz,
                                      @RequestParam(name = "textRuEdit") String textRu,
                                      @RequestParam(name = "textEnEdit") String textEn,
                                      RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        Message message = messageService.getMessageById(id);
        message.setCode(code);
        message.setTitleUz(titleUz);
        message.setTitleRu(titleRu);
        message.setTitleEn(titleEn);
        message.setTextUz(textUz);
        message.setTextRu(textRu);
        message.setTextEn(textEn);
        message.setMessageType(messageTypeService.getMessageTypeById(messageTypeId));
        messageService.saveMessage(message);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/module/message/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('MessageDelete')")
    public RedirectView deleteMessage(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        messageService.deleteMessage(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/module/message/");
    }
}

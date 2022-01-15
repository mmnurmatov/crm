package uz.isd.javagroup.grandcrm.controller.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.directories.Ticket;
import uz.isd.javagroup.grandcrm.entity.directories.TicketFile;
import uz.isd.javagroup.grandcrm.entity.directories.TicketMessage;
import uz.isd.javagroup.grandcrm.entity.modules.User;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.directories.TicketMessageRepository;
import uz.isd.javagroup.grandcrm.services.directories.TicketFileService;
import uz.isd.javagroup.grandcrm.services.directories.TicketService;
import uz.isd.javagroup.grandcrm.services.modules.UserService;
import uz.isd.javagroup.grandcrm.utility.Status;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("directory/ticket-messages")
public class TicketMessageController extends BaseController {

    @Autowired
    UserService userService;
    @Autowired
    TicketService ticketService;
    @Autowired
    TicketMessageRepository ticketMessageRepository;
    @Autowired
    TicketFileService ticketFileService;

    @RequestMapping(path = "/{ticketId}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('TicketMessageRead')")
    public ModelAndView index(@PathVariable(name = "ticketId") Long ticketId, @ModelAttribute("status") String status, @ModelAttribute("message") String messageData, ModelAndView modelAndView) throws RecordNotFoundException {
        if (status.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", status);
            modelAndView.addObject("message", messageData);
        }
        boolean isAdmin = false;
        boolean isReply = false;

        User user = getAuthUserData();
        if (user.getRole().getCode().equals("ADMIN")){
            isAdmin = true;
        }

        Ticket ticket = ticketService.getTicketById(ticketId);
        List<TicketMessage> ticketMessages = ticketMessageRepository.findAllByTicketIdOrderByIdDesc(ticketId);
        for (TicketMessage ticketMessage : ticketMessages) {
            if (ticketMessage.getAdminId() != 0L)
            {
                ticketMessage.setAdmin(userService.getUserById(ticketMessage.getAdminId()));
            }
            ticketMessage.setTicketFiles(ticketFileService.findAllByTicketMessageId(ticketMessage.getId()));
        }

        String str = String.valueOf(ticket.getStatus());
        if(str.equals("CLOSED")){
            isReply = true;
        }

        modelAndView.setViewName("/pages/directories/tickets/message");
        modelAndView.addObject("ticket", ticket);
        modelAndView.addObject("isReply", isReply);
        modelAndView.addObject("isAdmin", isAdmin);
        modelAndView.addObject("ticketMessages", ticketMessages);
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('TicketMessageCreate')")
    public RedirectView createTicket(@RequestParam(name = "ticketId") Long ticketId,
                                     @RequestParam(name = "message") String message,
                                     @RequestParam(name = "files") List<MultipartFile> files, RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        Ticket ticket = ticketService.getTicketById(ticketId);
        User user = getAuthUserData();
        ticket.setUpdatedAt(new Date());
        TicketMessage ticketMessage = new TicketMessage();
        if (user.getRole().getCode().equals("ADMIN")){
            ticketMessage.setAdminId(user.getId());
            ticket.setStatus(Status.ANSWERED);
        }
        else {
            ticketMessage.setAdminId(0L);
            ticket.setStatus(Status.PENDING);
        }
        ticketService.saveTicket(ticket);
        ticketMessage.setTicket(ticket);
        ticketMessage.setMessage(message);
        ticketMessage.setDate(new Date());
        ticketMessageRepository.save(ticketMessage);
        if (files != null && files.size() > 0) ticketFileService.saveMessageFile(ticketMessage, files);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/directory/ticket-messages/" + ticketId);
    }


    @RequestMapping(path = "/delete-file", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('TicketMessageDelete')")
    public RedirectView deleteTicketFile(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        ticketFileService.deleteFile(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/directory/ticket-messages/");
    }
}

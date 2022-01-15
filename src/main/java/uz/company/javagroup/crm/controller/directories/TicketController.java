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
import uz.isd.javagroup.grandcrm.entity.directories.TicketMessage;
import uz.isd.javagroup.grandcrm.entity.modules.User;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.directories.TicketMessageRepository;
import uz.isd.javagroup.grandcrm.services.directories.TicketFileService;
import uz.isd.javagroup.grandcrm.services.directories.TicketService;
import uz.isd.javagroup.grandcrm.utility.Priority;
import uz.isd.javagroup.grandcrm.utility.Status;
import uz.isd.javagroup.grandcrm.utility.Utils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("directory/tickets")
public class TicketController extends BaseController {

    @Autowired
    TicketService ticketService;
    @Autowired
    TicketMessageRepository ticketMessageRepository;
    @Autowired
    TicketFileService ticketFileService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('TicketRead')")
    public ModelAndView index(ModelAndView modelAndView, @ModelAttribute("status") String status, @ModelAttribute("message") String messageData) {
        if (status.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", status);
            modelAndView.addObject("message", messageData);
        }
        List<Ticket> tickets;
        boolean isAdmin = false;
        User user = getAuthUserData();
        if (user.getRole().getCode().equals("ADMIN")){

            tickets = ticketService.findAll();
            isAdmin = true;
        }
        else{
            tickets = ticketService.findAllByUserId(user.getId());
        }

        modelAndView.setViewName("/pages/directories/tickets/index");
        modelAndView.addObject("priorities", Arrays.asList(Priority.values()));
        modelAndView.addObject("tickets", tickets);
        modelAndView.addObject("isAdmin", isAdmin);
        modelAndView.addObject("statuses", Arrays.asList(Status.values()));
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('TicketCreate')")
    public ModelAndView create(ModelAndView modelAndView) {
        modelAndView.addObject("priorities", Arrays.asList(Priority.values()));
//        modelAndView.addObject("statuses", Arrays.asList(Status.values()));
        modelAndView.setViewName("/pages/directories/tickets/create");
        return modelAndView;
    }

    @RequestMapping(path = "/close/{ticketId}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('TicketCreate')")
    public RedirectView closeTicketByTicketId(@PathVariable(name = "ticketId") Long ticketId,
                                              RedirectAttributes redirectAttributes) throws RecordNotFoundException {

        Ticket ticket = ticketService.getTicketById(ticketId);
        ticket.setId(ticketId);
        ticket.setStatus(Status.CLOSED);
        ticket.setUpdatedAt(new Date());
        ticketService.saveTicket(ticket);


        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/directory/tickets/");
    }


    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public RedirectView createTicket(@RequestParam(name = "createdAt") Date createdAt,
                                     @RequestParam(name = "priority") String priority,
//                                     @RequestParam(name = "status") String status,
                                     @RequestParam(name = "title") String title,
                                     @RequestParam(name = "message") String message,
                                     @RequestParam(name = "files") List<MultipartFile> files, RedirectAttributes redirectAttributes) {
        User user = getAuthUserData();
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setPriority(Priority.valueOf(priority));
        ticket.setStatus(Status.ACTIVE);
        ticket.setTitle(title);
        ticket.setCreatedAt(new Date());
        ticket.setUpdatedAt(new Date());
        ticketService.saveTicket(ticket);
        Utils.generateRegNumber(ticket);
        ticketService.saveTicket(ticket);

        TicketMessage ticketMessage = new TicketMessage();
        if (user.getRole().getCode().equals("ADMIN")) ticketMessage.setAdminId(user.getId());
        else ticketMessage.setAdminId(0L);
        ticketMessage.setTicket(ticket);
        ticketMessage.setMessage(message);
        ticketMessage.setDate(new Date());
        ticketMessageRepository.save(ticketMessage);
        if (files != null && files.size() > 0) ticketFileService.saveMessageFile(ticketMessage, files);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/directory/tickets/");
    }

}

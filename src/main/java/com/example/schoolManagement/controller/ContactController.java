package com.example.schoolManagement.controller;

import com.example.schoolManagement.model.Contact;
import com.example.schoolManagement.service.ContactService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

@Slf4j
@Controller
public class ContactController {

    //private static Logger log = LoggerFactory.getLogger(ContactController.class);

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public String displayContactPage(Model model){
        model.addAttribute("contact", new Contact());
        log.info("Inside contact page {}", "dinesh");
        return "contact.html";
    }

//   @RequestMapping(value = "/saveMsg", method = RequestMethod.POST)
//    public ModelAndView saveMessage(@RequestParam String name, @RequestParam String mobileNum, @RequestParam String email,
//                                    @RequestParam String subject, @RequestParam String message
//                                    ){
//        log.info("Name : " + name);
//        log.info("Mobile Number : " + mobileNum);
//        log.info("Email : " + email);
//        log.info("Subject : " + subject);
//        log.info("Message : " + message);
//        return new ModelAndView("redirect:/contact");
//   }

//    @RequestMapping(value = "/saveMsg", method = RequestMethod.POST)
//    public ModelAndView saveMessage(Contact contact){
//        contactService.saveMessageDetails(contact);
//        return new ModelAndView("redirect:/contact");
//    }

    @RequestMapping(value = "/saveMsg", method = RequestMethod.POST)
    public String saveMessage(@Valid @ModelAttribute("contact") Contact contact, Errors errors){
        if(errors.hasErrors()){
            log.error("Contact form validation error due to {}", errors.toString());
            return "contact.html";
        }
        contactService.saveMessageDetails(contact);
        return "redirect:/contact";
    }

//    @RequestMapping(value = {"/displayMessages"})
//    public ModelAndView displayMessages(Model model){
//        List<Contact> contactMsgs = contactService.findMsgsWithOpenStatus();
//        //System.out.println(contactMsgs);
//        ModelAndView modelAndView = new ModelAndView("messages.html");
//        modelAndView.addObject("contactMsgs", contactMsgs);
//        return modelAndView;
//    }

    @RequestMapping(value = {"/displayMessages/page/{pageNum}"})
    public ModelAndView displayMessages(Model model, @PathVariable(name = "pageNum") int pageNum,
                                        @RequestParam(name = "sortField") String sortField,
                                        @RequestParam(name = "sortDir") String sortDir){
        Page<Contact> msgPage = contactService.findMsgsWithOpenStatus(pageNum, sortField, sortDir);
        List<Contact> contactMsgs = msgPage.getContent();
        ModelAndView modelAndView = new ModelAndView("messages.html");
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", msgPage.getTotalPages());
        model.addAttribute("totalMsgs", msgPage.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        modelAndView.addObject("contactMsgs",contactMsgs);
        return modelAndView;
    }

    @RequestMapping(value = "/closeMsg", method = RequestMethod.GET)
    public String closeMsg(@RequestParam int id, Authentication authentication){
        //contactService.updateMsgStatus(id, authentication.getName());
        contactService.updateMsgStatus(id);
        return "redirect:/displayMessages";
    }
}

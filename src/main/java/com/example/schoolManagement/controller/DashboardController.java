package com.example.schoolManagement.controller;

import com.example.schoolManagement.model.Person;
import com.example.schoolManagement.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class DashboardController {

    @Autowired
    PersonRepository personRepository;

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String displayDashboard(Model model, Authentication authentication, HttpSession httpSession){
        Person person = personRepository.readByEmail(authentication.getName());
        model.addAttribute("username", person.getName());
        model.addAttribute("roles", authentication.getAuthorities().toString());
        if(person.getEazyClass() != null && person.getEazyClass().getName() != null){
            model.addAttribute("enrolledClass", person.getEazyClass().getName());
        }
        httpSession.setAttribute("loggedInPerson", person);
        return "dashboard.html";
//        model.addAttribute("username", authentication.getName());
//        model.addAttribute("roles", authentication.getAuthorities().toString());
//        return "dashboard.html";
    }
}

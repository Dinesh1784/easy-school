package com.example.schoolManagement.controller;

import com.example.schoolManagement.model.Courses;
import com.example.schoolManagement.model.EazyClass;
import com.example.schoolManagement.model.Person;
import com.example.schoolManagement.repository.CoursesRepository;
import com.example.schoolManagement.repository.EazyClassRepository;
import com.example.schoolManagement.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequestMapping(value = "admin")
public class AdminController {

    @Autowired
    EazyClassRepository eazyClassRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    CoursesRepository coursesRepository;

    @RequestMapping(value = "/displayClasses", method = {RequestMethod.GET})
    public ModelAndView displayClasses(Model model){
        List<EazyClass> allClasses = eazyClassRepository.findAll();
        ModelAndView modelAndView = new ModelAndView("classes.html");
        modelAndView.addObject("eazyClasses", allClasses);
        modelAndView.addObject("eazyClass", new EazyClass());
        return modelAndView;
    }

    @RequestMapping(value = "/addNewClass", method = {RequestMethod.POST})
    public ModelAndView addNewClass(@ModelAttribute("eazyClass") EazyClass eazyClass, Model model){
        eazyClassRepository.save(eazyClass);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayClasses");
        return modelAndView;
    }

    @RequestMapping(value = "/deleteClass")
    public ModelAndView deleteClass(Model model, @RequestParam int id){
        Optional<EazyClass> eazyClass = eazyClassRepository.findById(id);
        for (Person person: eazyClass.get().getPersons()){
            person.setEazyClass(null);
            personRepository.save(person);
        }
        eazyClassRepository.deleteById(id);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayClasses");
        return modelAndView;
    }

    @RequestMapping(value = "/displayStudents", method = RequestMethod.GET)
    public ModelAndView displayStudents(Model model, @RequestParam int classId, HttpSession httpSession,
                                        @RequestParam(value = "error", required = false) String error){
        String errorMessage = null;
        ModelAndView modelAndView = new ModelAndView("students.html");
        Optional<EazyClass> eazyClass = eazyClassRepository.findById(classId);
        modelAndView.addObject("eazyClass", eazyClass.get());
        modelAndView.addObject("person", new Person());
        httpSession.setAttribute("eazyClass", eazyClass.get());
        if(error != null){
            errorMessage = "Invalid Email is entered";
            modelAndView.addObject("errorMessage", errorMessage);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/addStudent", method = RequestMethod.POST)
    public ModelAndView addStudent(Model model, @ModelAttribute("person") Person person, HttpSession httpSession){
        ModelAndView modelAndView = new ModelAndView();
        EazyClass eazyClass = (EazyClass) httpSession.getAttribute("eazyClass");
        Person personEntity = personRepository.readByEmail(person.getEmail());
        if(personEntity == null || !(personEntity.getPersonId() > 0)){
            modelAndView.setViewName("redirect:/admin/displayStudents?classId="+eazyClass.getClassId()+"&error=true");
            return modelAndView;
        }
        personEntity.setEazyClass(eazyClass);
        personRepository.save(personEntity);
        eazyClass.getPersons().add(personEntity);
        eazyClassRepository.save(eazyClass);
        modelAndView.setViewName("redirect:/admin/displayStudents?classId="+eazyClass.getClassId());
        return modelAndView;
    }

    @RequestMapping(value = "/deleteStudent", method = RequestMethod.GET)
    public ModelAndView deleteStudent(Model model, @RequestParam int personId, HttpSession httpSession){
        EazyClass eazyClass = (EazyClass) httpSession.getAttribute("eazyClass");
        Optional<Person> person = personRepository.findById(personId);
        person.get().setEazyClass(null);
        eazyClass.getPersons().remove(person.get());
        personRepository.save(person.get());
        EazyClass eazyClassSaved = eazyClassRepository.save(eazyClass);
        httpSession.setAttribute("eazyClass", eazyClassSaved);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayStudents?classId="+eazyClass.getClassId());
        return modelAndView;
    }

    @RequestMapping(value = "/displayCourses", method = {RequestMethod.GET})
    public ModelAndView displayCourses(Model model){
        //List<Courses> courses = coursesRepository.findAll();
//        List<Courses> coursess = coursesRepository.findAll(Sort.by("name").descending()
//                .and(Sort.by("id").descending())
//                .and(Sort.by("etc").ascending())
//        );
        List<Courses> courses = coursesRepository.findAll(Sort.by("name").descending());
        ModelAndView modelAndView = new ModelAndView("courses_secure.html");
        modelAndView.addObject("courses",courses);
        modelAndView.addObject("course", new Courses());
        return modelAndView;
    }

    @RequestMapping(value = "/addNewCourse", method = {RequestMethod.POST})
    public ModelAndView addNewCourse(Model model, @ModelAttribute("course") Courses course){
        ModelAndView modelAndView = new ModelAndView();
        coursesRepository.save(course);
        modelAndView.setViewName("redirect:/admin/displayCourses");
        return modelAndView;
    }

    @RequestMapping(value = "/viewStudents", method = {RequestMethod.GET})
    public ModelAndView viewStudents(Model model, @RequestParam int id, HttpSession httpSession,
    @RequestParam(value = "error", required = false) String error
    ){
        String errorMessage = null;
        ModelAndView modelAndView = new ModelAndView("courses_students.html");
        Optional<Courses> courses = coursesRepository.findById(id);
        httpSession.setAttribute("courses", courses.get());
        modelAndView.addObject("courses", courses.get());
        modelAndView.addObject("person", new Person());
        if(error != null){
            errorMessage = "Invalid Email entered";
            return modelAndView;
        }
        return modelAndView;
    }

    @RequestMapping(value = "/addStudentToCourse", method = {RequestMethod.POST})
    public ModelAndView addStudentToCourse(Model model, @ModelAttribute("poerson") Person person, HttpSession httpSession ){
        ModelAndView modelAndView = new ModelAndView();
        Courses courses = (Courses) httpSession.getAttribute("courses");
        Person personEntity = personRepository.readByEmail(person.getEmail());
        if(personEntity == null || !(personEntity.getPersonId() > 0)){
            modelAndView.setViewName("redirect:/admin/viewStudents?id="+courses.getCourseId()+"&error=true");
            return modelAndView;
        }
        personEntity.getCourses().add(courses);
        courses.getPersons().add(personEntity);
        personRepository.save(personEntity);
        httpSession.setAttribute("courses", courses);
        modelAndView.setViewName("redirect:/admin/viewStudents?id="+courses.getCourseId());
        return modelAndView;
    }

    @RequestMapping(value = "/deleteStudentFromCourse", method = {RequestMethod.GET})
    public ModelAndView deleteStudentFromCourse(Model model, @RequestParam int personId, HttpSession httpSession){
        Courses courses = (Courses) httpSession.getAttribute("courses");
        Optional<Person> person = personRepository.findById(personId);
        person.ifPresent(stu -> {
            stu.getCourses().remove(courses);
            courses.getPersons().remove(stu);
            personRepository.save(stu);
            httpSession.setAttribute("courses", courses);
        });
//        person.get().getCourses().remove(courses);
//        courses.getPersons().remove(person);
//        personRepository.save(person.get());
//        httpSession.setAttribute("courses", courses);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/viewStudents?id="+courses.getCourseId());
        return modelAndView;
    }

}

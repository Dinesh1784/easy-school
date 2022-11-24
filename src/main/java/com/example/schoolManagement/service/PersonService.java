package com.example.schoolManagement.service;

import com.example.schoolManagement.constants.EazySchoolConatsnts;
import com.example.schoolManagement.model.Person;
import com.example.schoolManagement.model.Roles;
import com.example.schoolManagement.repository.PersonRepository;
import com.example.schoolManagement.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    RolesRepository rolesRepository;

    public boolean createNewPerson(Person person){
        boolean isSaved = false;
        Roles role = rolesRepository.getByRoleName(EazySchoolConatsnts.STUDENT_ROLE);
        person.setRoles(role);
        person = personRepository.save(person);
        if(person != null && person.getPersonId() > 1){
            isSaved = true;
        }
        return isSaved;
    }
}

package com.example.schoolManagement.controller;

import com.example.schoolManagement.constants.EazySchoolConatsnts;
import com.example.schoolManagement.model.Contact;
import com.example.schoolManagement.model.Response;
import com.example.schoolManagement.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/api/contact")
@CrossOrigin(origins = "*")
public class ContactRestController {

    @Autowired
    ContactRepository contactRepository;

    @GetMapping(value = "/getMessagesByStatus", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Contact> getMessagesByStatus(@RequestParam(value = "status") String status){
        return contactRepository.findByStatus(status);
    }

    @GetMapping(value = "/getAllMsgsByStatus")
    public List<Contact> getAllMsgsByStatus(@RequestBody Contact contact){
       if(contact != null && contact.getStatus() != null){
           return contactRepository.findByStatus(contact.getStatus());
       }else{
           return List.of();
       }
    }

    @PostMapping(value = "/saveMsg", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> saveMsg(@RequestHeader(value = "invocationFrom") String invocationFrom, @Valid @RequestBody Contact contact){
        log.info("Header from invocationFrom = {}", invocationFrom);
        contactRepository.save(contact);
        Response response = new Response();
        response.setStatusCode("200");
        response.setStatusMsg("Message saved successfully");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("isMsgSaved", "true")
                .body(response);
    }

    @DeleteMapping(value = "/deleteMsg")
    public ResponseEntity<?> deleteMsg(RequestEntity<Contact> requestEntity){
        //RequestEntity has the contact model as well as header object
        HttpHeaders httpHeaders = requestEntity.getHeaders();
        httpHeaders.forEach((key, value) -> {
            log.info("Headers {} = {}", key, value.stream().collect(Collectors.joining("|")));
        });
        Contact contact = requestEntity.getBody();
        contactRepository.deleteById(contact.getContactId());
        Response response = new Response();
        response.setStatusCode("200");
        response.setStatusMsg("Message deleted successfully");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping(value = "/closeMsg")
    public ResponseEntity<?> closeMsg(@RequestBody Contact contactReq){
        Response response = new Response();
        Optional<Contact> contact = contactRepository.findById(contactReq.getContactId());
        if(contact.isPresent()){
            contact.get().setStatus(EazySchoolConatsnts.CLOSE);
            contactRepository.save(contact.get());
        }else{
            response.setStatusCode("400");
            response.setStatusMsg("Invalid Contact ID recieved");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
        response.setStatusCode("200");
        response.setStatusMsg("Message updated successfully");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}

package com.example.schoolManagement.service;

import com.example.schoolManagement.constants.EazySchoolConatsnts;
import com.example.schoolManagement.model.Contact;
import com.example.schoolManagement.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ContactService {

//    private static Logger log = LoggerFactory.getLogger(ContactService.class);
    @Autowired
    ContactRepository contactRepository;
    public boolean saveMessageDetails(Contact contact){
        boolean isSaved = false;
        //since using audit we dont need createdAt and createdBy
//        contact.setCreatedAt(LocalDateTime.now());
//        contact.setCreatedBy(EazySchoolConatsnts.ANONYMOUS);
        contact.setStatus(EazySchoolConatsnts.OPEN);
       // int result = contactRepository.saveContactMsg(contact);
        Contact savedContact = contactRepository.save(contact);
//        if(result > 0){
//            isSaved = true;
//        }
        if(savedContact != null && savedContact.getContactId() > 0){
            isSaved = true;
        }
        log.info(contact.toString());
        return isSaved;
    }

    public List<Contact> findMsgsWithOpenStatus() {
        //List<Contact> contactMsgs = contactRepository.findMsgsWithStatus(EazySchoolConatsnts.OPEN);
        List<Contact> contactMsgs = contactRepository.findByStatus(EazySchoolConatsnts.OPEN);
        return contactMsgs;
    }

    public boolean updateMsgStatus(int contactId, String updatedBy) {
        boolean isUpdated = false;
        //int result = contactRepository.updateMsgStatus(contactId, EazySchoolConatsnts.CLOSE, updatedBy);
        Optional<Contact> contact = contactRepository.findById(contactId);
        contact.ifPresent(contact1 -> {
            contact1.setStatus(EazySchoolConatsnts.CLOSE);
            //since using audit we dont need updatedAt and updatedBy
//            contact1.setUpdatedBy(updatedBy);
//            contact1.setUpdatedAt(LocalDateTime.now());
        });
        Contact updatedContact = contactRepository.save(contact.get());
        if(updatedContact != null && updatedContact.getUpdatedBy() != null) {
            isUpdated = true;
        }
        return isUpdated;
    }
}

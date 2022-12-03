package com.example.schoolManagement.repository;

import com.example.schoolManagement.model.Contact;
import com.example.schoolManagement.rowmapper.ContactRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Integer> {
    List<Contact> findByStatus(String status);

    //"Select c FROM Contact c where c.status = :status" or "Select c FROM Contact c where c.status = ?1"
    @Query("Select c FROM Contact c where c.status = :status")  // -> JPQL QUERY (Java persistence query language)
    //@Query(value = "Select * FROM contact_msg as c WHERE c.status = :status", nativeQuery = true) // -> native query
    Page<Contact> findByStatus(@Param("status") String status, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Contact c SET c.status = ?1 WHERE c.contactId = ?2")
    int updateStatusById(String status, int id);

    // NAMED QUERIES
//    Page<Contact> findOpenMsgs(@Param("status") String status, Pageable pageable);
//
//    @Transactional
//    @Modifying
//    int pdateMsgStatus(String status, int id);
}

//@Repository
//public class ContactRepository {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    public ContactRepository(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    public int saveContactMsg(Contact contact){
//        String sql = "INSERT INTO CONTACT_MSG (NAME,MOBILE_NUM,EMAIL,SUBJECT,MESSAGE,STATUS,CREATED_AT,CREATED_BY) VALUES (?,?,?,?,?,?,?,?)";
//        return jdbcTemplate.update(sql,contact.getName(),contact.getMobileNum(),
//                contact.getEmail(),contact.getSubject(),contact.getMessage(),
//                contact.getStatus(),contact.getCreatedAt(),contact.getCreatedBy());
//    }
//
//    public List<Contact> findMsgsWithStatus(String status) {
//        String sql = "SELECT * FROM CONTACT_MSG WHERE STATUS = ?";
//        return jdbcTemplate.query(sql, new PreparedStatementSetter() {
//            public void setValues(PreparedStatement preparedStatement) throws SQLException {
//                preparedStatement.setString(1, status);
//            }
//        }, new ContactRowMapper());
//    }
//
//    public int updateMsgStatus(int contactId, String status, String updatedBy) {
//        String sql = "UPDATE CONTACT_MSG SET STATUS = ?, UPDATED_BY = ?,UPDATED_AT =? WHERE CONTACT_ID = ?";
//        return jdbcTemplate.update(sql,new PreparedStatementSetter() {
//            public void setValues(PreparedStatement preparedStatement) throws SQLException {
//                preparedStatement.setString(1, status);
//                preparedStatement.setString(2, updatedBy);
//                preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
//                preparedStatement.setInt(4, contactId);
//            }
//        });
//    }
//}

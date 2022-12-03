package com.example.schoolManagement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "contact_msg")
@Data
@SqlResultSetMappings({
        @SqlResultSetMapping(name = "SqlResultSetMapping.count", columns = @ColumnResult(name = "cnt"))
})
@NamedQueries({
        @NamedQuery(name = "Contact.findOpenMsgs",
        query = "SELECT c FROM Contact c WHERE c.status = :status"),
        @NamedQuery(name = "Contact.updateMsgStatus",
        query = "UPDATE Contact c SET c.status = ?1 WHERE c.contactId = ?2")
})

@NamedNativeQueries({
        @NamedNativeQuery(name = "Contact.findOpenMsgsNative",
        query = "SELECT * FROM contact_msg c WHERE c.status = :status"),
        @NamedNativeQuery(name = "Contact.findOpenMsgsNative.count",
        query = "SELECT count(*) as cnt FROM contact_msg c WHERE c.status = :status",
        resultSetMapping = "SqlResultSetMapping.count"),
        @NamedNativeQuery(name = "Contact.updateMsgStatusNative",
        query = "UPDATE contact_msg c SET c.status = ?1 WHERE c.contactId = ?2")
})
@AllArgsConstructor
@NoArgsConstructor
public class Contact extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "contact_id")
//    @JsonProperty("id")
    private int contactId;

    @NotBlank(message = "Name must not be blank")
    @Size(min = 3, message = "Name must be atleast 3 characters long")
    private String name;

    @NotBlank(message = "Mobile number is must")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    private String mobileNum;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Subject must not be blank")
    @Size(min = 5, message = "Subject must be atleast 5 characters long")
    private String subject;

    @NotBlank(message = "Message must not be belank")
    @Size(min = 10, message = "Message must be atleast 10 characters long")
    private String message;

    private String status;
}

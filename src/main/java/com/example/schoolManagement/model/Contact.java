package com.example.schoolManagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {

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
}

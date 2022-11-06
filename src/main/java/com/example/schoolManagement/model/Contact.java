package com.example.schoolManagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    private String name;
    private String mobileNum;
    private String email;
    private String subject;
    private String message;
}

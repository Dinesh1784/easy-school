package com.example.schoolManagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "holidays")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Holiday extends BaseEntity {

    @Id
    private String day;
    private String reason;

    @Enumerated(EnumType.STRING)
    private Type type;

    public enum Type{
        FESTIVAL, FEDERAL
    }
}

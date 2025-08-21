package com.billa.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor@NoArgsConstructor
@Data
public class PersonDetails {
    private Long id;
    private String name;
    private String companyName;
    private String companyAddress;
    private String email;
}

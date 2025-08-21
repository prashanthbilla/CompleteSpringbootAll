package com.billa.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor@NoArgsConstructor
@Data
public class Prashanth {
    private Long id;
    private Long personId;
    private Integer amount;
    private Long addressId;
    private Long addressPersonId;
    private Integer addressAmount;
    private String name;
}
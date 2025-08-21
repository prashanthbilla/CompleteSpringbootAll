package com.billa.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jooq.Allow;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonAndAddress {
    private Long id;
    private Long personId;
    private Integer amount;
    private Long addressId;
    private Long addressPersonId;
    private Integer addressAmount;
}

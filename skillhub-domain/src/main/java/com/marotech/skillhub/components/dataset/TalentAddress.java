package com.marotech.skillhub.components.dataset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TalentAddress {
    private String streetNumber;
    private String streetName;
    private String suburb;
    private String city;
}

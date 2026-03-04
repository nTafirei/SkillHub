package com.marotech.skillhub.components.dataset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TalentProfile {
    private String firstName;
    private String lastName;
    private TalentAddress address;
    private TalentCoordinates coordinates;
    private String email;
    private String phone;
    private String description;
    private String nationalId;
}

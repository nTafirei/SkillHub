package com.marotech.skillhub.components.dataset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SkillProvider {
    private String category;
    private String skill;
    private TalentProfile profile;
}

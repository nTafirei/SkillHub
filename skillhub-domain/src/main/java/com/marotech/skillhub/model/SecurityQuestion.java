package com.marotech.skillhub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "security_question")
public class SecurityQuestion extends BaseEntity {

    @Column
    @NotNull
    private String question;

    @Column
    @NotNull
    private String answer;
    @Enumerated(EnumType.STRING)
    @NotNull
    private SecurityQuestionTag tag;
}
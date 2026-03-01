package com.marotech.skillhub.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "language_model")
public class LanguageModel extends BaseEntity {

    @Column(nullable = false, length = 36)
    private String name;
    @Column(length = 36)
    private String apiKey = "";
    @Column
    private String url = "";
}

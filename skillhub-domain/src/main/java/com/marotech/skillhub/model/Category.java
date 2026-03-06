package com.marotech.skillhub.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
@ToString
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name="category")
public class Category extends BaseEntity {

    @Column(nullable = false)
    @NotNull
    private String name;
}
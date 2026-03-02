package com.marotech.skillhub.model;


import com.marotech.skillhub.util.CategoryComparator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
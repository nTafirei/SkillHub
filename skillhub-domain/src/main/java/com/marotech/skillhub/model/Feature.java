package com.marotech.skillhub.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;


@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "feature")
public class Feature extends BaseEntity {

    @Column(nullable = false,length = 32)
    private String name;
}

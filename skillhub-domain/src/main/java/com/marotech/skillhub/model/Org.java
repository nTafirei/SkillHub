package com.marotech.skillhub.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;


@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Transactional
@Table(name = "org")
public class Org extends BaseEntity {

    @Column(nullable = false)
    private String name;
    @Column
    private String address;
    @Column
    private String address2;
    @Column
    private String email;
    @Column
    private String city;
    @Column
    private String province;
    @Column
    private String postcode;
    @Column
    private String country;
    @Column
    private String telephone;
    @Column
    private String fax;
}
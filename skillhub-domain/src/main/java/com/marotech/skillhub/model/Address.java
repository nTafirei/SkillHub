package com.marotech.skillhub.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "address")
public class Address extends BaseEntity {

    @Column(nullable = false)
    private String address;

    @ManyToOne(fetch = FetchType.EAGER)
    private Suburb suburb;
}

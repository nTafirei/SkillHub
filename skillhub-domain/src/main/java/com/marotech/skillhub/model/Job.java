package com.marotech.skillhub.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "job")
public class Job extends BaseEntity {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String title;
    @Column
    private String email;
    @Column(nullable = false)
    private String mobile;
    @Column(nullable = false)
    private String desc;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    private Suburb suburb;
}

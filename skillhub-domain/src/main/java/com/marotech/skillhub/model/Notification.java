package com.marotech.skillhub.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "notification")
public class Notification extends BaseEntity {

    @Column(nullable = false)
    private String subject;
    @Column(nullable = false)
    private String body;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    private User recipient;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    private Org org;
}

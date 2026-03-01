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
@Table(name = "node")
public class Node extends BaseEntity {

    @Column(nullable = false)
    private String id;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private int top;
    @Column(nullable = false)
    private int left;
    @Column(nullable = false)
    private int width;
    @Column(nullable = false)
    private int height;
}

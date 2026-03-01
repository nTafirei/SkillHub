package com.marotech.skillhub.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "embedding")
public class Embedding extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    private Publication publication;
    @Column
   // @JdbcTypeCode(SqlTypes.VECTOR)
    //@Array(length = 512) // dimensions
    private  List<Double> embedding;
    @Column(length = 512)
    private String chunk;
}
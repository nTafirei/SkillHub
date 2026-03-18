package com.marotech.skillhub.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "activity")
public class Activity extends BaseEntity {

    @Column(nullable = false)
    private String title;
    @Column
    private BigDecimal amount;
    @Column
    private String currency;
    @Column
    private BigDecimal originalAmount;
    @ManyToOne(fetch = FetchType.EAGER)
    private User actor;
    @Enumerated(EnumType.STRING)
    private ActivityType activityType;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> actions = new ArrayList<>();
    @OneToOne(fetch = FetchType.EAGER)
    private Attachment attachment;
    public void addAction(String action){
        actions.add(action);
    }
}
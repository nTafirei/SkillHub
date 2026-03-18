package com.marotech.skillhub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "activity_audit")
public class ActivityAudit extends BaseEntity {

    @Column(nullable = false)
    @NotNull
    private String sessionId;
    @Column(nullable = false)
    @NotNull
    private String serviceCode;
    @Column(nullable = false)
    @NotNull
    private String phoneNumber;
    @Column(length = 160, nullable = false)
    @NotNull
    private String ussdText;
    @Column(length = 160)
    private String servedContent;
}
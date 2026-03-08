package com.marotech.skillhub.model;

import com.marotech.skillhub.util.EmailStatus;
import com.marotech.skillhub.util.MessageStatus;
import com.marotech.skillhub.util.SMSStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    private String fromName;
    @Column
    private String fromEmail;
    @Column(nullable = false)
    private String fromMobile;
    @Column(nullable = false)
    private String subject;
    @Column(nullable = false)
    private String body;
    @Column(nullable = false)
    private String toName;
    @Column
    private String toEmail;
    @Column(nullable = false)
    private String toMobile;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    private User recipient;
    @Enumerated(EnumType.STRING)
    @NotNull
    private MessageStatus messageStatus = MessageStatus.CREATED;
    @Enumerated(EnumType.STRING)
    @NotNull
    private SMSStatus smsStatus = SMSStatus.NOT_SENT;
    @Enumerated(EnumType.STRING)
    @NotNull
    private EmailStatus emailStatus = EmailStatus.NOT_SENT;
}

package com.marotech.skillhub.model;

import com.marotech.skillhub.gson.GsonExcludeField;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@MappedSuperclass
@Transactional
public abstract class BaseEntity implements Persistable<String>, Serializable {
    @Column(length = 40)
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    protected String id;
    @GsonExcludeField
    @Column(nullable = false)
    protected Boolean online = true;
    @GsonExcludeField
    @Column(nullable = false)
    protected Boolean deleted = false;
    @GsonExcludeField
    @Column(nullable = false)
    protected LocalDateTime dateCreated = LocalDateTime.now();
    @GsonExcludeField
    @Column(nullable = false)
    protected LocalDateTime dateLastUpdated = LocalDateTime.now();

    public boolean isNew() {
        return this.id == null;
    }

    public String getFormattedDateCreated() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm");
        return fmt.format(dateCreated);
    }


    public String getFormattedDateCreated2() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:s");
        return fmt.format(dateCreated);
    }


    public String getFormattedDayCreated() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");
        return fmt.format(dateCreated);
    }


    public String getFormattedDateLastUpdated() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm");
        return fmt.format(dateLastUpdated);
    }


    public String getFormattedDay() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy");
        return fmt.format(dateCreated);
    }

    private static final long serialVersionUID = 3691539777459050302L;
}

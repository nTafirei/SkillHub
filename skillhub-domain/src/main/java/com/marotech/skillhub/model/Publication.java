package com.marotech.skillhub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "publication")
public class Publication extends BaseEntity {

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String summary;
    @Column(nullable = false)
    private String fileName;
    @Column(nullable = false)
    private LocalDate pubDate;
    @Enumerated(EnumType.STRING)
    @NotNull
    private PubType pubType = PubType.BOOK;
    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Category category;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Showcase showcase = Showcase.NO;
    @Enumerated(EnumType.STRING)
    @NotNull
    private ActiveStatus activeStatus = ActiveStatus.ACTIVE;
    @Column(nullable = false)
    private String source;
    @ToString.Exclude
    @OneToOne(fetch = FetchType.EAGER)
    private Attachment attachment;
    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private User worker;
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    private List<String> citations = new ArrayList<>();

    public Boolean getIsShowcased() {
        return showcase == Showcase.YES;
    }

    public String getFormattedPubDate() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy");
        return fmt.format(dateCreated);
    }
}

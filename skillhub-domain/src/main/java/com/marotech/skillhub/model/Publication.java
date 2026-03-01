package com.marotech.skillhub.model;

import com.marotech.skillhub.util.CommentComparator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
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
    @Enumerated(EnumType.STRING)
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
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "pub_workers", joinColumns = @JoinColumn(name = "pub_id"),
            inverseJoinColumns = @JoinColumn(name = "auth_id"))
    private List<Worker> workers = new ArrayList<>();
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    private List<String> citations = new ArrayList<>();
    @ToString.Exclude
    @OneToMany(fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    private Org org;

    public Boolean getIsShowcased() {
        return showcase == Showcase.YES;
    }

    public String getWorkerNames() {
        if (workers.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        int index = 0;
        for (Worker worker : workers) {
            builder.append(worker.getFullName());
            index = index + 1;
            if (index < (workers.size() - 1)) {
                builder.append(",");
            }
        }
        return builder.toString();
    }

    public List<Comment> getSortedComments() {
        if (comments.isEmpty()) {
            return comments;
        }
        Collections.sort(comments, new CommentComparator());
        return comments;
    }

    public String getFormattedPubDate() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy");
        return fmt.format(dateCreated);
    }
}

package com.marotech.skillhub.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name="attachment")
public class Attachment extends BaseEntity {
    @Column(nullable = false, length = 80)
    @NotNull
    private String name;
    @Column(nullable = false, length = 16)
    @NotNull
    private long size;
    @Lob
    @NotNull
    @Basic(fetch = FetchType.LAZY)
    private byte[] data;
    @Column(nullable = false, length = 72)
    @NotNull
    private String contentType;

    public Attachment getCopy() {
        Attachment copy = new Attachment();
        copy.setName(name);
        copy.setSize(size);
        copy.setData(data);
        copy.setContentType(contentType);
        copy.setDateCreated(dateCreated);
        copy.setDateLastUpdated(dateLastUpdated);
        return copy;
    }

    public String getPreview() {
        if (contentType.startsWith("text")) {
            int amount = Math.min(data.length, 30);
            return new String(data, 0, amount);
        } else {
            return "[Binary File]";
        }
    }
}

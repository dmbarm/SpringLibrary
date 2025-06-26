package org.springlibrary.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(length = 24, name = "image_file_id")
    private String bookCoverFileId;

    public Book(String title, String author, String description) {
        this.title = title;
        this.author = author;
        this.description = description;
    }

    public Book(String title, String author, String description, String bookCoverFileId) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.bookCoverFileId = bookCoverFileId;
    }

    @Override
    public String toString() {
        return id + ". '" + title + "' by " + author + ". " + description + ".";
    }

}

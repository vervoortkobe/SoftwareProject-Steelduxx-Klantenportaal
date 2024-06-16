package edu.ap.softwareproject.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * The metadata of an uploadable file.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private LocalDateTime created;
    private String ordersId;
    public Document(String name, LocalDateTime created, String ordersId) {
        this.name = name;
        this.created = created;
        this.ordersId = ordersId;
    }
}

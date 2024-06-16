package edu.ap.softwareproject;

import edu.ap.softwareproject.api.entity.Document;

import java.time.LocalDateTime;
import java.util.List;

public class DocumentsObjectMother {
    public static List<Document> createMockDocument() {
        return List.of(
                new Document("TestDocument", LocalDateTime.now(), "1")
        );
    }
}

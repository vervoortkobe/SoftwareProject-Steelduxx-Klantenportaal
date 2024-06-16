package edu.ap.softwareproject.api.repository;

import edu.ap.softwareproject.api.entity.Document;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DocumentRepository extends CrudRepository<Document, String> {
    List<Document> findAllByOrdersIdOrderByCreated(String orderId);
}

package edu.ap.softwareproject.api.service;

import edu.ap.softwareproject.api.dto.DocumentDTO;
import edu.ap.softwareproject.api.entity.Document;
import edu.ap.softwareproject.api.repository.DocumentRepository;
import edu.ap.softwareproject.api.util.SentryExceptionHelper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * An upload service to upload files.
 */
@Service
public class DocumentService {
  private final Path root = Paths.get("./uploads");
  private final DocumentRepository documentRepository;
  private final SentryExceptionHelper exceptionHelper;

  public DocumentService(DocumentRepository documentRepository, SentryExceptionHelper exceptionHelper) {
    this.init();
    this.documentRepository = documentRepository;
    this.exceptionHelper = exceptionHelper;
  }

  public void init() {
    try {
      if (!Files.isDirectory(root))
        Files.createDirectories(root);
    } catch (IOException e) {
      exceptionHelper.warn(e);
    }
  }

  /**
   * Upload document to the database
   * 
   * @param id   The orderId to upload the file to.
   * @param file Input file
   * @return Document that is saved to the database.
   */
  public Document uploadDocument(String id, MultipartFile file) {
    if (!Files.exists(this.root)) {
      try {
        Files.createDirectory(this.root);
      } catch (IOException e) {
        exceptionHelper.warn(e);
      }
    }

    String fileName = file.getOriginalFilename();
    if (fileName != null)
      fileName = StringUtils.cleanPath(fileName);
    Document document = new Document(fileName, LocalDateTime.now(), id);

    try {
      document = documentRepository.save(document);
      Files.copy(file.getInputStream(), this.root.resolve(document.getId()));
    } catch (Exception e) {
      exceptionHelper.warn(e);
      documentRepository.deleteById(document.getId());
    }

    return document;
  }

  /**
   * Removes a document from the database.
   * 
   * @param id The id of the document.
   */
  public boolean removeDocument(String id) {
    try {
      Path file = root.resolve(id);
      Resource resource = new UrlResource(file.toUri());

      if (resource.exists()) {
        resource.getFile().delete();
        documentRepository.deleteById(id);
        return true;
      } else
        return false;
    } catch (IOException e) {
      exceptionHelper.warn(e);
    }
    return false;
  }

  /**
   * Download a document by Id.
   * 
   * @param id Id of the document in the database.
   * @return A document object.
   */
  public Optional<Resource> downloadDocument(String id) {
    try {
      Path file = root.resolve(id);
      Resource resource = new UrlResource(file.toUri());

      if (resource.exists() || resource.isReadable()) {
        return Optional.of(resource);
      } else {
        return Optional.empty();
      }
    } catch (MalformedURLException e) {
      exceptionHelper.warn(e);
    }
    return Optional.empty();
  }

  public Optional<Document> getDocumentById(String id) {
    return documentRepository.findById(id);
  }

  /**
   * Gives back a DocumentDTO that contains all the documents for a specific
   * order.
   * 
   * @param id The orderId of an order.
   */
  public List<DocumentDTO> listFilesForOrder(String id) {
    return documentRepository
        .findAllByOrdersIdOrderByCreated(id)
        .stream()
        .map(document -> {
          try {
            Path file = root.resolve(document.getId());
            Resource resource = new UrlResource(file.toUri());
            return new DocumentDTO(document.getId(), document.getName(), document.getCreated(),
                resource.getFile().length());
          } catch (Exception e) {
            exceptionHelper.warn(e);
          }
          return new DocumentDTO(document.getId(), document.getName(), document.getCreated(), 0);
        })
        .toList();
  }
}

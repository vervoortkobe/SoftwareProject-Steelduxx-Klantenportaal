package edu.ap.softwareproject.api.service;

import edu.ap.softwareproject.DocumentsObjectMother;
import edu.ap.softwareproject.api.dto.DocumentDTO;
import edu.ap.softwareproject.api.entity.Document;
import edu.ap.softwareproject.api.repository.DocumentRepository;
import edu.ap.softwareproject.api.util.SentryExceptionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DocumentServiceTest {
  @Mock
  private DocumentRepository documentRepository;

  private DocumentService documentService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    documentService = new DocumentService(documentRepository, new SentryExceptionHelper());
  }

  @Test
  void givenOrderEntity_shouldBeDTO() {
    List<Document> documents = DocumentsObjectMother
        .createMockDocument();
    when(documentRepository.findAllByOrdersIdOrderByCreated("1"))
        .thenReturn(documents);

    // Verify that the first document is a valid DTO.
    Document document = documents.get(0);

    DocumentDTO documentDTO = documentService.listFilesForOrder("1").get(0);

    assertThat(document.getName()).isEqualTo(documentDTO.name());
  }

  @Test
  void givenDocument_shouldExtractNameCorrectly() {

    Document mockDocument = new Document();
    mockDocument.setId(UUID.randomUUID().toString());

    Mockito.when(documentRepository.save(any())).thenReturn(mockDocument);

    // Create test file.
    MockMultipartFile file = new MockMultipartFile("test.json", "test.json", "application/json",
        "{\"key1\": \"value1\"}".getBytes());

    documentService.uploadDocument("1", file);

      ArgumentCaptor<Document> argumentCaptor = ArgumentCaptor.forClass(Document.class);
    verify(documentRepository).save(argumentCaptor.capture());

    Document capturedDocument = argumentCaptor.getValue();

    assertThat(capturedDocument.getName()).isEqualTo("test.json");
  }
}

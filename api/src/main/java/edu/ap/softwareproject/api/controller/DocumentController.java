package edu.ap.softwareproject.api.controller;

import edu.ap.softwareproject.api.dto.DocumentDTO;
import edu.ap.softwareproject.api.entity.Document;
import edu.ap.softwareproject.api.interfaces.ExceptionHelper;
import edu.ap.softwareproject.api.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/document")
public class DocumentController {
  // Will not be saved on API due to time limits + this code would need a giant
  // refactor.
  // Only locally.
  private final DocumentService documentService;
  private final ExceptionHelper exceptionHelper;

  public DocumentController(DocumentService documentService, ExceptionHelper exceptionHelper) {
    this.documentService = documentService;
    this.exceptionHelper = exceptionHelper;
  }

  @Operation(summary = "Uploads a file for an order request.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "The file has been uploaded."),
  })
  @PostMapping("/{id}")
  public ResponseEntity<Void> upload(@PathVariable("id") String id,
      @RequestParam("file") MultipartFile file) {
    documentService.uploadDocument(id, file);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @Operation(summary = "Removes a file.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "The file has been removed."),
      @ApiResponse(responseCode = "404", description = "The file does not exist."),
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> remove(@PathVariable("id") String id) {
    Boolean success = documentService.removeDocument(id);
    return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
  }

  @Operation(summary = "Downloads a file.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "The file data."),
      @ApiResponse(responseCode = "404", description = "The file does not exist.", content = @Content(schema = @Schema(implementation = Void.class))),
      @ApiResponse(responseCode = "500", description = "Something went wrong with downloading the file.", content = @Content(schema = @Schema(implementation = Void.class))),
  })
  @GetMapping("/{id}")
  public ResponseEntity<byte[]> download(@PathVariable("id") String id) {
    Optional<Resource> document = documentService.downloadDocument(id);
    Optional<Document> documentInfo = documentService.getDocumentById(id);

    if (document.isPresent() && documentInfo.isPresent()) {
      try {
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + documentInfo.get().getName() + "\"")
            .body(document.get().getContentAsByteArray());
      } catch (IOException e) {
        exceptionHelper.warn(e);
      }
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.internalServerError().build();
  }

  @Operation(summary = "Lists all files uploaded to an order.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "A list of documents."),
  })
  @GetMapping("/listfiles/{orderId}")
  public ResponseEntity<List<DocumentDTO>> listFilesForOrder(@PathVariable("orderId") String orderId) {
    return ResponseEntity.status(HttpStatus.OK).body(documentService.listFilesForOrder(orderId));
  }
}

import { AfterViewInit, Component, Input } from '@angular/core';
import { DocumentService } from '../services/document.service';
import { Document } from '../interfaces/document';
import { Modal, ModalOptions } from 'flowbite';

@Component({
  selector: 'app-documents',
  templateUrl: './documents.component.html',
  styleUrls: ['./documents.component.css'],
})
export class DocumentsComponent implements AfterViewInit {
  @Input({ required: true }) orderId!: string;
  public Documents: Document[] = [];
  private documentService: DocumentService;
  public modal: Modal | any = null;
  private selectedToDelete = '';

  ngAfterViewInit(): void {
    // set the modal menu element
    const $targetEl = document.getElementById('popup-modal');
    // options with default values
    const options: ModalOptions = {
      placement: 'bottom-right',
      backdrop: 'dynamic',
      backdropClasses: 'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
      closable: true,
    };

    // instance options object
    const instanceOptions = {
      id: 'popup-modal',
      override: true,
    };

    this.modal = new Modal($targetEl, options, instanceOptions);
    this.loadDocuments();
  }

  constructor(DocumentService: DocumentService) {
    this.documentService = DocumentService;
  }

  public loadDocuments() {
    this.documentService
      .listFiles(this.orderId)
      .subscribe((data: Document[]) => {
        data.map((d) => {
          d.downloadlink = this.documentService.getDownloadLink(d.id);
        });
        this.Documents = data;
      });
  }

  public downloadFile(fileUrl: string, fileName: string) {
    const link = document.createElement('a');
    link.href = fileUrl;
    link.download = fileName;
    link.click();
  }

  public deleteDocument(documentId: string) {
    this.selectedToDelete = documentId;
    this.modal.show();
  }

  public deleteDocumentFinal() {
    this.modal.hide();
    this.documentService
      .removeDocument(this.selectedToDelete)
      .subscribe((data) => {
        this.loadDocuments();
      });
  }
}

import {
  Component,
  ElementRef,
  EventEmitter,
  Input,
  Output,
} from '@angular/core';
import { DocumentService } from '../services/document.service';
import { HttpEventType } from '@angular/common/http';
import { ViewChild } from '@angular/core';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css'],
})
export class UploadComponent {
  @Input({ required: true }) orderId!: string;

  @Output() reloadRequired = new EventEmitter<string>();

  @ViewChild('fileInput')
  private myInputVariable: ElementRef | any;

  private currentFileUpload: File | any = null;
  public progress = 0;
  public message = '';
  public success = false;

  constructor(private documentService: DocumentService) {}

  closeAlert() {
    this.progress = 0;
    this.message = '';
  }

  upload(event: any) {
    this.progress = 0;
    this.currentFileUpload = event.target.files.item(0);
    this.documentService
      .uploadDocument(this.orderId, this.currentFileUpload)
      .subscribe({
        next: (event) => {
          if (event.type === HttpEventType.UploadProgress) {
            this.progress = Math.round((100 * event.loaded) / event.total);
          } else if (event.type === 4) {
            this.reloadRequired.emit();
            this.progress = 0;
          }
        },
        complete: () => {
          this.myInputVariable.nativeElement.value = '';
        },
        error: (err) => {
          console.log(err);
          this.success = false;
          this.progress = 0;
          this.message = 'Could not upload the file!';
          this.currentFileUpload = null;
        },
      });
  }
}

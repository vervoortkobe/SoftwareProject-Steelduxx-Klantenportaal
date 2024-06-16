import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpRequest } from '@angular/common/http';
import { Document } from '../interfaces/document';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class DocumentService {
  readonly endpoint: string = environment.API_URL + '/document';

  readonly listfiles: string = '/listfiles';
  constructor(private http: HttpClient) {}

  public uploadDocument(orderId: string, File: File): Observable<any> {
    const formData = new FormData();

    formData.append('file', File);

    const req = new HttpRequest(
      'POST',
      this.endpoint + '/' + orderId,
      formData,
      {
        reportProgress: true,
        responseType: 'json',
      }
    );

    return this.http.request(req);
  }

  public listFiles(orderId: string): Observable<Document[]> {
    return this.http.get<Document[]>(
      this.endpoint + this.listfiles + '/' + orderId
    );
  }

  public removeDocument(documentId: String): Observable<any> {
    return this.http.delete(this.endpoint + '/' + documentId);
  }

  public getDownloadLink(documentId: String): string {
    return this.endpoint + '/' + documentId;
  }
}

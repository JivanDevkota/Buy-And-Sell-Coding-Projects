import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { LanguageDTO, CategoryDTO, PublicProjectListItem, PublicProjectDetails } from "../model";
import { environment } from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class PublicService {

  private readonly publicUrl = environment.apiUrl;
  constructor(private http: HttpClient) {}

  getAllLanguages(): Observable<LanguageDTO[]> {
    return this.http.get<LanguageDTO[]>(this.publicUrl + "/languages");
  }

  getAllCategory(): Observable<CategoryDTO[]> {
    return this.http.get<CategoryDTO[]>(this.publicUrl + "/categories");
  }

  getAllProjects(): Observable<PublicProjectListItem[]> {
    return this.http.get<PublicProjectListItem[]>(this.publicUrl + "/projects");
  }

  getProjectDetailsById(projectId: number): Observable<PublicProjectDetails> {
    return this.http.get<PublicProjectDetails>(this.publicUrl + "/projects/" + projectId);
  }
}

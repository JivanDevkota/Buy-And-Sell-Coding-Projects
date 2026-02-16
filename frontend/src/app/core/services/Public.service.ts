import {HttpClient} from "@angular/common/http";
import { Injectable } from "@angular/core";
import {Observable} from "rxjs";
import {LanguageResponse} from "../model/languageResponse";
import {CategoryDTO} from "../model/CategoryDTO";
import {PublicProjectResponse} from "../model/PublicProjectResponse";
import {PublicProjectDetailsResponse} from "../model/PublicProjectDetailsReponse";


@Injectable({
  providedIn: 'root'
})
export  class PublicService {

  private readonly publicUrl="http://localhost:8080/api";
  constructor(private http:HttpClient) {}

  getAllLanguages(): Observable<LanguageResponse[]>{
    return  this.http.get<LanguageResponse[]>(this.publicUrl+"/languages");
  }
  getAllCategory():Observable<CategoryDTO[]>{
    return  this.http.get<CategoryDTO[]>(this.publicUrl+"/categories");
  }

  getAllProjects():Observable<PublicProjectResponse[]>{
    return  this.http.get<PublicProjectResponse[]>(this.publicUrl+"/projects");
  }

  getProjectDetailsById(projectId:number):Observable<PublicProjectDetailsResponse>{
    return  this.http.get<PublicProjectDetailsResponse>(this.publicUrl+"/projects/" + projectId);
  }
}

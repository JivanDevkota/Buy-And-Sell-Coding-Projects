import {HttpClient} from "@angular/common/http";
import { Injectable } from "@angular/core";
import {Observable} from "rxjs";
import {LanguageResponse} from "../model/languageResponse";


@Injectable({
  providedIn: 'root'
})
export  class PublicService {

  private readonly publicUrl="http://localhost:8080/api";
  constructor(private http:HttpClient) {}

  getAllLanguages(): Observable<LanguageResponse[]>{
    return  this.http.get<LanguageResponse[]>(this.publicUrl+"/languages");
  }

}

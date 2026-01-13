import { Component } from '@angular/core';
import {LanguageResponse} from "../../core/model/languageResponse";
import {PublicService} from "../../core/services/PublicService";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  languages:LanguageResponse[]=[]

  constructor(private publicService:PublicService) {}

  ngOnInit() {
    this.getAllProgrammingLanguages()
  }

  getAllProgrammingLanguages():void{
    this.publicService.getAllLanguages().subscribe(({
    next: (data) => {
      this.languages = data;
    },
      error: (error) => {
      console.log("error", error);
      },
    }))
  }

}

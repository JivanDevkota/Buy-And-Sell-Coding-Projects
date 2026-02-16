import { Component } from '@angular/core';
import {LanguageResponse} from "../../core/model/languageResponse";
import {PublicService} from "../../core/services/Public.service";
import {PublicProjectResponse} from "../../core/model/PublicProjectResponse";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  languages:LanguageResponse[]=[]
  projects:PublicProjectResponse[]=[];

  constructor(private publicService:PublicService) {}

  ngOnInit() {
    this.getAllProgrammingLanguages()
    this.getAllProjects();
  }

  getAllProgrammingLanguages():void{
    this.publicService.getAllLanguages().subscribe(({
    next: (data) => {
      this.languages = data;
      console.log(this.languages);
    },
      error: (error) => {
      console.log("error", error);
      },
    }))
  }

  getAllProjects(): void {
    this.publicService.getAllProjects().subscribe({
      next: (data) => {
        console.log('PROJECT API RESPONSE:', data);
        this.projects = data;
      },
      error: (error) => {
        console.log("error", error);
      }
    });
  }

}

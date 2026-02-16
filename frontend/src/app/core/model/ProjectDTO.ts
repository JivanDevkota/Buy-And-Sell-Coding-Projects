import {Language} from "./LanguageDTO";

export interface ProjectDTO {
  id?: number;
  title: string,
  description: string,
  projectImgUrl?: string,
  tags: string[]
  price: number
  languageIds:number[]
  categoryId: number
  userId: number |null
}

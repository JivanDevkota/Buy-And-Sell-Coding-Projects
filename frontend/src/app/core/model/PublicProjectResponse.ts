import {ProjectStatus} from "./ProjectResponse";

export interface PublicProjectResponse{
  id: number
  title: string
  description: string
  projectImgUrl: string
  price: number
  languagesName: string[]
  categoryName:string
}

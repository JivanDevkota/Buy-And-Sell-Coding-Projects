export interface ProjectResponse {
  id: number
  title: string
  description: string
  projectImgUrl: string
  tags:string[]
  price: number
  languagesName: string[]
  status:ProjectStatus
  categoryName:string
}

export enum ProjectStatus {
  DRAFT = 'DRAFT',
  UNDER_REVIEW = 'UNDER_REVIEW',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
  SUSPENDED = 'SUSPENDED',
  ARCHIVED = 'ARCHIVED'
}

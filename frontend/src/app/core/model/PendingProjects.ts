
export interface PendingProjects {
  projectId:number
  projectName:string
  sellerName:string
  categoryName:string
  submittedAt:string
  price:number
}

export interface PaginatedPendingProjectsResponse {
  content: PendingProjects[];
  totalElements:number
  totalPages:number
  size:number
  page:number
}

export interface CategoryDTO {
  id: number;
  name: string;
  description?: string;
  iconUrl?: string;
}

export interface CreateCategoryRequest {
  name: string;
  description?: string;
  iconFile?: File;
}

export interface LanguageDTO {
  id: number;
  name: string;
  description?: string;
  iconUrl?: string;
}

export interface CreateLanguageRequest {
  name: string;
  description?: string;
  iconFile?: File;
}

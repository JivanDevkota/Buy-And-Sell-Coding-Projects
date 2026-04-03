import { ProjectStatus, FileType } from './enums';

export interface BaseProject {
  id: number;
  title: string;
  description: string;
  projectImgUrl: string;
  price: number;
  languagesName: string[];
  categoryName: string;
}

export interface ProjectListItem extends BaseProject {
  tags: string[];
  status: ProjectStatus;
}

export interface ProjectDetails extends ProjectListItem {
  files: ProjectFileResponse[];
}

export interface PublicProjectListItem extends BaseProject {
  status: ProjectStatus;
}

export interface PublicProjectDetails extends PublicProjectListItem {
  tags: string[];
  files: ProjectFileResponse[];
}

export interface ProjectFileResponse {
  id: number;
  fileType: FileType;
  description?: string;
  displayOrder: number;
  fileName: string;
  fileUrl: string;
}

export interface ProjectFileDTO {
  id?: number;
  projectId?: number;
  fileType: FileType;
  description?: string;
  displayOrder?: number;
  fileName?: string;
  fileUrl?: string;
}

export interface ProjectDTO {
  id?: number;
  title: string;
  description: string;
  projectImgUrl?: string;
  tags: string[];
  price: number;
  languageIds: number[];
  categoryId: number;
  userId: number | null;
}

// ============================================================
// Enums - defined ONCE here, imported everywhere else
// ============================================================

export enum ProjectStatus {
  DRAFT = 'DRAFT',
  UNDER_REVIEW = 'UNDER_REVIEW',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
  SUSPENDED = 'SUSPENDED',
  ARCHIVED = 'ARCHIVED'
}

export enum FileType {
  SOURCE_CODE = 'SOURCE_CODE',
  EXECUTABLE = 'EXECUTABLE',
  DOCUMENTATION = 'DOCUMENTATION',
  DATABASE = 'DATABASE',
  CONFIGURATION = 'CONFIGURATION',
  MEDIA = 'MEDIA',
  OTHER = 'OTHER'
}

// ============================================================
// Interfaces
// ============================================================

export interface ProjectFileResponse {
  id: number;
  fileType: FileType;
  description?: string;
  displayOrder: number;
  fileName: string;
  fileUrl: string;
}

export interface ProjectDetailsResponse {
  id: number;
  title: string;
  description: string;
  projectImgUrl: string;
  tags: string[];
  price: number;
  languagesName: string[];
  status: ProjectStatus;
  categoryName: string;
  files: ProjectFileResponse[];
}

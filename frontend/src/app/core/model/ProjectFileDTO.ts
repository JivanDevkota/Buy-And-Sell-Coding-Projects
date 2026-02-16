export interface ProjectFileDTO {
  id?: number;
  projectId: number;
  fileType: FileType;
  description?: string;
  displayOrder?: number;
  fileName?: string;
  fileUrl?: string;
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


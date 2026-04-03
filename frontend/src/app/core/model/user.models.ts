import { Role, UserStatus } from './enums';

export interface User {
  id: number;
  username: string;
  email: string;
  fullName?: string;
  bio?: string;
  profileImgUrl?: string;
  roles: Role[];
  status: UserStatus;
  balance?: number;
  createdAt?: string;
  updatedAt?: string;
}

export interface UserDetailsResponse {
  id: number;
  username: string;
  email: string;
  role: string[];
  joined: string;
}

export interface LoginRequest {
  emailOrUsername: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  fullName?: string;
  role?: Role;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
  user: UserDetailsResponse;
}

export interface RefreshTokenRequest {
  refreshToken: string;
}

export interface UpdateUserStatusRequest {
  userId: number;
  status: UserStatus;
}

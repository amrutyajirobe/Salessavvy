export type UserRole = "CUSTOMER" | "ADMIN";

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  userId: number;
  username: string;
  role: UserRole;
  message: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}

export interface RegisteredUser {
  userId: number;
  username: string;
  email: string;
  role: UserRole;
}

export interface RegisterResponse {
  message: string;
  user: RegisteredUser;
}

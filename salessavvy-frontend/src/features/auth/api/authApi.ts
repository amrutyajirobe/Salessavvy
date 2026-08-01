import { endpoints } from "../../../config/endpoints";
import { apiClient } from "../../../lib/api/apiClient";
import type {
  LoginRequest,
  LoginResponse,
  RegisterRequest,
  RegisterResponse
} from "../types/auth.types";

export async function login(payload: LoginRequest): Promise<LoginResponse> {
  const response = await apiClient.post<LoginResponse>(endpoints.auth.login, payload);
  return response.data;
}

export async function register(payload: RegisterRequest): Promise<RegisterResponse> {
  const response = await apiClient.post<RegisterResponse>(endpoints.auth.register, payload);
  return response.data;
}

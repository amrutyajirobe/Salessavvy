import axios from "axios";

type ErrorBody = {
  error?: string;
  message?: string;
  errors?: Record<string, string>;
};

export function getApiErrorMessage(error: unknown): string {
  if (!axios.isAxiosError<ErrorBody>(error)) {
    return "Something went wrong. Please try again.";
  }

  const body = error.response?.data;
  if (body?.error) return body.error;
  if (body?.message) return body.message;
  if (body?.errors) return Object.values(body.errors)[0] ?? "Validation failed.";

  return error.message || "Unable to reach the server.";
}

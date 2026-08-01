import { useState, type FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import { getApiErrorMessage } from "../../../lib/api/getApiErrorMessage";
import { login } from "../api/authApi";

export function LoginForm() {
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [submitting, setSubmitting] = useState(false);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setError("");
    setSubmitting(true);

    try {
      const user = await login({ username: username.trim(), password });
      sessionStorage.setItem("salessavvy.user", JSON.stringify(user));
      navigate(user.role === "ADMIN" ? "/admin/products" : "/products", { replace: true });
    } catch (requestError) {
      setError(getApiErrorMessage(requestError));
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <form className="auth-form" onSubmit={handleSubmit}>
      <label htmlFor="username">Username</label>
      <input
        id="username"
        name="username"
        value={username}
        onChange={(event) => setUsername(event.target.value)}
        placeholder="Enter your username"
        autoComplete="username"
        required
      />

      <label htmlFor="password">Password</label>
      <input
        id="password"
        name="password"
        type="password"
        value={password}
        onChange={(event) => setPassword(event.target.value)}
        placeholder="Enter your password"
        autoComplete="current-password"
        required
      />

      {error && <p className="form-message form-error" role="alert">{error}</p>}

      <button type="submit" disabled={submitting}>
        {submitting ? "Signing in…" : "Sign In"}
      </button>
    </form>
  );
}

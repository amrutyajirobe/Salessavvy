import { useState, type FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import { getApiErrorMessage } from "../../../lib/api/getApiErrorMessage";
import { register } from "../api/authApi";

export function RegisterForm() {
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [submitting, setSubmitting] = useState(false);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setError("");
    setSubmitting(true);

    try {
      await register({ username: username.trim(), email: email.trim(), password });
      navigate("/login", { replace: true });
    } catch (requestError) {
      setError(getApiErrorMessage(requestError));
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <form className="auth-form" onSubmit={handleSubmit}>
      <label htmlFor="username">Username</label>
      <input id="username" value={username} onChange={(event) => setUsername(event.target.value)}
        minLength={3} maxLength={255} autoComplete="username" required />

      <label htmlFor="email">Email</label>
      <input id="email" type="email" value={email} onChange={(event) => setEmail(event.target.value)}
        autoComplete="email" required />

      <label htmlFor="password">Password</label>
      <input id="password" type="password" value={password} onChange={(event) => setPassword(event.target.value)}
        minLength={8} autoComplete="new-password" required />

      {error && <p className="form-message form-error" role="alert">{error}</p>}

      <button type="submit" disabled={submitting}>
        {submitting ? "Creating account…" : "Sign Up"}
      </button>
    </form>
  );
}

import { Link } from "react-router-dom";
import { AuthCard } from "../components/AuthCard";
import { LoginForm } from "../components/LoginForm";

export function LoginPage() {
  return (
    <AuthCard title="Login" footer={<>New user? <Link to="/register">Sign up here</Link></>}>
      <LoginForm />
    </AuthCard>
  );
}

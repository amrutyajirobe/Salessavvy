import { Link } from "react-router-dom";
import { AuthCard } from "../components/AuthCard";
import { RegisterForm } from "../components/RegisterForm";

export function RegisterPage() {
  return (
    <AuthCard title="Create Account" footer={<>Already registered? <Link to="/login">Sign in</Link></>}>
      <RegisterForm />
    </AuthCard>
  );
}

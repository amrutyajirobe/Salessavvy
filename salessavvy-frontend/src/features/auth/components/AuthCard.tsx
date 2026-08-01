import type { PropsWithChildren, ReactNode } from "react";

interface AuthCardProps extends PropsWithChildren {
  title: string;
  footer: ReactNode;
}

export function AuthCard({ title, children, footer }: AuthCardProps) {
  return (
    <main className="auth-page">
      <section className="auth-card" aria-labelledby="auth-title">
        <h1 id="auth-title">{title}</h1>
        {children}
        <div className="auth-footer">{footer}</div>
      </section>
    </main>
  );
}

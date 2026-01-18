export interface User {
  email: string;
  name: string;
  role: 'admin' | 'client';
}

export interface LoginRequest {
  email: string;
  password: string;
}
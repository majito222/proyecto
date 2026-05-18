// Espejo de: record LoginRequest(String email, String password)
export interface LoginRequest {
  email: string;
  password: string;
}

// Espejo de: record TokenResponse(String token, String type, String expireAt, Collection<String> roles)
export interface TokenResponse {
  token: string;
  type: string;
  expireAt: string;
  roles: string[];
}
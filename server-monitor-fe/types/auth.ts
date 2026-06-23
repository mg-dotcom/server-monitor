export interface AuthenticatedUser {
  role: "ADMIN" | "OPERATOR";
  username: string;
}
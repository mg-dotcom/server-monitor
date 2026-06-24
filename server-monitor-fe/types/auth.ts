export interface AuthenticatedUser {
  username: string;
  name: string;
  role: "ADMIN" | "OPERATOR";
}
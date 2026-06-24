export interface Log {
  id: number;
  status: "UP" | "DOWN" | "UNKNOWN" | "CHECKING";
  detail: string;
  createdAt: string;
}
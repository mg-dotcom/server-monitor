export type ServerStatus = "UP" | "DOWN" | "UNKNOWN" | "CHECKING";

export interface Operator {
  id: string;
  name: string;
  lineUserId: string;
  role: "ADMIN" | "OPERATOR";
}

export interface Server {
  id: string;
  name: string;
  endpoint: string;
  isMonitored: boolean;
  currentStatus: ServerStatus;
  operators: Operator[];
}
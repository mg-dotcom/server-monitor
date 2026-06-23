import { cookies } from "next/headers";
import { Server, Operator } from "@/types/server";
import { ApiResponse } from "@/types/api";
import { AuthenticatedUser } from "@/types/auth";
import { Log } from "@/types/log";
import { api } from "@/lib/api";

async function authHeader() {
  const cookieStore = await cookies();
  const token = cookieStore.get("token")?.value;
  return token ? { Cookie: `token=${token}` } : {}; 
}

export async function getServers(): Promise<Server[]> {
  const { data: response } = await api.get<ApiResponse<Server[]>>("/servers", {
    headers: await authHeader(),
  });
  return response.data;
}

export async function getServerById(id: string): Promise<Server> {
  const { data: response } = await api.get<ApiResponse<Server>>(`/servers/${id}`, {
    headers: await authHeader(),
  });
  return response.data;
}

export async function getServerLogs(id: string): Promise<Log[]> {
  try {
    const { data: response } = await api.get<ApiResponse<Log[]>>(`/servers/${id}/logs`, {
      headers: await authHeader(),
    });
    return response.data;
  } catch (error: any) {
    console.log("ERROR BODY:", error.response?.data || error.message);
    throw new Error("Failed to fetch logs");
  }
}

export async function getOperators(): Promise<Operator[]> {
  const { data: response } = await api.get<ApiResponse<Operator[]>>("/operators", {
    headers: await authHeader(),
  });
  return response.data;
}

export async function getMe(): Promise<AuthenticatedUser> {
  const { data: response } = await api.get<ApiResponse<AuthenticatedUser>>("/auth/me", {
    headers: await authHeader(),
  });
  return response.data;
}
import { cookies } from "next/headers";
import { Server, Operator } from "@/types/server";
import { ApiResponse } from "@/types/api";
import { AuthenticatedUser } from "@/types/auth";
import { Log } from "@/types/log";


async function authHeader() {
  const cookieStore = await cookies();
  const token = cookieStore.get("token")?.value;
  return { Cookie: `token=${token}` }; 
}

export async function getServers(): Promise<Server[]> {
  const res = await fetch("http://localhost:8080/api/servers", {
    headers: await authHeader(),
    cache: "no-store",
  });

  if (!res.ok) {
    throw new Error("Failed to fetch servers");
  }

  const data: ApiResponse<Server[]> = await res.json();
  return data.data;
}


export async function getServerById(id: string): Promise<Server> {
  const res = await fetch(`http://localhost:8080/api/servers/${id}`, {
    headers: await authHeader(),
    cache: "no-store",
  });
  if (!res.ok) {
    throw new Error("Failed to fetch server");
  }
  const data: ApiResponse<Server> = await res.json();
  return data.data;
}

export async function getServerLogs(id: string): Promise<Log[]> {

  const res = await fetch(
    `http://localhost:8080/api/servers/${id}/logs`,
    {
      headers: await authHeader(),
      cache: "no-store",
    }
  );

  if (!res.ok) {
    const text = await res.text();
    console.log("ERROR BODY:", text);
    throw new Error("Failed to fetch logs");
  }

  const data: ApiResponse<Log[]>   = await res.json();
  return data.data;
}

export async function getOperators(): Promise<Operator[]> {
  const res = await fetch("http://localhost:8080/api/operators", {
    headers: await authHeader(),
    cache: "no-store",
  });

  if (!res.ok) throw new Error("Failed to fetch operators");

  const data: ApiResponse<Operator[]> = await res.json();
  return data.data;
}

export async function getMe(): Promise<AuthenticatedUser> {
  const res = await fetch("http://localhost:8080/api/auth/me", {
    headers: await authHeader(),
    cache: "no-store",
  });
  
  if (!res.ok) throw new Error("Failed to fetch authenticated user");

  const data: ApiResponse<AuthenticatedUser> = await res.json();
  return data.data;
}

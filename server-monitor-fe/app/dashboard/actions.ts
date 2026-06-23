"use server";

import { cookies } from "next/headers";
import { revalidatePath } from "next/cache";
import { ApiResponse } from "@/types/api";
import { api } from "@/lib/api";

type AddServerInput = {
  name: string;
  endpoint: string;
};

async function authHeader() {
  const cookieStore = await cookies();
  const token = cookieStore.get("token")?.value;
  return token ? { Cookie: `token=${token}` } : {}; 
}

export async function loginAction(credentials: any) {
  try {
    const { data: response } = await api.post<ApiResponse<{ token: string }>>("/auth/login", credentials);

    const token = response.data.token; 

    const cookieStore = await cookies();
    cookieStore.set("token", token, {
      httpOnly: true, 
      secure: true,
      sameSite: "lax",
      path: "/",
      maxAge: 60 * 60 * 24, 
    });

    return { success: true };
  } catch (error: any) {
    return { 
      success: false, 
      message: error.response?.data?.message || "Login failed" 
    };
  }
}

export async function logoutAction() {
  const cookieStore = await cookies();
  cookieStore.delete("token"); 
  return { success: true };
}

export async function addServer({ name, endpoint }: AddServerInput) {
  await api.post("/servers", { name, endpoint }, {
    headers: await authHeader(),
  });

  revalidatePath("/dashboard");
}

export async function removeServer(id: string) {
  await api.delete(`/servers/${id}`, {
    headers: await authHeader(),
  });

  revalidatePath("/dashboard");
}

export async function assignOperator(serverId: string, operatorId: string) {
  await api.post(`/servers/${serverId}/assign-operator/${operatorId}`, {}, {
    headers: await authHeader(),
  });

  revalidatePath(`/dashboard/servers/${serverId}`);
}

export async function removeOperator(serverId: string, operatorId: string) {
  await api.delete(`/servers/${serverId}/remove-operator/${operatorId}`, {
    headers: await authHeader(),
  });

  revalidatePath(`/dashboard/servers/${serverId}`);
}
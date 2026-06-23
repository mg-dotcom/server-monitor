"use server";

import { cookies } from "next/headers";
import { revalidatePath } from "next/cache";
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
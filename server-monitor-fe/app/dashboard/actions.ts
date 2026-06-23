"use server";

import { cookies } from "next/headers";
import { revalidatePath } from "next/cache";

type AddServerInput = {
  name: string;
  endpoint: string;
};

async function authHeader() {
  const cookieStore = await cookies();
  const token = cookieStore.get("token")?.value;
  return { Cookie: `token=${token}` };
}

export async function addServer({ name, endpoint }: AddServerInput) {
  const res = await fetch("http://localhost:8080/api/servers", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      ...(await authHeader()),
    },
    body: JSON.stringify({ name, endpoint }),
  });

  if (!res.ok) {
    throw new Error("Failed to add server");
  }

  revalidatePath("/dashboard");
}

export async function removeServer(id: string) {
  const res = await fetch(`http://localhost:8080/api/servers/${id}`, {
    method: "DELETE",
    headers: await authHeader(),
  });

  if (!res.ok) {
    throw new Error("Failed to remove server");
  }

  revalidatePath("/dashboard");
}

export async function assignOperator(serverId: string, operatorId: string) {
  const res = await fetch(
    `http://localhost:8080/api/servers/${serverId}/assign-operator/${operatorId}`,
    {
      method: "POST",
      headers: await authHeader(),
    }
  );

  if (!res.ok) throw new Error("Failed to assign operator");
  revalidatePath(`/dashboard/servers/${serverId}`);
}

export async function removeOperator(serverId: string, operatorId: string) {
  const res = await fetch(
    `http://localhost:8080/api/servers/${serverId}/remove-operator/${operatorId}`,
    {
      method: "DELETE",
      headers: await authHeader(),
    }
  );

  if (!res.ok) throw new Error("Failed to remove operator");
  revalidatePath(`/dashboard/servers/${serverId}`);
}

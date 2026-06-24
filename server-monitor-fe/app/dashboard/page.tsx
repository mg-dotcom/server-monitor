export const dynamic = "force-dynamic";

import DashboardContent from "@/components/dashboard/DashboardContent";
import { getServers } from "@/lib/server-api";
import PageWrapper from "@/components/shared/PageWrapper";

export default async function DashboardPage() {
  const servers = await getServers();

  return (
    <PageWrapper title="Server Dashboard">
      <DashboardContent servers={servers} />
    </PageWrapper>
  );
}

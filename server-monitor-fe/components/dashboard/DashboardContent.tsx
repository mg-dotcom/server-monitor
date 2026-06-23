import { Server } from "@/types/server";
import SummaryCards from "./SummaryCards";
import ServerList from "./ServerList";
import StatusPoller from "./StatusPoller";
import AnimatedContainer from "@/components/shared/AnimatedContainer";
import { getMe } from "@/lib/server-api";

type Props = {
  servers: Server[];
};

export default async function DashboardContent({ servers }: Props) {
  const me = await getMe();

  return (
    <div className="max-w-6xl mx-auto space-y-8 px-6">
      <StatusPoller intervalMs={10000} />

      <AnimatedContainer delay={0}>
        <SummaryCards servers={servers} />
      </AnimatedContainer>

      <AnimatedContainer delay={1}>
        <ServerList servers={servers} role={me.role} />
      </AnimatedContainer>
    </div>
  );
}
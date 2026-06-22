import { Server } from "@/types/server";
import SummaryCards from "./SummaryCards";
import ServerList from "./ServerList";
import StatusPoller from "./StatusPoller";
import AnimatedContainer from "@/components/shared/AnimatedContainer";

type Props = {
  servers: Server[];
};

export default function DashboardContent({ servers }: Props) {
  return (
    <div className="max-w-6xl mx-auto space-y-8 px-6">
      <StatusPoller intervalMs={10000} />

      <AnimatedContainer delay={0}>
        <SummaryCards servers={servers} />
      </AnimatedContainer>

      <AnimatedContainer delay={1}>
        <ServerList servers={servers} />
      </AnimatedContainer>
    </div>
  );
}
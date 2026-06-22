import { Server } from "@/types/server";
import SummaryCard from "./SummaryCard";

type Props = {
  servers: Server[];
};

export default function SummaryCards({ servers }: Props) {
  const total = servers.length;

  const up = servers.filter(
    (server) => server.currentStatus === "UP"
  ).length;

  const down = servers.filter(
    (server) => server.currentStatus === "DOWN"
  ).length;

  const stats: { title: string; value: number; color: "blue" | "green" | "red" }[] = [
    { title: "Total Servers", value: total, color: "blue" },
    { title: "UP", value: up, color: "green" },
    { title: "DOWN", value: down, color: "red" },
  ];

  return (
    <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
      {stats.map((stat, idx) => (
        <div
          key={stat.title}
          className="animate-scale-in"
          style={{ animationDelay: `${idx * 0.1}s` }}
        >
          <SummaryCard 
            title={stat.title} 
            value={stat.value} 
            color={stat.color}
          />
        </div>
      ))}
    </div>
  );
}
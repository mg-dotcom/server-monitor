import { getMe, getOperators, getServerById, getServerLogs } from "@/lib/server-api";
import PageWrapper from "@/components/shared/PageWrapper";
import OperatorsSection from "@/components/dashboard/OperatorsSection";

type Props = {
  params: Promise<{
    id: string;
  }>;
};

export default async function ServerDetailPage({
  params,
}: Props) {
  const { id } = await params;

  const [server, logs, operators, me] = await Promise.all([
    getServerById(id),
    getServerLogs(id),
    getOperators(),
    getMe()
  ]);

  console.log("server.operators:", server.operators);


  return (
    <PageWrapper title="Server Logs">
      <div className="max-w-6xl mx-auto space-y-6 px-6">
        <OperatorsSection serverId={server.id} assignedOperators={server.operators} allOperators={operators} role={me.role} />
        <div className="animate-slide-up">
          <div className="bg-slate-900 rounded-xl border border-slate-800 overflow-hidden hover:border-slate-700 transition-all duration-300">
            <table className="w-full">
              <thead>
                <tr className="border-b border-slate-800 bg-slate-900/50">
                  <th className="p-4 text-left text-slate-300 font-semibold">
                    Status
                  </th>

                  <th className="p-4 text-left text-slate-300 font-semibold">
                    Detail
                  </th>

                  <th className="p-4 text-left text-slate-300 font-semibold">
                    Time
                  </th>
                </tr>
              </thead>

              <tbody>
                {logs.length > 0 ? (
                  logs.map((log, idx) => (
                    <tr
                      key={log.id}
                      className="border-b border-slate-800 hover:bg-slate-800/50 transition-all duration-200 animate-slide-up"
                      style={{ animationDelay: `${idx * 0.05}s` }}
                    >
                      <td className="p-4">
                        <span
                          className={`inline-flex items-center gap-2 px-3 py-1 rounded-full font-semibold text-sm ${log.status === "UP"
                              ? "bg-green-500/10 text-green-400 border border-green-500/30"
                              : "bg-red-500/10 text-red-400 border border-red-500/30"
                            }`}
                        >
                          <span
                            className={`w-2 h-2 rounded-full ${log.status === "UP"
                                ? "bg-green-400"
                                : "bg-red-400"
                              }`}
                          />
                          {log.status}
                        </span>
                      </td>

                      <td className="p-4 text-slate-300">
                        {log.detail}
                      </td>

                      <td className="p-4 text-slate-400 text-sm">
                        {new Date(
                          log.createdAt
                        ).toLocaleString("en-US", {
                          year: "numeric",
                          month: "short",
                          day: "numeric",
                          hour: "2-digit",
                          minute: "2-digit",
                          second: "2-digit",
                        })}
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan={3} className="p-8 text-center text-slate-400">
                      <svg
                        className="w-12 h-12 mx-auto mb-3 opacity-50"
                        fill="none"
                        stroke="currentColor"
                        viewBox="0 0 24 24"
                      >
                        <path
                          strokeLinecap="round"
                          strokeLinejoin="round"
                          strokeWidth={2}
                          d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"
                        />
                      </svg>
                      No logs available
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </PageWrapper>
  );
}
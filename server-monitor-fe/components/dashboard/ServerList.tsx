"use client";

import { Server } from "@/types/server";
import Link from "next/link";
import { useState } from "react";
import AddServerModal from "@/app/dashboard/AddServerModal";
import { removeServer } from "@/app/dashboard/actions";

type Props = {
  servers: Server[];
  role: "ADMIN" | "OPERATOR";
};

export default function ServerList({ servers, role }: Props) {
  const [showModal, setShowModal] = useState(false);
  const [deletingId, setDeletingId] = useState<string | null>(null);
  const [deleteError, setDeleteError] = useState<string | null>(null);

  const handleDelete = async (id: string, name: string) => {
    if (!window.confirm(`Remove "${name}" from monitoring?`)) return;

    setDeleteError(null);
    setDeletingId(id);

    try {
      await removeServer(id);
    } catch {
      setDeleteError(`Failed to remove "${name}". Please try again.`);
    } finally {
      setDeletingId(null);
    }
  };

  return (
    <div>
      <div className="flex justify-between items-center mb-4">
        <h2 className="text-white font-semibold">Servers</h2>

        {role === "ADMIN" && (
          <button
            onClick={() => setShowModal(true)}
            className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-all"
          >
            + Add Server
          </button>
        )}
      </div>

      {deleteError && (
        <div className="mb-4 bg-red-500/10 border border-red-500/30 text-red-400 px-4 py-3 rounded-lg text-sm animate-shake">
          {deleteError}
        </div>
      )}

      <div className="bg-slate-900 border border-slate-800 rounded-xl overflow-hidden hover:border-slate-700 transition-all duration-300">
        <table className="w-full">
          <thead>
            <tr className="border-b border-slate-800 bg-slate-900/50">
              <th className="p-4 text-left text-slate-300 font-semibold">
                Server Name
              </th>

              <th className="p-4 text-left text-slate-300 font-semibold">
                Status
              </th>

              <th className="p-4 text-left text-slate-300 font-semibold">
                Endpoint
              </th>

              <th className="p-4 text-left text-slate-300 font-semibold">
                Action
              </th>
            </tr>
          </thead>

          <tbody>
            {servers.map((server, idx) => (
              <tr
                key={server.id}
                className="border-b border-slate-800 hover:bg-slate-800/50 transition-all duration-200 animate-slide-up"
                style={{ animationDelay: `${idx * 0.05}s` }}
              >
                <td className="p-4 text-white font-medium">
                  {server.name}
                </td>

                <td className="p-4">
                  <span
                    className={`inline-flex items-center gap-2 px-3 py-1 rounded-full font-semibold text-sm ${server.currentStatus === "UP"
                      ? "bg-green-500/10 text-green-400 border border-green-500/30"
                      : "bg-red-500/10 text-red-400 border border-red-500/30"
                      }`}
                  >
                    <span
                      className={`w-2 h-2 rounded-full ${server.currentStatus === "UP"
                        ? "bg-green-400 animate-pulse-light"
                        : "bg-red-400"
                        }`}
                    />
                    {server.currentStatus}
                  </span>
                </td>

                <td className="p-4 text-slate-300 font-mono text-sm">
                  {server.endpoint}
                </td>

                <td className="p-4">
                  <div className="flex items-center gap-1">
                    <Link
                      href={`/dashboard/servers/${server.id}`}
                      className="inline-flex items-center gap-1 px-3 py-2 text-blue-400 hover:text-blue-300 hover:bg-blue-500/10 rounded-lg transition-all duration-200 active:scale-95"
                    >
                      <span>View</span>
                    </Link>

                    {role === "ADMIN" && (
                      <button
                        onClick={() => handleDelete(server.id, server.name)}
                        disabled={deletingId === server.id}
                        className="inline-flex items-center px-3 py-2 text-red-400 hover:text-red-300 hover:bg-red-500/10 rounded-lg transition-all duration-200 active:scale-95 disabled:opacity-50 disabled:cursor-not-allowed"
                      >
                        {deletingId === server.id ? "Removing..." : "Remove"}
                      </button>
                    )}

                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        {servers.length === 0 && (
          <div className="p-8 text-center text-slate-400">
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
            No servers found
          </div>
        )}
      </div>

      {showModal && (
        <AddServerModal onClose={() => setShowModal(false)} />
      )}
    </div>
  );
}

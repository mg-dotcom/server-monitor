"use client";

import { Server } from "@/types/server";
import Link from "next/link";
import { useState } from "react";
import { removeServer } from "@/app/dashboard/actions";
import ServerFormModal from "./ServerFormModal";

type Props = {
  servers: Server[];
  role: "ADMIN" | "OPERATOR";
};

export default function ServerList({ servers, role }: Props) {
  const [showModal, setShowModal] = useState(false);
  const [editingServer, setEditingServer] = useState<Server | null>(null);
  const [modalMode, setModalMode] = useState<"add" | "edit">("add");
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

  const handleEdit = (id: string) => {
    setShowModal(true);
    const serverToEdit = servers.find((server) => server.id === id);
    if (serverToEdit) {
      setEditingServer(serverToEdit);
      setModalMode("edit");
    }
  }

  return (
    <div>
      <div className="flex justify-between items-center mb-4">
        <h2 className="text-white font-semibold">Servers</h2>

        {role === "ADMIN" && (
          <button
            onClick={() => {
              setShowModal(true);
              setEditingServer(null);
              setModalMode("add");
            }}
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
            {servers.map((server, idx) => {
              const isDisabled = !server.isMonitored;
              return (
                <tr
                  key={server.id}
                  className={`border-b border-slate-800 transition-all duration-200 animate-slide-up ${
                    isDisabled
                      ? "bg-slate-800/30 hover:bg-slate-800/40 opacity-60"
                      : "hover:bg-slate-800/50"
                  }`}
                  style={{ animationDelay: `${idx * 0.05}s` }}
                >
                  <td className="p-4 text-white font-medium">
                    {server.name}
                  </td>

                  <td className="p-4">
                    {server.currentStatus === "CHECKING" ? (
                      <span className="inline-flex items-center gap-2 px-3 py-1 rounded-full font-medium text-xs bg-indigo-500/10 text-indigo-400 border border-indigo-500/30 animate-pulse">
                        <svg className="w-3.5 h-3.5 animate-spin" fill="none" viewBox="0 0 24 24">
                          <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                          <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                        </svg>
                        Checking
                      </span>
                    ) : isDisabled ? (
                      <span className="inline-flex items-center px-3 py-1 rounded-full font-medium text-xs bg-slate-800 text-slate-400 border border-slate-700">
                        Not Monitored
                      </span>
                    ) : (
                      <span
                        className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full font-medium text-xs border ${
                          server.currentStatus === "UP"
                            ? "bg-emerald-500/10 text-emerald-400 border-emerald-500/30"
                            : server.currentStatus === "UNKNOWN"
                            ? "bg-slate-500/10 text-slate-400 border-slate-500/30"
                            : "bg-rose-500/10 text-rose-400 border-rose-500/30" // พวก DOWN หรือ ERROR
                        }`}
                      >
                        <span className={`w-1.5 h-1.5 rounded-full ${
                          server.currentStatus === "UP" ? "bg-emerald-400 shadow-[0_0_8px_rgba(52,211,153,0.8)]" : 
                          server.currentStatus === "UNKNOWN" ? "bg-slate-400" : 
                          "bg-rose-400 shadow-[0_0_8px_rgba(251,113,133,0.8)]"
                        }`}></span>
                        {server.currentStatus}
                      </span>
                    )}
                  </td>

                  <td className="p-4 text-slate-300 font-mono text-sm">
                    {server.endpoint}
                  </td>

                  <td className="p-4">
                    <div className="flex items-center gap-1">
                      {isDisabled || server.currentStatus === "CHECKING" || server.currentStatus === "UNKNOWN" ? (
                        <div 
                          className="inline-flex items-center gap-1 px-3 py-2 text-slate-500 bg-slate-800/30 rounded-lg cursor-not-allowed opacity-50"
                          title={isDisabled ? "Not Monitored" : "Waiting for status update..."} // ✨ แอบใส่ title ให้รู้ว่าทำไมกดไม่ได้
                        >
                          <span>View</span>
                        </div>
                      ) : (
                        <Link
                          href={`/dashboard/servers/${server.id}`}
                          className="inline-flex items-center gap-1 px-3 py-2 text-blue-400 hover:text-blue-300 hover:bg-blue-500/10 rounded-lg transition-all duration-200 active:scale-95"
                        >
                          <span>View</span>
                        </Link>
                      )}

                      {role === "ADMIN" && (
                        <button
                          onClick={() => handleEdit(server.id)}
                          className="inline-flex items-center px-3 py-2 text-yellow-400 hover:text-yellow-300 hover:bg-yellow-500/10 rounded-lg transition-all duration-200 active:scale-95"
                        >
                          Edit
                        </button>
                      )}

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
              );
            })}
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
        <ServerFormModal
          onClose={() => setShowModal(false)}
          existingServer={editingServer}
          mode={modalMode}
        />
      )}
    </div>
  );
}

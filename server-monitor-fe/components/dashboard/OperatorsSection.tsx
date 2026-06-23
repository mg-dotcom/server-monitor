"use client";

import { Operator } from "@/types/server";
import { assignOperator, removeOperator } from "@/app/dashboard/actions";
import { useState } from "react";

type Props = {
    serverId: string;
    assignedOperators: Operator[];
    allOperators: Operator[];
    role: "ADMIN" | "OPERATOR";
};

export default function OperatorsSection({ serverId, assignedOperators, allOperators, role }: Props) {
    const [selectedOperatorId, setSelectedOperatorId] = useState<string | null>(null);

    return (
        <div className="bg-slate-900 rounded-xl border border-slate-800 p-6 space-y-4">
            <h3 className="text-white font-semibold text-lg">Operators</h3>
            <div className="space-y-2">
                {assignedOperators.length === 0 ? (
                    <p className="text-slate-500 text-sm">No operators assigned</p>
                ) : (
                    assignedOperators.map((operator) => (
                        <div key={operator.id} className="flex justify-between items-center bg-slate-800 rounded-lg p-2">
                            <span className="text-slate-300">{operator.name}</span>
                            {role === "ADMIN" && (
                                <button
                                    onClick={async () => {
                                        try {
                                            await removeOperator(serverId, operator.id);
                                            alert(`Removed operator ${operator.name}`);
                                        } catch {
                                            alert(`Failed to remove operator ${operator.name}`);
                                        }
                                    }}
                                    className="px-3 py-1 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-all"
                                >
                                    Remove
                                </button>
                            )}
                        </div>
                    ))
                )}
            </div>
            {role === "ADMIN" && (
                <div className="flex items-center space-x-2 mt-4">
                    <select
                        value={selectedOperatorId || ""}
                        onChange={(e) => setSelectedOperatorId(e.target.value)}
                        className="bg-slate-800 text-slate-300 rounded-lg p-2 flex-grow"
                    >
                        <option value="" disabled>
                            Select an operator
                        </option>
                        {allOperators
                            .filter((op) =>
                                !assignedOperators.some((assigned) => assigned.id === op.id) 
                                && op.role !== "ADMIN" 
                            )
                            .map((operator) => (
                                <option key={operator.id} value={operator.id}>
                                    {operator.name}
                                </option>
                            ))}
                    </select>
                    <button
                        onClick={async () => {
                            if (!selectedOperatorId) return;
                            try {
                                await assignOperator(serverId, selectedOperatorId);
                                alert(`Assigned operator to server`);
                                setSelectedOperatorId(null);
                            } catch (error) {
                                alert(`Failed to assign operator to server`);
                            }
                        }}
                        className="px-3 py-1 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-all"
                    >
                        Assign
                    </button>
                </div>
            )}

        </div>
    );
}
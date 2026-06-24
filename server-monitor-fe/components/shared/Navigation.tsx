"use client";

import Link from "next/link";
import { useRouter, usePathname } from "next/navigation";
import { logoutAction } from "@/app/dashboard/actions";
import { AuthenticatedUser } from "@/types/auth";

type NavigationProps = {
  user: AuthenticatedUser | null
}

export default function Navigation({ user }: NavigationProps) {
  const router = useRouter();
  const pathname = usePathname();

  const showBackButton = pathname !== "/" && pathname !== "/login" && pathname !== "/dashboard";
  const showLogoutButton = !!user && pathname !== "/" && pathname !== "/login";

  return (
    <nav className="bg-slate-900 border-b border-slate-800 sticky top-0 z-40">
      <div className="max-w-6xl mx-auto px-6 py-4 flex items-center justify-between">
        <div className="flex items-center gap-4">
          {showBackButton && (
            <button
              onClick={() => router.back()}
              className="flex items-center gap-2 px-3 py-2 rounded-lg text-slate-300 hover:text-white hover:bg-slate-800 transition-all duration-200 active:scale-95"
              aria-label="Go back"
            >
              <svg
                className="w-5 h-5"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M15 19l-7-7 7-7"
                />
              </svg>
              <span className="text-sm hidden sm:inline">Back</span>
            </button>
          )}

          <Link
            href="/dashboard"
            className="flex items-center gap-2 text-white font-semibold hover:text-slate-200 transition-colors"
          >
            <div className="w-2 h-2 bg-blue-500 rounded-full" />
            Server Monitor
          </Link>
        </div>

        <div className="flex items-center gap-4">
          {showLogoutButton && user && (
            <div className="flex items-center gap-3 px-4 py-2.5 rounded-xl bg-slate-900/50 border border-slate-700/50 shadow-sm hover:bg-slate-800/80 transition-all duration-200">
              <div className="flex flex-col">
                <div className="flex items-center gap-2">
                  <span className="text-sm text-slate-100 font-semibold tracking-wide">
                    {user.username}
                  </span>
                  <span className={`px-2 py-0.5 rounded-md text-[10px] font-bold uppercase tracking-wider border ${user.role === "ADMIN"
                      ? "bg-amber-500/10 text-amber-400 border-amber-500/30"
                      : "bg-blue-500/10 text-blue-400 border-blue-500/30"
                    }`}>
                    {user.role}
                  </span>
                </div>

                <span className="text-xs text-slate-400 mt-0.5">
                  {user.name}
                </span>
              </div>

            </div>
          )}
          {showLogoutButton && (
            <button
              onClick={async () => {
                await logoutAction();
                router.push("/login");
                router.refresh();
              }}
              className="px-4 py-2 rounded-lg text-sm text-slate-300 hover:text-white hover:bg-slate-800 transition-all duration-200 active:scale-95"
            >
              Logout
            </button>
          )}
        </div>
      </div>
    </nav>
  );
}

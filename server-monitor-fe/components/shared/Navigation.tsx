"use client";

import Link from "next/link";
import { useRouter, usePathname } from "next/navigation";

export default function Navigation() {
  const router = useRouter();
  const pathname = usePathname();

  // Show back button on all pages except home and login
  const showBackButton = pathname !== "/" && pathname !== "/login";

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

        <div className="flex items-center gap-2">
          {pathname !== "/login" && (
            <button
              onClick={async () => {
                await fetch("/api/auth/logout", { method: "POST" });
                router.push("/login");
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

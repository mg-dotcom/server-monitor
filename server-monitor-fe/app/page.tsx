import Link from "next/link";

export default function Home() {
  return (
    <div className="min-h-screen bg-slate-950 flex items-center justify-center px-6">
      <div className="max-w-2xl w-full animate-fade-in">
        <div className="text-center space-y-8">
          {/* Logo/Icon */}
          <div className="flex justify-center">
            <div className="w-16 h-16 bg-gradient-to-br from-blue-500 to-purple-600 rounded-2xl flex items-center justify-center animate-scale-in">
              <svg
                className="w-8 h-8 text-white"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M9 3v2m6-2v2m-9 12v2m6-2v2M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2m-4 0V3m0 2H9m3 6h.01M15 6h.01M12 18h.01M15 18h.01M9 18h.01"
                />
              </svg>
            </div>
          </div>

          {/* Heading */}
          <div className="space-y-3 animate-slide-up" style={{ animationDelay: "0.1s" }}>
            <h1 className="text-4xl sm:text-5xl font-bold text-white">
              Server Monitor
            </h1>
            <p className="text-xl text-slate-300">
              Monitor and manage your servers with ease
            </p>
          </div>

          {/* Description */}
          <div className="animate-slide-up" style={{ animationDelay: "0.2s" }}>
            <p className="text-slate-400 text-lg">
              Real-time server monitoring, status tracking, and instant alerts for your infrastructure
            </p>
          </div>

          {/* CTA Buttons */}
          <div className="flex flex-col sm:flex-row gap-4 justify-center pt-4 animate-slide-up" style={{ animationDelay: "0.3s" }}>
            <Link
              href="/login"
              className="px-8 py-3 bg-blue-600 hover:bg-blue-700 text-white font-semibold rounded-lg transition-all duration-200 hover:shadow-lg hover:-translate-y-0.5 active:scale-95 text-center"
            >
              Get Started
            </Link>
          </div>

          {/* Features */}
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 pt-8 animate-fade-in" style={{ animationDelay: "0.4s" }}>
            {[
              {
                icon: "📊",
                title: "Real-time Stats",
                desc: "Live server status and metrics",
              },
              {
                icon: "🔔",
                title: "Alerts",
                desc: "Instant notifications of issues",
              },
              {
                icon: "📈",
                title: "History",
                desc: "Track server history and logs",
              },
            ].map((feature, idx) => (
              <div
                key={idx}
                className="p-4 rounded-lg border border-slate-800 bg-slate-900/50 hover:bg-slate-900 transition-all duration-200 hover:border-slate-700 animate-slide-up"
                style={{ animationDelay: `${0.5 + idx * 0.1}s` }}
              >
                <div className="text-2xl mb-2">{feature.icon}</div>
                <h3 className="text-white font-semibold mb-1">{feature.title}</h3>
                <p className="text-sm text-slate-400">{feature.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}

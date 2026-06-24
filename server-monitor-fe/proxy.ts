import type { NextRequest } from "next/server";
import { NextResponse } from "next/server"

export function proxy(request: NextRequest) {
    const token = request.cookies.get("token")?.value

    if (token && request.nextUrl.pathname == "/") {
        return NextResponse.redirect(new URL("/dashboard", request.url))
    } else if (!token && request.nextUrl.pathname.startsWith("/dashboard")) {
        return NextResponse.redirect(new URL("/", request.url))
    }

}

export const config = {
    matcher: [
        "/",
        "/dashboard/:path*"
    ]
}
import { Link, useNavigate } from 'react-router-dom'

export default function Navbar() {
    const navigate = useNavigate()
    const user = JSON.parse(localStorage.getItem('user') || '{}') as {
        userId?: string
        username?: string
    }

    const handleLogout = () => {
        localStorage.removeItem('user')
        navigate('/login')
    }

    return (
        <nav className="bg-slate-900 text-white shadow-md">
            <div className="mx-auto flex max-w-6xl items-center justify-between px-6 py-4">
                <Link to="/movies" className="text-xl font-bold tracking-wide text-indigo-400 hover:text-indigo-300">
                    🎬 MovieApp
                </Link>

                <div className="flex items-center gap-6">
                    {user.userId ? (
                        <>
                            <Link
                                to="/movies"
                                className="text-sm font-medium text-slate-300 transition hover:text-white"
                            >
                                Phim
                            </Link>
                            <Link
                                to="/bookings"
                                className="text-sm font-medium text-slate-300 transition hover:text-white"
                            >
                                Vé
                            </Link>
                            <span className="rounded-full bg-slate-700 px-3 py-1 text-sm font-semibold text-indigo-300">
                                {user.username}
                            </span>
                            <button
                                onClick={handleLogout}
                                className="rounded-lg bg-red-600 px-4 py-2 text-sm font-medium text-white transition hover:bg-red-500"
                            >
                                Đăng xuất
                            </button>
                        </>
                    ) : (
                        <>
                            <Link
                                to="/login"
                                className="rounded-lg border border-slate-600 px-4 py-2 text-sm font-medium text-slate-300 transition hover:border-white hover:text-white"
                            >
                                Đăng nhập
                            </Link>
                            <Link
                                to="/register"
                                className="rounded-lg bg-indigo-600 px-4 py-2 text-sm font-medium text-white transition hover:bg-indigo-500"
                            >
                                Đăng ký
                            </Link>
                        </>
                    )}
                </div>
            </div>
        </nav>
    )
}
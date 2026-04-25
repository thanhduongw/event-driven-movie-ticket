import { useState, useEffect } from 'react'
import { movieApi, bookingApi } from '../api/apiClient'
import type { Movie } from '../types'

export default function MoviesPage() {
    const [movies, setMovies] = useState<Movie[]>([])
    const [message, setMessage] = useState('')
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        movieApi.getAll()
            .then(res => setMovies(res.data))
            .catch(() => setMessage('Không thể tải danh sách phim'))
            .finally(() => setLoading(false))
    }, [])

    const handleBook = async (movie: Movie) => {
        const user = JSON.parse(localStorage.getItem('user') || '{}')

        if (!user.userId) {
            setMessage('Vui lòng đăng nhập trước!')
            return
        }

        try {
            const res = await bookingApi.create({
                userId: user.userId,
                movieId: movie.id,
                movieTitle: movie.title,
                numberOfSeats: 1,
                totalPrice: movie.price,
            })

            setMessage(`Đặt vé thành công! Booking #${res.data.id} đang chờ thanh toán...`)
        } catch {
            setMessage('Đặt vé thất bại!')
        }
    }

    if (loading) {
        return (
            <div className="flex h-96 items-center justify-center">
                <div className="h-10 w-10 animate-spin rounded-full border-4 border-slate-200 border-t-indigo-600" />
            </div>
        )
    }

    return (
        <div className="mx-auto max-w-6xl px-6 py-10">
            <h2 className="mb-2 text-3xl font-bold text-slate-800">🎬 Danh sách phim</h2>
            <p className="mb-8 text-slate-500">Chọn phim yêu thích và đặt vé ngay hôm nay</p>

            {message && (
                <div
                    className={`mb-6 rounded-lg px-4 py-3 text-sm font-medium ${message.includes('thành công')
                            ? 'bg-green-50 text-green-700'
                            : 'bg-red-50 text-red-700'
                        }`}
                >
                    {message}
                </div>
            )}

            <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
                {movies.map(movie => (
                    <div
                        key={movie.id}
                        className="flex flex-col rounded-2xl border border-slate-200 bg-white p-5 shadow-sm transition hover:-translate-y-1 hover:shadow-lg"
                    >
                        <div className="mb-4 flex h-40 items-center justify-center rounded-xl bg-slate-100 text-4xl">
                            🎞️
                        </div>

                        <h3 className="mb-2 text-lg font-bold text-slate-800">{movie.title}</h3>
                        <p className="mb-4 flex-1 text-sm leading-relaxed text-slate-500">{movie.description}</p>

                        <div className="flex items-center justify-between">
                            <span className="text-xl font-bold text-indigo-600">
                                {movie.price?.toLocaleString('vi-VN')} ₫
                            </span>
                            <button
                                onClick={() => handleBook(movie)}
                                className="rounded-lg bg-indigo-600 px-5 py-2 text-sm font-semibold text-white transition hover:bg-indigo-500 active:scale-95"
                            >
                                Đặt vé
                            </button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    )
}
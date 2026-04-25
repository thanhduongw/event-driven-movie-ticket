import { useState, useEffect } from 'react'
import { bookingApi } from '../api/apiClient'
import type { Booking } from '../types'

export default function BookingsPage() {
    const [bookings, setBookings] = useState<Booking[]>([])
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        bookingApi.getAll()
            .then(res => setBookings(res.data))
            .catch(() => alert('Không thể tải danh sách vé'))
            .finally(() => setLoading(false))
    }, [])

    const getStatusText = (status: Booking['status']) => {
        switch (status) {
            case 'CONFIRMED': return '✅ Đã xác nhận'
            case 'FAILED': return '❌ Thất bại'
            default: return '⏳ Đang chờ'
        }
    }

    const getStatusColor = (status: Booking['status']) => {
        switch (status) {
            case 'CONFIRMED': return 'text-green-600 bg-green-50'
            case 'FAILED': return 'text-red-600 bg-red-50'
            default: return 'text-amber-600 bg-amber-50'
        }
    }

    return (
        <div className="mx-auto max-w-4xl px-6 py-10">
            <h2 className="mb-6 text-2xl font-bold text-slate-800">🎟️ Vé của bạn</h2>

            {loading ? (
                <div className="flex h-40 items-center justify-center">
                    <div className="h-8 w-8 animate-spin rounded-full border-4 border-slate-200 border-t-indigo-600" />
                </div>
            ) : bookings.length === 0 ? (
                <p className="rounded-lg bg-slate-50 py-12 text-center text-slate-500">
                    Bạn chưa có vé nào.
                </p>
            ) : (
                <div className="grid gap-4">
                    {bookings.map(b => (
                        <div
                            key={b.id}
                            className="flex items-center justify-between rounded-xl border border-slate-200 bg-white p-5 shadow-sm transition hover:shadow-md"
                        >
                            <div>
                                <h3 className="text-lg font-semibold text-slate-800">{b.movieTitle}</h3>
                                <p className="mt-1 text-sm text-slate-500">Mã vé: #{b.id}</p>
                            </div>
                            <div className="text-right">
                                <p className="text-lg font-bold text-indigo-600">
                                    {b.totalPrice.toLocaleString()} ₫
                                </p>
                                <span
                                    className={`mt-1 inline-block rounded-full px-3 py-1 text-xs font-medium ${getStatusColor(b.status)}`}
                                >
                                    {getStatusText(b.status)}
                                </span>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    )
}
import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { userApi } from '../api/apiClient'
import axios from 'axios'

export default function RegisterPage() {
    const [form, setForm] = useState({ username: '', password: '', email: '' })
    const [msg, setMsg] = useState('')
    const navigate = useNavigate()

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault()

        try {
            const res = await userApi.register(form)
            setMsg(res.data.message)
            setTimeout(() => navigate('/login'), 1500)
        } catch (err: unknown) {
            if (axios.isAxiosError(err)) {
                setMsg(err.response?.data?.error || 'Đăng ký thất bại')
            } else {
                setMsg('Lỗi không xác định')
            }
        }
    }

    return (
        <div className="flex min-h-screen items-center justify-center bg-slate-50 px-4">
            <div className="w-full max-w-md rounded-2xl bg-white p-8 shadow-lg">
                <h2 className="mb-2 text-center text-2xl font-bold text-slate-800">Tạo tài khoản</h2>
                <p className="mb-6 text-center text-sm text-slate-500">Đăng ký để đặt vé xem phim</p>

                <form onSubmit={handleSubmit} className="space-y-4">
                    <div>
                        <label className="mb-1 block text-sm font-medium text-slate-700">Tên đăng nhập</label>
                        <input
                            value={form.username}
                            onChange={e => setForm({ ...form, username: e.target.value })}
                            className="w-full rounded-lg border border-slate-300 px-4 py-2.5 text-sm outline-none transition focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200"
                            placeholder="Nhập username"
                            required
                        />
                    </div>

                    <div>
                        <label className="mb-1 block text-sm font-medium text-slate-700">Email</label>
                        <input
                            type="email"
                            value={form.email}
                            onChange={e => setForm({ ...form, email: e.target.value })}
                            className="w-full rounded-lg border border-slate-300 px-4 py-2.5 text-sm outline-none transition focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200"
                            placeholder="Nhập email"
                            required
                        />
                    </div>

                    <div>
                        <label className="mb-1 block text-sm font-medium text-slate-700">Mật khẩu</label>
                        <input
                            type="password"
                            value={form.password}
                            onChange={e => setForm({ ...form, password: e.target.value })}
                            className="w-full rounded-lg border border-slate-300 px-4 py-2.5 text-sm outline-none transition focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200"
                            placeholder="Nhập mật khẩu"
                            required
                        />
                    </div>

                    <button
                        type="submit"
                        className="w-full rounded-lg bg-indigo-600 py-2.5 text-sm font-semibold text-white transition hover:bg-indigo-500 active:scale-[0.98]"
                    >
                        Đăng ký
                    </button>
                </form>

                {msg && (
                    <p
                        className={`mt-4 rounded-lg px-4 py-2 text-center text-sm font-medium ${msg.includes('thành công') || msg.includes('success')
                                ? 'bg-green-50 text-green-700'
                                : 'bg-red-50 text-red-700'
                            }`}
                    >
                        {msg}
                    </p>
                )}

                <p className="mt-6 text-center text-sm text-slate-500">
                    Đã có tài khoản?{' '}
                    <Link to="/login" className="font-medium text-indigo-600 hover:underline">
                        Đăng nhập
                    </Link>
                </p>
            </div>
        </div>
    )
}
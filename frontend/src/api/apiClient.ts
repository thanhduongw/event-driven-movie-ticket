import axios from 'axios'
import type {
    RegisterRequest,
    LoginRequest,
    BookingRequest,
    Movie,
    Booking
} from '../types'

const API_BASE = '/api'

export const userApi = {
    register: (data: RegisterRequest) =>
        axios.post<{ message: string }>(`${API_BASE}/users/register`, data),

    login: (data: LoginRequest) =>
        axios.post<{ userId: string; username: string }>(`${API_BASE}/users/login`, data),
}

export const movieApi = {
    getAll: () =>
        axios.get<Movie[]>(`${API_BASE}/movies`),

    add: (data: Movie) =>
        axios.post(`${API_BASE}/movies`, data),
}

export const bookingApi = {
    create: (data: BookingRequest) =>
        axios.post<Booking>(`${API_BASE}/bookings`, data),

    getAll: () =>
        axios.get<Booking[]>(`${API_BASE}/bookings`),
}
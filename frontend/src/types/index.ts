export interface RegisterRequest {
    username: string
    password: string
    email: string
}

export interface LoginRequest {
    username: string
    password: string
}

export interface Movie {
    id: string
    title: string
    description: string
    genre: string
    duration: number
    price: number
    availableSeats: number
}

export interface Booking {
    id: string
    userId: string
    movieId: string
    movieTitle: string
    numberOfSeats: number
    totalPrice: number
    status: 'PENDING' | 'CONFIRMED' | 'FAILED'
    createdAt: string
}

export interface BookingRequest {
    userId: string
    movieId: string
    movieTitle: string
    numberOfSeats: number
    totalPrice: number
}
package iuh.fit.se.service;

import iuh.fit.se.model.Movie;
import iuh.fit.se.repository.MovieRepository;
import jakarta.annotation.*;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    @PostConstruct
    public void initData() {
        // Seed dữ liệu mẫu
        movieRepository.save(new Movie(null, "Avengers: Endgame", "Siêu anh hùng", "Action", 182, 85000, 50));
        movieRepository.save(new Movie(null, "Inception", "Thế giới giấc mơ", "Sci-Fi", 148, 75000, 30));
        movieRepository.save(new Movie(null, "Interstellar", "Vũ trụ", "Sci-Fi", 169, 80000, 40));
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    public Optional<Movie> updateMovie(Long id, Movie update) {
        return movieRepository.findById(id).map(movie -> {
            movie.setTitle(update.getTitle());
            movie.setDescription(update.getDescription());
            movie.setGenre(update.getGenre());
            movie.setDuration(update.getDuration());
            movie.setPrice(update.getPrice());
            movie.setAvailableSeats(update.getAvailableSeats());
            return movieRepository.save(movie);
        });
    }
}

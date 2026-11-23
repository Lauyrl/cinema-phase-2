package vn.edu.uet.cinema.business.movie;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import vn.edu.uet.cinema.business.PageResult;

public interface MovieRepository {

        List<Movie> findAll();

        PageResult<Movie> findAll(int page, int size);

        Optional<Movie> findById(UUID id);

        Movie save(Movie movie);

        void deleteById(UUID id);
}
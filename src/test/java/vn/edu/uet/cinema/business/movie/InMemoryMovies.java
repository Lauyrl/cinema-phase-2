package vn.edu.uet.cinema.business.movie;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import vn.edu.uet.cinema.business.PageResult;

/** In-memory {@link MovieRepository} for business tests. */
public class InMemoryMovies implements MovieRepository {

        private final Map<UUID, Movie> rows = new LinkedHashMap<>();

        @Override
        public List<Movie> findAll() {
                return new ArrayList<>(rows.values());
        }

        @Override
        public PageResult<Movie> findAll(int page, int size) {
                List<Movie> sorted = rows.values().stream()
                        .sorted(Comparator.comparing(Movie::releaseDate).reversed().thenComparing(Movie::id))
                        .toList();
                int fromIndex = Math.min((Math.max(page, 1) - 1) * size, sorted.size());
                int toIndex = Math.min(fromIndex + size, sorted.size());
                return PageResult.of(sorted.subList(fromIndex, toIndex), page, size, sorted.size());
        }

        @Override
        public Optional<Movie> findById(UUID id) {
                return Optional.ofNullable(rows.get(id));
        }

        @Override
        public Movie save(Movie movie) {
                rows.put(movie.id(), movie);
                return movie;
        }

        @Override
        public void deleteById(UUID id) {
                rows.remove(id);
        }
}
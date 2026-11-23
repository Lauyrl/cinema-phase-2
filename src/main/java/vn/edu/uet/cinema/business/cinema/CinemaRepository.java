package vn.edu.uet.cinema.business.cinema;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import vn.edu.uet.cinema.business.PageResult;

public interface CinemaRepository {

        List<Cinema> findAll();

        PageResult<Cinema> findAll(int page, int size);

        Optional<Cinema> findById(UUID id);

        Cinema save(Cinema cinema);

        void deleteById(UUID id);
}
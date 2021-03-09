package codes.recursive.repository;

import codes.recursive.domain.Reading;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.PageableRepository;
import io.reactivex.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

@Repository
public interface ReadingRepository extends PageableRepository<Reading, Long> {
    CompletableFuture<Reading> saveAsync(@NonNull Reading reading);
}

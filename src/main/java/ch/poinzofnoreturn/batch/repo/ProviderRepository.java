package ch.poinzofnoreturn.batch.repo;

import ch.poinzofnoreturn.batch.model.db.ProviderEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Datenbank-Zugriff auf die Anbieter
 */
public interface ProviderRepository extends CrudRepository<ProviderEntity, Long> {

    public List<ProviderEntity> findByPoinzId(String poinzId);
}

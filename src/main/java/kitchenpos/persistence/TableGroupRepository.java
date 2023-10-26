package kitchenpos.persistence;

import kitchenpos.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

    @Query("SELECT tg"
            + " FROM TableGroup tg"
            + " LEFT JOIN OrderTable ot ON ot.tableGroup.id = tg.id"
            + " WHERE tg.id = (:id)")
    Optional<TableGroup> findByIdWithOrderTables(final Long id);
}

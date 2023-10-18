package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

    @Query("SELECT tg FROM TableGroup tg LEFT JOIN FETCH tg.orderTables ob LEFT JOIN FETCH ob.orderTables WHERE tg.id = :id")
    Optional<TableGroup> findById(@Param("id") final Long id);

    List<TableGroup> findAll();
}

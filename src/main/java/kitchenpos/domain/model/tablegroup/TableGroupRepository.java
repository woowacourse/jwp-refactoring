package kitchenpos.domain.model.tablegroup;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
    TableGroup save(TableGroup entity);

    Optional<TableGroup> findById(Long id);

    @Query("select tg from TableGroup tg join fetch tg.orderTables")
    Optional<TableGroup> findByIdWithOrderTables(Long id);

    List<TableGroup> findAll();
}

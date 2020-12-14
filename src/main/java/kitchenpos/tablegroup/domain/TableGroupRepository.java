package kitchenpos.tablegroup.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
    @Query("select tg from TableGroup tg join fetch tg.orderTables")
    Optional<TableGroup> findByIdWithOrderTables(Long id);
}

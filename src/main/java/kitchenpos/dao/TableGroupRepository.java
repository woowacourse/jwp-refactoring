package kitchenpos.dao;

import java.util.Optional;
import kitchenpos.domain.TableGroup;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

    @EntityGraph(attributePaths = {"orderTables.values"}, type = EntityGraphType.LOAD)
    Optional<TableGroup> findById(Long id);
}

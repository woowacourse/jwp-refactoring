package kitchenpos.order.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.OrderTable;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface TableRepository extends JpaRepository<OrderTable, Long> {

    Optional<OrderTable> findWithTableGroupById(Long id);

    List<OrderTable> findAllByIdIn(List<Long> tableGroupIdIds);

    @EntityGraph(attributePaths = "tableGroup")
    List<OrderTable> findAllWithGroupByTableGroupId(@Param("tableGroupId") Long tableGroupId);
}

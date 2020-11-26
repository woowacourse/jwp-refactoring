package kitchenpos.repository.order;

import kitchenpos.domain.table.OrderTable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    @EntityGraph(attributePaths = "tableGroup")
    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(List<Long> orderTableIds);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
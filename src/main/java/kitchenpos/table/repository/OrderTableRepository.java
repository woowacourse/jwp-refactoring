package kitchenpos.table.repository;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);

    List<OrderTable> findAllByIdIn(List<Long> orderTableIds);
}

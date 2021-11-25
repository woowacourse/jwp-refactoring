package kitchenpos.ordertable.repository;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIn(List<Long> orderTableIds);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}

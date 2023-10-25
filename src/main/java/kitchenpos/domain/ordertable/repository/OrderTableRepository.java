package kitchenpos.domain.ordertable.repository;

import java.util.List;
import kitchenpos.domain.ordertable.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByTableGroupId(final Long tableGroupId);
}

package kitchenpos.ordertable.domain.repository;

import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findAllByIdIn(final List<Long> orderTableIds);

    List<OrderTable> findAllByTableGroupId(final Long tableGroupId);
}

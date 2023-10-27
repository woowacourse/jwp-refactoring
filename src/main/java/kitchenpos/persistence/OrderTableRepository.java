package kitchenpos.persistence;

import kitchenpos.domain.table.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByTableGroupId(final Long tableGroupId);

    List<OrderTable> findAllByIdIn(final List<Long> orderTableIds);
}

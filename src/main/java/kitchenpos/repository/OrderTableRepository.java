package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.entity.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIn(final List<Long> orderTableIds);

    List<OrderTable> findAllByTableGroupId(final Long tableGroupId);
}

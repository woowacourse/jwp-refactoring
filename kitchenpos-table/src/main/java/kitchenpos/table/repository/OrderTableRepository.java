package kitchenpos.table.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import kitchenpos.table.domain.OrderTable;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIn(final List<Long> ids);

    List<OrderTable> findAllByGroupId(final Long tableGroupId);
}

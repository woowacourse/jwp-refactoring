package kitchenpos.domain.repository;

import java.util.List;
import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIn(final List<Long> ids);

    List<OrderTable> findAllByTableGroupId(final Long tableGroupId);
}

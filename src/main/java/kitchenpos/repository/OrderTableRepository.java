package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIn(final List<Long> orderTableIds);

    List<OrderTable> findAllByTableGroupId(final Long tableGroupId);
}

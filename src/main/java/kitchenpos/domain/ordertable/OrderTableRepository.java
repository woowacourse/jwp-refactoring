package kitchenpos.domain.ordertable;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByTableGroupId(long tableGroupId);
}

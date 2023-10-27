package kitchenpos.ordertable.infra;

import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    default OrderTable getById(Long id) {
        return findById(id).orElseThrow(() -> new NoSuchElementException("테이블이 존재하지 않습니다."));
    }

    List<OrderTable> getAllByIdIn(List<Long> ids);

    List<OrderTable> getAllByTableGroupId(Long tableGroupId);
}

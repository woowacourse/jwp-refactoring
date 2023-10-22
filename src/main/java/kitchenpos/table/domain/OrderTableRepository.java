package kitchenpos.table.domain;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    @Override
    default OrderTable getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new NoSuchElementException("id가 %d인 테이블을 찾을 수 없습니다.".formatted(id)));
    }

    List<OrderTable> findAllByIdIn(List<Long> orderTableIds);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}

package kitchenpos.ordertable;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    default OrderTable getBy(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("그런 테이블은 없습니다"));
    }

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}

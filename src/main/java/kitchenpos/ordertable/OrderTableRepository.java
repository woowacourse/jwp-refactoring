package kitchenpos.ordertable;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    default OrderTable getBy(Long id) {
        return findById(id).orElseThrow(IllegalArgumentException::new);
    }

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}

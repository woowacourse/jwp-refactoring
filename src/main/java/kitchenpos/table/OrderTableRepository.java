package kitchenpos.table;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface OrderTableRepository extends Repository<OrderTable, Long> {

    OrderTable save(OrderTable entity);

    Optional<OrderTable> findById(Long id);

    default OrderTable getById(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));
    }

    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(List<Long> ids);

    boolean existsById(Long orderTableId);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}

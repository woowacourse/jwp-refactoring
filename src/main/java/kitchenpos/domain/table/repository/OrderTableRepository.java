package kitchenpos.domain.table.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.table.OrderTable;
import org.springframework.data.repository.Repository;

public interface OrderTableRepository extends Repository<OrderTable, Long> {

    OrderTable save(OrderTable entity);

    Optional<OrderTable> findById(Long id);

    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);

    default OrderTable get(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));
    }
}

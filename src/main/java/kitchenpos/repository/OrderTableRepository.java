package kitchenpos.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.notfound.OrderTableNotFoundException;
import org.springframework.data.repository.Repository;

public interface OrderTableRepository extends Repository<OrderTable, Long> {
    OrderTable save(OrderTable entity);

    Optional<OrderTable> findById(Long id);

    default OrderTable getById(Long id) {
        return findById(id).orElseThrow(() -> new OrderTableNotFoundException(id));
    }

    List<OrderTable> findAll();

    List<OrderTable> findAllByIdInAndEmptyIsTrueAndTableGroupIdIsNull(Collection<Long> id);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}

package kitchenpos.dao;

import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable,Long> {
    default OrderTable getById(final Long id) {
        return findById(id).orElseThrow(IllegalArgumentException::new);
    }

    List<OrderTable> findAllByIdIn(final List<Long> ids);
}

package kitchenpos.repository;

import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    default OrderTable getById(final Long id) {
        return this.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }
}

package kitchenpos.repository;

import kitchenpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    default Order getById(final Long id) {
        return this.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }
}

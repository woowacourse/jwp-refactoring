package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    default Order getById(final Long id) {
        return findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));
    }
}

package kitchenpos.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    default Order getById(Long id) throws IllegalArgumentException {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다. id = " + id));
    }
}

package order.domain.repository;

import java.util.List;
import order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    default Order getByIdOrThrow(final Long id) {
        return findById(id)
                .orElseThrow(() -> new NoEntityException("존재하지 않는 주문입니다."));
    }

    List<Order> findAllByOrderTableId(Long orderTableId);

    class NoEntityException extends RuntimeException {

        public NoEntityException(final String message) {
            super(message);
        }
    }
}

package repository;

import domain.Order;
import exception.OrderException;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import repository.customRepository.CustomOrderRepository;

public interface OrderRepository extends JpaRepository<Order, Long>, CustomOrderRepository {

    default Order getById(final Long id) {
        return findById(id).orElseThrow(() -> new OrderException.NotFoundException(id));
    }

    List<Order> findByOrderTableId(final Long orderTableId);
}

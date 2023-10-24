package kitchenpos.domain.repository;

import kitchenpos.domain.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findAll();

    Order findByOrderTableId(Long orderTableId);
}

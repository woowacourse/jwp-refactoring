package kitchenpos.domain.order;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findAll();

    Optional<Order> findByOrderTableId(Long orderTableId);
}

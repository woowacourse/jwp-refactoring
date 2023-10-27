package repository;

import domain.OrderLineItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface OrderLineItemDao extends Repository<OrderLineItem, Long> {

    OrderLineItem save(OrderLineItem entity);

    Optional<OrderLineItem> findById(Long id);

    List<OrderLineItem> findAll();
}

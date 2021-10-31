package kitchenpos.dao;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

    List<OrderLineItem> findAllByOrder(Order order);
}

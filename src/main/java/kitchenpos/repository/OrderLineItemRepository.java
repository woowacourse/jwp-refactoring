package kitchenpos.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public interface OrderLineItemRepository extends Repository<OrderLineItem, Long> {

    OrderLineItem save(OrderLineItem orderLineItem);
}

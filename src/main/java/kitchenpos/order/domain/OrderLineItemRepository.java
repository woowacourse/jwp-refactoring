package kitchenpos.order.domain;

import java.util.List;
import org.springframework.data.repository.Repository;

public interface OrderLineItemRepository extends Repository<OrderLineItem, Long> {

    List<OrderLineItem> findAll();
}

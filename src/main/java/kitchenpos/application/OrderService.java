package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.ui.dto.request.OrderChangeStatusRequest;
import kitchenpos.ui.dto.request.OrderCreateRequest;

public interface OrderService {
    Order create(OrderCreateRequest request);

    List<Order> list();

    Order changeOrderStatus(Long orderId, OrderChangeStatusRequest request);
}

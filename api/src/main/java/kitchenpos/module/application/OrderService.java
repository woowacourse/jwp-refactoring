package kitchenpos.module.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.module.domain.menu.Menu;
import kitchenpos.module.domain.menu.repository.MenuRepository;
import kitchenpos.module.domain.order.Order;
import kitchenpos.module.domain.order.OrderLineItem;
import kitchenpos.module.domain.order.repository.OrderRepository;
import kitchenpos.module.domain.ordertable.OrderTable;
import kitchenpos.module.domain.ordertable.OrderTableRepository;
import kitchenpos.module.domain.vo.OrderStatus;
import kitchenpos.module.presentation.dto.request.OrderCreateRequest;
import kitchenpos.module.presentation.dto.request.OrderLineItemCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public Order create(final OrderCreateRequest request) {
        final long orderTableId = request.getOrderTableId();
        final OrderTable orderTable = findOrderTable(orderTableId);
        final List<OrderLineItemCreateRequest> orderLineItemRequests = request.getOrderLineItems();
        validateMenuToOrder(orderLineItemRequests);

        final List<OrderLineItem> orderLineItems = createOrderLineItems(orderLineItemRequests);
        orderTable.validateToPlace();
        return orderRepository.save(new Order(orderTableId, orderLineItems));
    }

    private OrderTable findOrderTable(final long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateMenuToOrder(final List<OrderLineItemCreateRequest> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItemCreateRequest::getMenuId)
                .collect(Collectors.toList());
        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderLineItem> createOrderLineItems(List<OrderLineItemCreateRequest> orderLineItems) {
        List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItemCreateRequest orderLineItem : orderLineItems) {
            final Optional<Menu> menuToOrder = menuRepository.findById(orderLineItem.getMenuId());
            savedOrderLineItems.add(new OrderLineItem(menuToOrder.get(), orderLineItem.getQuantity()));
        }
        return savedOrderLineItems;
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderRepository.findAll();
    }

    public Order changeOrderStatus(final Long orderId, final String orderStatus) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrder.changeOrderStatus(OrderStatus.valueOf(orderStatus));

        return savedOrder;
    }
}

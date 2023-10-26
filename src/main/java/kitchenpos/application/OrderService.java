package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.vo.OrderStatus;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemCreateRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderTableRepository;
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

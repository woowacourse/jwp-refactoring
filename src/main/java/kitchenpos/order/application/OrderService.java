package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.repository.OrderTableRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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

    @Transactional
    public Order create(final OrderRequest request) {
        final List<OrderLineItem> orderLineItems = createOrderLineItems(request);
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return orderRepository.save(new Order(orderTable.getId(), LocalDateTime.now(), orderLineItems));
    }

    private List<OrderLineItem> createOrderLineItems(final OrderRequest request) {
        final List<Long> menuIds = request.getOrderLineItems().stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        List<Menu> menus = menuRepository.findAllByIdIn(menuIds);

        if (menus.size() != menuIds.size()) {
            throw new IllegalArgumentException();
        }

        return request.getOrderLineItems().stream()
                .map(i -> new OrderLineItem(i.getMenuId(), i.getQuantity()))
                .collect(Collectors.toList());
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        order.changeStatus(orderStatus);
        return order;
    }
}

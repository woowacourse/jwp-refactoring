package kitchenpos.application;

import kitchenpos.Menu;
import kitchenpos.MenuName;
import kitchenpos.MenuPrice;
import kitchenpos.MenuRepository;
import kitchenpos.Order;
import kitchenpos.OrderLineItem;
import kitchenpos.OrderLineItemQuantity;
import kitchenpos.OrderRepository;
import kitchenpos.OrderStatus;
import kitchenpos.OrderTable;
import kitchenpos.OrderTableRepository;
import kitchenpos.application.request.OrderLineItemDto;
import kitchenpos.application.request.OrderRequest;
import kitchenpos.application.request.OrderStatusRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final OrderRepository orderRepository,
                        final MenuRepository menuRepository,
                        final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public Order create(final OrderRequest request) {
        final List<OrderLineItemDto> orderLineItemsDtos = request.getOrderLineItems();
        validateExistenceOfOrderLineItem(orderLineItemsDtos);
        final OrderTable orderTable = findOrderTable(request);
        final List<OrderLineItem> orderLineItems = orderLineItemsDtos.stream()
                .map(m -> {
                    final Menu menu = menuRepository.findById(m.getMenuId())
                            .orElseThrow(() -> new IllegalArgumentException("메뉴가 존재하지 않습니다."));
                    return new OrderLineItem(new MenuName(menu.getName()), new MenuPrice(menu.getPrice()), new OrderLineItemQuantity(m.getQuantity()));
                })
                .collect(Collectors.toList());

        return orderRepository.save(new Order(orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(), orderLineItems));
    }

    private OrderTable findOrderTable(final OrderRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        if (!orderTable.isEmpty()) {
            throw new IllegalArgumentException("테이블이 이미 차있습니다.");
        }

        return orderTable;
    }

    private void validateExistenceOfOrderLineItem(final List<OrderLineItemDto> orderLineItemsDtos) {
        final List<Long> menuIds = orderLineItemsDtos.stream()
                .map(OrderLineItemDto::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemsDtos.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴가 포함되었습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderRepository.findAll();
    }

    public Order changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Order findOrder = findOrder(orderId);
        findOrder.updateOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));

        return findOrder;
    }

    private Order findOrder(final Long orderId) {
        final Order findOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        if (Objects.equals(OrderStatus.COMPLETION, findOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        return findOrder;
    }
}

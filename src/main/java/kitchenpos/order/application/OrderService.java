package kitchenpos.order.application;

import kitchenpos.menu.application.MenuRepository;
import kitchenpos.order.Order;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.OrderTable;
import kitchenpos.order.SaveOrderLineItemsEvent;
import kitchenpos.order.application.request.OrderLineItemDto;
import kitchenpos.order.application.request.OrderRequest;
import kitchenpos.order.application.request.OrderStatusRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final ApplicationEventPublisher publisher;
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final ApplicationEventPublisher publisher,
                        final OrderRepository orderRepository,
                        final MenuRepository menuRepository,
                        final OrderTableRepository orderTableRepository) {
        this.publisher = publisher;
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public Order create(final OrderRequest request) {
        final List<OrderLineItemDto> orderLineItemsDtos = request.getOrderLineItems();
        validateExistenceOfOrderLineItem(orderLineItemsDtos);
        final OrderTable orderTable = findOrderTable(request);
        final Order savedOrder = orderRepository.save(new Order(orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now()));
        publisher.publishEvent(new SaveOrderLineItemsEvent(orderLineItemsDtos, savedOrder));

        return savedOrder;
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

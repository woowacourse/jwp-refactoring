package kitchenpos.domain.order.service;

import kitchenpos.domain.menu.repository.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.repository.OrderLineItemRepository;
import kitchenpos.domain.order.repository.OrderRepository;
import kitchenpos.domain.order.repository.OrderTableRepository;
import kitchenpos.domain.order.service.dto.OrderCreateRequest;
import kitchenpos.domain.order.service.dto.OrderLineItemCreateRequest;
import kitchenpos.domain.order.service.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.toList;
import static kitchenpos.domain.order.OrderStatus.COOKING;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository, final OrderRepository orderRepository, final OrderLineItemRepository orderLineItemRepository, final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final List<Long> menuIds = request.getOrderLineItems().stream()
                .map(OrderLineItemCreateRequest::getMenuId)
                .collect(toList());
        final OrderLineItems orderLineItems = OrderLineItems.from(orderLineItemRepository.findAllByMenuIds(menuIds));

        if (orderLineItems.getOrderLineItems().size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException(String.format("저장되어 있지 않은 Menu가 존재합니다. menuIds = %s", menuIds));
        }

        final OrderTable savedOrderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException(String.format("OrderTableId로 OrderTable을 찾을 수 없습니다. 입력값 = %s", request.getOrderTableId())));
        final Order savedOrder = orderRepository.save(new Order(savedOrderTable, COOKING, now(), new OrderLineItems()));
        savedOrder.addAllOrderLineItems(orderLineItems);

        return OrderResponse.toDto(savedOrder);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::toDto)
                .collect(toList());
    }

    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(order.getOrderStatus());
        final List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(orderId);
        savedOrder.addAllOrderLineItems(OrderLineItems.from(orderLineItems));

        return savedOrder;
    }
}

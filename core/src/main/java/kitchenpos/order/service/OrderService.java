package kitchenpos.order.service;

import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItems;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.service.dto.OrderCreateRequest;
import kitchenpos.order.service.dto.OrderLineItemCreateRequest;
import kitchenpos.order.service.dto.OrderResponse;
import kitchenpos.order.service.dto.OrderUpateRequest;
import kitchenpos.table.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.toList;
import static kitchenpos.order.OrderStatus.COOKING;

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
        final Order savedOrder = orderRepository.save(new Order(savedOrderTable.getId(), COOKING, now(), new OrderLineItems()));
        savedOrder.addAllOrderLineItems(orderLineItems);

        return OrderResponse.toDto(savedOrder);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::toDto)
                .collect(toList());
    }

    public void changeOrderStatus(final Long orderId, final OrderUpateRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Order 식별자로 Order를 찾을 수 없습니다. 식별자 = %s", orderId)));

        savedOrder.changeOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));
    }
}

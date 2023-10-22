package kitchenpos.application;

import kitchenpos.application.dto.request.CreateOrderRequest;
import kitchenpos.application.dto.request.OrderLineItemDto;
import kitchenpos.application.dto.request.UpdateOrderStatusRequest;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItemRepository;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            MenuRepository menuRepository,
            OrderRepository orderRepository,
            OrderLineItemRepository orderLineItemRepository,
            OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(CreateOrderRequest createOrderRequest) {
        OrderTable orderTable = orderTableRepository.findById(createOrderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now());

        List<OrderLineItemDto> orderLineItemDtos = createOrderRequest.getOrderLineItems();
        addOrderLineItems(orderLineItemDtos, order);

        long orderMenuSize = extractOrderMenuCount(orderLineItemDtos);
        order.checkEqualMenuCount(orderMenuSize);

        orderRepository.save(order);
        return OrderResponse.from(order);
    }

    private long extractOrderMenuCount(List<OrderLineItemDto> orderLineItemDtos) {
        List<Long> menuIds = orderLineItemDtos.stream()
                .map(OrderLineItemDto::getMenuId)
                .collect(Collectors.toList());

        return menuRepository.countByIdIn(menuIds);
    }

    private void addOrderLineItems(List<OrderLineItemDto> orderLineItemDtos, Order order) {
        for (OrderLineItemDto orderLineItemDto : orderLineItemDtos) {
            Menu menu = menuRepository.findById(orderLineItemDto.getMenuId())
                    .orElseThrow(IllegalArgumentException::new);
            OrderLineItem orderLineItem = new OrderLineItem(menu, orderLineItemDto.getQuantity());
            order.addOrderLineItem(orderLineItem);
        }
    }

    public List<OrderResponse> list() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, UpdateOrderStatusRequest updateOrderStatusRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (order.isCompleted()) {
            throw new IllegalArgumentException();
        }

        OrderStatus orderStatus = OrderStatus.valueOf(updateOrderStatusRequest.getOrderStatus());
        order.changeOrderStatus(orderStatus);

        return OrderResponse.from(order);
    }
}

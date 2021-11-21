package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.exception.InvalidMenuException;
import kitchenpos.exception.InvalidOrderException;
import kitchenpos.exception.InvalidOrderTableException;
import kitchenpos.ui.dto.request.order.OrderLineItemDto;
import kitchenpos.ui.dto.request.order.OrderRequestDto;
import kitchenpos.ui.dto.request.order.OrderStatusRequestDto;
import kitchenpos.ui.dto.response.order.OrderLineItemResponseDto;
import kitchenpos.ui.dto.response.order.OrderResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
        MenuRepository menuRepository,
        OrderRepository orderRepository,
        OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponseDto create(final OrderRequestDto orderRequestDto) {
        final OrderTable orderTable = findOrderTableById(orderRequestDto);
        final List<OrderLineItem> orderLineItems = toOrderLineItems(
            orderRequestDto.getOrderLineItems());

        final Order order = new Order(orderTable, orderLineItems);
        final Order created = orderRepository.save(order);

        return toOrderResponseDto(created);
    }

    private OrderTable findOrderTableById(OrderRequestDto orderRequestDto) {
        return orderTableRepository.findById(orderRequestDto.getOrderTableId())
            .orElseThrow(InvalidOrderTableException::new);
    }

    private List<OrderLineItem> toOrderLineItems(List<OrderLineItemDto> orderLineItems) {
        return orderLineItems.stream()
            .map(this::getOrderLineItem)
            .collect(toList());
    }

    private OrderLineItem getOrderLineItem(OrderLineItemDto orderLineItemDto) {
        final Menu menu = menuRepository.findById(orderLineItemDto.getMenuId())
            .orElseThrow(InvalidMenuException::new);

        return new OrderLineItem(menu, orderLineItemDto.getQuantity());
    }

    public List<OrderResponseDto> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
            .map(this::toOrderResponseDto)
            .collect(toList());
    }

    @Transactional
    public OrderResponseDto changeOrderStatus(
        final Long orderId, final OrderStatusRequestDto orderStatusRequestDto
    ) {
        final Order order = orderRepository.findById(orderId)
            .orElseThrow(InvalidOrderException::new);

        order.changeOrderStatus(orderStatusRequestDto.getOrderStatus());
        return toOrderResponseDto(order);
    }

    private OrderResponseDto toOrderResponseDto(Order order) {
        return new OrderResponseDto(
            order.getId(),
            order.getOrderTableId(),
            order.getOrderStatus(),
            order.getOrderedTime(),
            toOrderLineItemsResponseDto(order.getId(), order.getOrderLineItems()));
    }

    private List<OrderLineItemResponseDto> toOrderLineItemsResponseDto(
        Long orderId, List<OrderLineItem> orderLineItems
    ) {
        return orderLineItems.stream()
            .map(orderLineItem ->
                new OrderLineItemResponseDto(
                    orderLineItem.getSeq(),
                    orderId,
                    orderLineItem.getMenuId(),
                    orderLineItem.getQuantity())
            ).collect(toList());
    }
}

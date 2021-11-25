package kitchenpos.order.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.exception.InvalidMenuException;
import kitchenpos.order.exception.InvalidOrderException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.ui.dto.request.OrderLineItemRequestDto;
import kitchenpos.order.ui.dto.request.OrderRequestDto;
import kitchenpos.order.ui.dto.request.OrderStatusRequestDto;
import kitchenpos.order.ui.dto.response.OrderLineItemResponseDto;
import kitchenpos.order.ui.dto.response.OrderResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(
        MenuRepository menuRepository,
        OrderRepository orderRepository,
        OrderValidator orderValidator
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponseDto create(final OrderRequestDto orderRequestDto) {
        final List<OrderLineItem> orderLineItems = toOrderLineItems(orderRequestDto);

        final Order order = new Order(orderRequestDto.getOrderTableId(), orderLineItems);
        order.validate(orderValidator);

        final Order created = orderRepository.save(order);
        return toOrderResponseDto(created);
    }

    private List<OrderLineItem> toOrderLineItems(OrderRequestDto orderRequestDto) {
        List<OrderLineItemRequestDto> orderLineItems = orderRequestDto.getOrderLineItems();
        if (Objects.isNull(orderLineItems)) {
            throw new InvalidOrderException();
        }

        return orderLineItems.stream()
            .map(this::getOrderLineItem)
            .collect(toList());
    }

    private OrderLineItem getOrderLineItem(OrderLineItemRequestDto orderLineItemDto) {
        validateMenuId(orderLineItemDto);
        return new OrderLineItem(orderLineItemDto.getMenuId(), orderLineItemDto.getQuantity());
    }

    private void validateMenuId(OrderLineItemRequestDto orderLineItemDto) {
        if (!menuRepository.existsById(orderLineItemDto.getMenuId())) {
            throw new InvalidMenuException();
        }
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

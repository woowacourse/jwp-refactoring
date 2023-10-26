package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuSnapShot;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderChangeDto;
import kitchenpos.dto.OrderCreateDto;
import kitchenpos.dto.OrderDto;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(final MenuRepository menuRepository,
                        final OrderRepository orderRepository, final OrderValidator orderValidator) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderDto create(final OrderCreateDto request) {
        final Order order = getOrder(request);
        orderValidator.validate(order);

        orderRepository.save(order);
        return OrderDto.toDto(order);
    }

    private Order getOrder(final OrderCreateDto request) {
        return new Order(request.getOrderTableId(), getOrderLineItems(request));
    }

    private List<OrderLineItem> getOrderLineItems(final OrderCreateDto request) {
        return request.getOrderLineItems().stream()
                .map(orderLineItemDto -> {
                    Menu menu = menuRepository.findById(orderLineItemDto.getMenuId())
                            .orElseThrow(IllegalArgumentException::new);
                    return new OrderLineItem(menu.getId(), MenuSnapShot.make(menu), orderLineItemDto.getQuantity());
                })
                .collect(Collectors.toList());
    }

    public List<OrderDto> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(OrderDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDto changeOrderStatus(final Long orderId, final OrderChangeDto request) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        order.changeOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));

        return OrderDto.toDto(order);
    }
}

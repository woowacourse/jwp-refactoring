package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.MenuSnapShot;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.application.dto.OrderChangeDto;
import kitchenpos.order.application.dto.OrderCreateDto;
import kitchenpos.order.application.dto.OrderDto;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.repository.OrderRepository;
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
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));
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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
        order.changeOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));

        return OrderDto.toDto(order);
    }
}

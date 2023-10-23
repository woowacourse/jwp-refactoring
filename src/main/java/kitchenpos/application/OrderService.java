package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderChangeDto;
import kitchenpos.dto.OrderCreateDto;
import kitchenpos.dto.OrderDto;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderService(final MenuRepository menuRepository, final OrderTableRepository orderTableRepository,
                        final OrderRepository orderRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderDto create(final OrderCreateDto request) {
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        final Order order = new Order(orderTable);
        order.addOrderLineItems(getOrderLineItems(request));

        orderRepository.save(order);

        return OrderDto.toDto(order);
    }

    private List<OrderLineItem> getOrderLineItems(final OrderCreateDto request) {
        return request.getOrderLineItems().stream()
                .map(dto -> {
                    Menu menu = menuRepository.findById(dto.getMenuId())
                            .orElseThrow(IllegalArgumentException::new);
                    return new OrderLineItem(menu, dto.getQuantity());
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

package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.application.dto.OrderCreationDto;
import kitchenpos.order.application.dto.OrderDto;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.OrderedMenuRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderedMenu;
import kitchenpos.common.Price;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderedMenuRepository orderedMenuRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository,
            final OrderedMenuRepository orderedMenuRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderedMenuRepository = orderedMenuRepository;
    }

    @Transactional
    public OrderDto create(final OrderCreationDto orderCreationDto) {
        List<OrderLineItem> orderLineItems = createOrderedMenuAndGetOrderLineItems(orderCreationDto);
        final Order order = OrderCreationDto.toEntity(orderCreationDto);
        validateOrderTable(order);

        final Order savedOrder = orderRepository.save(
                new Order(order.getOrderTableId(), OrderStatus.COOKING, LocalDateTime.now(), orderLineItems));

        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = orderLineItems.stream()
                .map(orderLineItem -> orderLineItemRepository.save(
                        new OrderLineItem(orderId, orderLineItem.getOrderedMenuId(), orderLineItem.getQuantity())))
                .collect(Collectors.toList());

        return OrderDto.from(savedOrder.saveOrderLineItems(savedOrderLineItems));
    }

    private List<OrderLineItem> createOrderedMenuAndGetOrderLineItems(final OrderCreationDto orderCreationDto) {
        return orderCreationDto.getOrderLineItems()
                .stream()
                .map(orderLineItemDto -> {
                    final Menu menu = menuRepository.findById(orderLineItemDto.getMenuId())
                            .orElseThrow(IllegalArgumentException::new);
                    final OrderedMenu orderedMenu = orderedMenuRepository.save(
                            new OrderedMenu(menu.getName(), new Price(menu.getPrice())));
                    return new OrderLineItem(orderedMenu.getId(), orderLineItemDto.getQuantity());
                })
                .collect(Collectors.toList());
    }

    private void validateOrderTable(final Order order) {
        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional(readOnly = true)
    public List<OrderDto> getOrders() {
        return orderRepository.findAll()
                .stream()
                .map(order -> order.saveOrderLineItems(orderLineItemRepository.findAllByOrderId(order.getId())))
                .map(OrderDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDto changeOrderStatus(final Long orderId, final String orderStatus) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        validateOrderStatus(savedOrder);

        final Order order = orderRepository.save(new Order(savedOrder.getId(),
                savedOrder.getOrderTableId(),
                OrderStatus.valueOf(orderStatus),
                savedOrder.getOrderedTime(),
                orderLineItemRepository.findAllByOrderId(orderId)));

        return OrderDto.from(order);
    }

    private void validateOrderStatus(final Order order) {
        if (order.isInCompletionStatus()) {
            throw new IllegalArgumentException();
        }
    }
}

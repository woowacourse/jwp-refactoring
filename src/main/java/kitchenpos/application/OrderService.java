package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderLineItemRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.Order.Builder;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderDto;
import kitchenpos.dto.OrderLineItemDto;
import kitchenpos.exception.CustomException;
import kitchenpos.exception.ExceptionType;
import kitchenpos.menu.service.MenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuService menuService;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
        final MenuService menuService,
        final OrderRepository orderRepository,
        final OrderLineItemRepository orderLineItemRepository,
        final OrderTableRepository orderTableRepository
    ) {
        this.menuService = menuService;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderDto create(final OrderDto orderDto) {
        List<OrderLineItem> orderLineItems = orderDto.getOrderLineItemDtos()
                                                     .stream()
                                                     .map(this::toOrderLineItem)
                                                     .collect(toList());

        Long orderTableId = orderDto.getOrderTableId();
        OrderTable foundOrderTable = orderTableRepository.findById(orderTableId)
                                                         .orElseThrow(() -> new CustomException(ExceptionType.ORDER_TABLE_NOT_FOUND, String.valueOf(orderTableId)));

        Order order = new Builder()
            .orderTable(foundOrderTable)
            .orderStatus(OrderStatus.COOKING)
            .orderedTime(LocalDateTime.now())
            .orderLineItems(orderLineItems)
            .build();

        Order savedOrder = orderRepository.save(order);
        return OrderDto.from(savedOrder);
    }

    private OrderLineItem toOrderLineItem(OrderLineItemDto orderLineItemDto) {
        Menu menu = menuService.getById(orderLineItemDto.getMenuId());
        return new OrderLineItem.Builder()
            .menu(menu)
            .quantity(orderLineItemDto.getQuantity())
            .build();
    }

    public List<OrderDto> list() {
        final List<OrderDto> orderDtos = orderRepository.findAll()
                                                        .stream().map(OrderDto::from)
                                                        .collect(toList());

        for (final OrderDto orderDto : orderDtos) {
            List<OrderLineItemDto> orderLineItemDtos = orderLineItemRepository.findAllByOrderId(orderDto.getId())
                                                                              .stream()
                                                                              .map(OrderLineItemDto::from)
                                                                              .collect(toList());
            orderDto.setOrderLineItemDtos(orderLineItemDtos);
        }

        return orderDtos;
    }

    @Transactional
    public OrderDto changeOrderStatus(final Long orderId, final OrderDto orderDto) {
        final Order foundOrder = orderRepository.findById(orderId)
                                                .orElseThrow(IllegalArgumentException::new);
        final OrderStatus orderStatus = OrderStatus.valueOf(orderDto.getOrderStatus());

        foundOrder.changeOrderStatus(orderStatus);

        return OrderDto.from(foundOrder);
    }

    public List<Order> findByOrderTableIdAndOrderStatus(Long orderTableId, OrderStatus orderStatus) {
        return orderRepository.findByOrderTableIdAndOrderStatus(orderTableId, orderStatus);
    }

    public List<Order> findByOrderTableIdInAndOrderStatusIn(List<Long> orderTableId, List<OrderStatus> orderStatus) {
        return orderRepository.findByOrderTableIdInAndOrderStatusIn(orderTableId, orderStatus);
    }
}

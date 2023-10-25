package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.Order.Builder;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderDto;
import kitchenpos.dto.OrderLineItemDto;
import kitchenpos.exception.CustomException;
import kitchenpos.exception.ExceptionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuService menuService;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
        final MenuService menuService,
        final OrderDao orderDao,
        final OrderLineItemDao orderLineItemDao,
        final OrderTableDao orderTableDao
    ) {
        this.menuService = menuService;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderDto create(final OrderDto orderDto) {
        List<OrderLineItem> orderLineItems = orderDto.getOrderLineItemDtos()
                                                     .stream()
                                                     .map(this::toOrderLineItem)
                                                     .collect(toList());

        Long orderTableId = orderDto.getOrderTableId();
        OrderTable foundOrderTable = orderTableDao.findById(orderTableId)
                                                  .orElseThrow(() -> new CustomException(ExceptionType.ORDER_TABLE_NOT_FOUND, String.valueOf(orderTableId)));

        Order order = new Builder()
            .orderTable(foundOrderTable)
            .orderStatus(OrderStatus.COOKING)
            .orderedTime(LocalDateTime.now())
            .orderLineItems(orderLineItems)
            .build();

        Order savedOrder = orderDao.save(order);
        return OrderDto.from(savedOrder);
    }

    private OrderLineItem toOrderLineItem(OrderLineItemDto orderLineItemDto) {
        Menu menu = menuService.findById(orderLineItemDto.getMenuId());
        return new OrderLineItem.Builder()
            .menu(menu)
            .quantity(orderLineItemDto.getQuantity())
            .build();
    }

    public List<OrderDto> list() {
        final List<OrderDto> orderDtos = orderDao.findAll()
                                                 .stream().map(OrderDto::from)
                                                 .collect(toList());

        for (final OrderDto orderDto : orderDtos) {
            List<OrderLineItemDto> orderLineItemDtos = orderLineItemDao.findAllByOrderId(orderDto.getId())
                                                                       .stream()
                                                                       .map(OrderLineItemDto::from)
                                                                       .collect(toList());
            orderDto.setOrderLineItemDtos(orderLineItemDtos);
        }

        return orderDtos;
    }

    @Transactional
    public OrderDto changeOrderStatus(final Long orderId, final OrderDto orderDto) {
        final Order foundOrder = orderDao.findById(orderId)
                                         .orElseThrow(IllegalArgumentException::new);
        final OrderStatus orderStatus = OrderStatus.valueOf(orderDto.getOrderStatus());

        foundOrder.changeOrderStatus(orderStatus);

        return OrderDto.from(foundOrder);
    }
}

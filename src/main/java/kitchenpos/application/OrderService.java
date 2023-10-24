package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderDto;
import kitchenpos.dto.OrderLineItemDto;
import kitchenpos.domain.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class OrderService {

    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
        final MenuDao menuDao,
        final OrderDao orderDao,
        final OrderLineItemDao orderLineItemDao,
        final OrderTableDao orderTableDao
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderDto create(final OrderDto orderDto) {
        final List<OrderLineItemDto> orderLineItemDtos = orderDto.getOrderLineItemDtos();

        if (CollectionUtils.isEmpty(orderLineItemDtos)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItemDtos.stream()
                                                    .map(OrderLineItemDto::getMenuId)
                                                    .collect(toList());

        if (orderLineItemDtos.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        orderDto.setId(null);

        final OrderTable foundOrderTable = orderTableDao.findById(orderDto.getOrderTableId())
                                                        .orElseThrow(IllegalArgumentException::new);

        if (foundOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        orderDto.setOrderTableId(foundOrderTable.getId());
        orderDto.setOrderStatus(OrderStatus.COOKING.name());
        orderDto.setOrderedTime(LocalDateTime.now());

        Order savedOrder = orderDao.save(toEntity(orderDto));
        final OrderDto savedOrderDto = OrderDto.from(savedOrder);

        final List<OrderLineItemDto> savedOrderLineItemDtos = new ArrayList<>();
        for (final OrderLineItemDto orderLineItemDto : orderLineItemDtos) {
            orderLineItemDto.setOrderId(savedOrder.getId());
            OrderLineItem savedOrderLineItem = orderLineItemDao.save(toEntity(orderLineItemDto));
            savedOrderLineItemDtos.add(OrderLineItemDto.from(savedOrderLineItem));
        }
        savedOrderDto.setOrderLineItemDtos(savedOrderLineItemDtos);

        return savedOrderDto;
    }

    private Order toEntity(OrderDto orderDto) {
        OrderTable orderTable = orderTableDao.findById(orderDto.getOrderTableId())
                                             .orElseThrow(IllegalArgumentException::new);
        return new Order(
            orderDto.getId(),
            orderTable,
            OrderStatus.valueOf(orderDto.getOrderStatus()),
            orderDto.getOrderedTime(),
            null
        );
    }

    private OrderLineItem toEntity(OrderLineItemDto orderLineItemDto) {
        Order order = orderDao.findById(orderLineItemDto.getOrderId())
                              .orElseThrow(IllegalArgumentException::new);
        Menu menu = menuDao.findById(orderLineItemDto.getMenuId())
                           .orElseThrow(IllegalArgumentException::new);
        return new OrderLineItem(
            orderLineItemDto.getSeq(),
            order,
            menu,
            orderLineItemDto.getQuantity()
        );
    }

    public List<OrderDto> list() {
        final List<OrderDto> orderDtos = orderDao.findAll()
                                                 .stream().map(OrderDto::from)
                                                 .collect(toList());

        for (final OrderDto orderDto : orderDtos) {
            List<OrderLineItemDto> orderLineItemDtos = orderLineItemDao.findAllByOrderId(
                                                                           orderDto.getId())
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

        if (Objects.equals(OrderStatus.COMPLETION, foundOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(orderDto.getOrderStatus());
        foundOrder.changeOrderStatus(orderStatus);

        return OrderDto.from(foundOrder);
    }
}

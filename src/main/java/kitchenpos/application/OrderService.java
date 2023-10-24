package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dto.OrderDto;
import kitchenpos.dto.OrderLineItemDto;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderTableDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        final List<OrderLineItemDto> orderLineItemDtos = orderDto.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItemDtos)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItemDtos.stream()
                                                    .map(OrderLineItemDto::getMenuId)
                                                    .collect(Collectors.toList());

        if (orderLineItemDtos.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        orderDto.setId(null);

        final OrderTableDto orderTableDto = orderTableDao.findById(orderDto.getOrderTableId())
                                                         .orElseThrow(IllegalArgumentException::new);

        if (orderTableDto.isEmpty()) {
            throw new IllegalArgumentException();
        }

        orderDto.setOrderTableId(orderTableDto.getId());
        orderDto.setOrderStatus(OrderStatus.COOKING.name());
        orderDto.setOrderedTime(LocalDateTime.now());

        final OrderDto savedOrderDto = orderDao.save(orderDto);

        final Long orderId = savedOrderDto.getId();
        final List<OrderLineItemDto> savedOrderLineItemDtos = new ArrayList<>();
        for (final OrderLineItemDto orderLineItemDto : orderLineItemDtos) {
            orderLineItemDto.setOrderId(orderId);
            savedOrderLineItemDtos.add(orderLineItemDao.save(orderLineItemDto));
        }
        savedOrderDto.setOrderLineItems(savedOrderLineItemDtos);

        return savedOrderDto;
    }

    public List<OrderDto> list() {
        final List<OrderDto> orderDtos = orderDao.findAll();

        for (final OrderDto orderDto : orderDtos) {
            orderDto.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderDto.getId()));
        }

        return orderDtos;
    }

    @Transactional
    public OrderDto changeOrderStatus(final Long orderId, final OrderDto orderDto) {
        final OrderDto savedOrderDto = orderDao.findById(orderId)
                                               .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrderDto.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(orderDto.getOrderStatus());
        savedOrderDto.setOrderStatus(orderStatus.name());

        orderDao.save(savedOrderDto);

        savedOrderDto.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return savedOrderDto;
    }
}

package kitchenpos.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.menu.domain.MenuDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderEditRequest;
import kitchenpos.order.dto.OrderLineItemDto;
import kitchenpos.table.domain.Table;
import kitchenpos.table.domain.TableRepository;

@Service
public class OrderService {
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final TableRepository tableRepository;

    public OrderService(
        final MenuDao menuDao,
        final OrderDao orderDao,
        final OrderLineItemDao orderLineItemDao,
        final TableRepository tableRepository
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.tableRepository = tableRepository;
    }

    @Transactional
    public Long create(final OrderCreateRequest request) {
        List<OrderLineItemDto> orderLineItemDtos = request.getOrderLineItemDtos();

        if (CollectionUtils.isEmpty(orderLineItemDtos)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItemDtos.stream()
            .map(OrderLineItemDto::getMenuId)
            .collect(Collectors.toList());

        if (orderLineItemDtos.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final Table table = tableRepository.findById(request.getTableId())
            .orElseThrow(IllegalArgumentException::new);

        if (table.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Order order = new Order(table.getId(), OrderStatus.COOKING.name());

        final Order savedOrder = orderDao.save(order);

        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (OrderLineItemDto orderLineItemDto : orderLineItemDtos) {
            OrderLineItem orderLineItem = new OrderLineItem(orderId, orderLineItemDto.getMenuId(),
                orderLineItemDto.getQuantity());
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        savedOrder.changeOrderLineItems(savedOrderLineItems);

        return savedOrder.getId();
    }

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.changeOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public void changeOrderStatus(Long orderId, OrderEditRequest request) {
        final Order savedOrder = orderDao.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        final OrderStatus orderStatus = request.getOrderStatus();
        savedOrder.changeOrderStatus(orderStatus);

        orderDao.save(savedOrder);
        savedOrder.changeOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));
    }
}

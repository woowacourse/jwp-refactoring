package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderChangeRequestDto;
import kitchenpos.application.dto.OrderCreateRequestDto;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Order create(final OrderCreateRequestDto dto) {
        final Long orderTableId = findOrderTableId(dto.getOrderTableId());
        final List<OrderLineItem> orderLineItems = getOrderLineItems(dto);

        final Order order = new Order(orderTableId, LocalDateTime.now(), orderLineItems);
        final Order savedOrder = orderDao.save(order);

        final List<OrderLineItem> savedOrderLineItems = saveOrderLineItems(orderLineItems, order.getId());

        //TODO: 뷰에 의존적, Response 만들 때 setter 제거, jpa 사용?
        savedOrder.setOrderLineItems(savedOrderLineItems);

        return savedOrder;
    }

    private List<OrderLineItem> saveOrderLineItems(List<OrderLineItem> orderLineItems, Long orderId) {
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrderId(orderId);
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        return savedOrderLineItems;
    }

    private List<OrderLineItem> getOrderLineItems(OrderCreateRequestDto dto) {
        final List<OrderLineItem> orderLineItems = dto.getOrderLineItems();
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
        return orderLineItems;
    }

    private Long findOrderTableId(Long orderTableId) {
        final OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        return orderTable.getId();
    }

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderChangeRequestDto dto) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrder.changeStatus(dto.getOrderStatus());
        orderDao.save(savedOrder);

        //TODO: 뷰에 의존적, Response 만들 때 setter 제거, jpa 사용?
        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));
        return savedOrder;
    }
}

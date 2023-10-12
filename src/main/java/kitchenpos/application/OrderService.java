package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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
    public Order create(final Order order) {
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems(); // orderLineItem 을 꺼내온다, 여기에는 menu 와 quantity 가 명시되어 있어 얼마나 시킬지 들어있다.

        if (CollectionUtils.isEmpty(orderLineItems)) { // orderLineImtes 가 없는 경우 예외가 발생한다.
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) { // menu 가 하나라도 존재하지 않으면 안된다.
            throw new IllegalArgumentException();
        }

        order.setId(null); // id 를 null 로 설정

        final OrderTable orderTable = orderTableDao.findById(order.getOrderTableId()) // order 의 id 를 기반으로 order table 를 가져온다.
                .orElseThrow(IllegalArgumentException::new); // 즉, order table 은 정말 table 그리고 거기서 발생한 order 가 order 이다.

        if (orderTable.isEmpty()) { // order table 이 주문이 불가능한 상태이면 주문 불가이다.
            throw new IllegalArgumentException();
        }

        order.setOrderTableId(orderTable.getId()); // 이건 말이 안되는 코드인 것 같은데
        order.setOrderStatus(OrderStatus.COOKING.name()); // 현재 요리 중으로 바꿈
        order.setOrderedTime(LocalDateTime.now()); // 그리고 현재 order 한 것으로 바꿈

        final Order savedOrder = orderDao.save(order); // order 를 저장

        final Long orderId = savedOrder.getId(); // 저장한 order 의 id 를 꺼낸다.
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrderId(orderId);
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        savedOrder.setOrderLineItems(savedOrderLineItems); // 여기서 order id 설정하고 orderLineItem 까지 저장

        return savedOrder;
    }

    public List<Order> list() { // 모든 order 를 반환
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) { // orderId 와 order 의 상태가 주어진다.
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new); // 이미 존재하는 order 여야한다.

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) { // 이미 식사 완료 상태이면 바꿀 수 없다.
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus.name());

        orderDao.save(savedOrder); // 상태를 변경한 후 저장한다.

        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId)); // 다시 order line item 을 가져와서 반환한다.

        return savedOrder;
    }
}

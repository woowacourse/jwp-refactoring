package kitchenpos.fixture;

import static kitchenpos.fixture.OrderTableFixture.네명인_테이블;
import static kitchenpos.fixture.OrderTableFixture.두명인_테이블;
import static kitchenpos.fixture.OrderTableFixture.세명인_테이블;
import static kitchenpos.fixture.OrderTableFixture.한명인_테이블;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.InMemoryOrderDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public class OrderFixture {

    public static final Long 조리중인_첫번째테이블_주문 = 1L;
    public static final Long 식사중인_두번째테이블_주문 = 2L;
    public static final Long 완료된_세번째테이블_주문 = 3L;
    public static final Long 완료된_네번째테이블_주문 = 4L;

    private final OrderDao orderDao;
    private List<Order> fixtures;

    public OrderFixture(final OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public static OrderFixture setUp() {
        final OrderFixture orderFixture = new OrderFixture(new InMemoryOrderDao());
        orderFixture.fixtures = orderFixture.createOrders();
        return orderFixture;
    }

    public static Order createOrder(final Long orderTableId, final String orderStatus, final LocalDateTime createTime) {
        final Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(createTime);
        return order;
    }

    public static Order createOrder(final Long orderTableId) {
        final Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        return order;
    }

    public static Order createOrder(final Long orderTableId, final String orderStatus) {
        final Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(LocalDateTime.now());
        return order;
    }

    private List<Order> createOrders() {
        return List.of(
                saveOrder(한명인_테이블, OrderStatus.COOKING),
                saveOrder(두명인_테이블, OrderStatus.MEAL),
                saveOrder(세명인_테이블, OrderStatus.COMPLETION),
                saveOrder(네명인_테이블, OrderStatus.COMPLETION)
        );
    }

    private Order saveOrder(final Long orderTableId, final OrderStatus orderStatus) {
        final Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus.name());
        order.setOrderedTime(LocalDateTime.now());
        return orderDao.save(order);
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    public List<Order> getFixtures() {
        return fixtures;
    }
}

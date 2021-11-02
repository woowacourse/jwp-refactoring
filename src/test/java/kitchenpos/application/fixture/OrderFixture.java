package kitchenpos.application.fixture;

import static kitchenpos.application.fixture.OrderTableFixture.네명있는_테이블;
import static kitchenpos.application.fixture.OrderTableFixture.다섯명있는_테이블;
import static kitchenpos.application.fixture.OrderTableFixture.두명있는_테이블;
import static kitchenpos.application.fixture.OrderTableFixture.세명있는_테이블;
import static kitchenpos.application.fixture.OrderTableFixture.여섯명있는_테이블;
import static kitchenpos.application.fixture.OrderTableFixture.일곱명있는_테이블;
import static kitchenpos.application.fixture.OrderTableFixture.한명있는_테이블;
import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.application.dao.TestOrderDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public class OrderFixture {

    public static final Long 조리중인_첫번째테이블_주문 = 1L;
    public static final Long 식사중인_두번째테이블_주문 = 2L;
    public static final Long 완료된_세번째테이블_주문 = 3L;
    public static final Long 네번째테이블_주문 = 4L;
    public static final Long 다섯번째테이블_주문 = 5L;
    public static final Long 여섯번째테이블_주문 = 6L;
    public static final Long 일곱번째테이블_주문 = 7L;

    private final OrderDao orderDao;
    private List<Order> fixtures;

    private OrderFixture(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public static OrderFixture createFixture() {
        OrderFixture orderFixture = new OrderFixture(new TestOrderDao());
        orderFixture.fixtures = orderFixture.createOrders();
        return orderFixture;
    }

    private List<Order> createOrders() {
        return Arrays.asList(
            saveOrder(한명있는_테이블, COOKING),
            saveOrder(두명있는_테이블, MEAL),
            saveOrder(세명있는_테이블, COMPLETION),
            saveOrder(네명있는_테이블, COOKING),
            saveOrder(다섯명있는_테이블, MEAL),
            saveOrder(여섯명있는_테이블, COOKING),
            saveOrder(일곱명있는_테이블, COMPLETION)
        );
    }

    private Order saveOrder(Long orderTableId, OrderStatus orderStatus) {
        Order order = new Order();
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

package kitchenpos.fixture;

import static kitchenpos.fixture.MenuFixture.맵슐랭;
import static kitchenpos.fixture.MenuFixture.허니콤보;
import static kitchenpos.fixture.OrderFixture.식사중인_두번째테이블_주문;
import static kitchenpos.fixture.OrderFixture.완료된_세번째테이블_주문;
import static kitchenpos.fixture.OrderFixture.조리중인_첫번째테이블_주문;

import java.util.List;
import kitchenpos.dao.InMemoryOrderLineItemDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static final Long 첫번째테이블_맵슐랭_한개_주문 = 1L;
    public static final Long 첫번째테이블_허니콤보_두개_주문 = 2L;
    public static final Long 두번째테이블_맵슐랭_한개_주문 = 3L;
    public static final Long 세번째테이블_맵슐랭_세개_주문 = 4L;

    private final OrderLineItemDao orderLineItemDao;
    private List<OrderLineItem> fixtures;

    public OrderLineItemFixture(final OrderLineItemDao orderLineItemDao) {
        this.orderLineItemDao = orderLineItemDao;
    }

    public static OrderLineItemFixture setUp() {
        OrderLineItemFixture orderLineItemFixture = new OrderLineItemFixture(new InMemoryOrderLineItemDao());
        orderLineItemFixture.fixtures = orderLineItemFixture.createOrders();
        return orderLineItemFixture;
    }

    public static OrderLineItem createOrderLineItem(final Long menuId, final Long quantity) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    private List<OrderLineItem> createOrders() {
        return List.of(
                saveOrderLineItem(조리중인_첫번째테이블_주문, 맵슐랭, 1),
                saveOrderLineItem(조리중인_첫번째테이블_주문, 허니콤보, 2),
                saveOrderLineItem(식사중인_두번째테이블_주문, 맵슐랭, 1),
                saveOrderLineItem(완료된_세번째테이블_주문, 허니콤보, 3)
        );
    }

    private OrderLineItem saveOrderLineItem(Long orderId, Long menuId, int quantity) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItemDao.save(orderLineItem);
    }

    public OrderLineItemDao getOrderLineItemDao() {
        return orderLineItemDao;
    }

    public List<OrderLineItem> getFixtures() {
        return fixtures;
    }
}

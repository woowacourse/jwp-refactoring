package kitchenpos.application.fixture;

import static kitchenpos.application.fixture.MenuFixture.반반치킨;
import static kitchenpos.application.fixture.MenuFixture.양념치킨;
import static kitchenpos.application.fixture.MenuFixture.통구이;
import static kitchenpos.application.fixture.MenuFixture.후라이드치킨;
import static kitchenpos.application.fixture.OrderFixture.식사중인_두번째테이블_주문;
import static kitchenpos.application.fixture.OrderFixture.완료된_세번째테이블_주문;
import static kitchenpos.application.fixture.OrderFixture.조리중인_첫번째테이블_주문;

import java.util.Arrays;
import java.util.List;
import kitchenpos.application.dao.TestOrderLineItemDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static final Long 첫번째테이블_후라이드_한개_주문항목 = 1L;
    public static final Long 첫번째테이블_양념치킨_두개_주문항목 = 2L;
    public static final Long 두번째테이블_후라이드_한개_주문항목 = 3L;
    public static final Long 두번째테이블_반반치킨_세개_주문항목 = 4L;
    public static final Long 세번째테이블_통구이_한개_주문항목 = 5L;

    private final OrderLineItemDao orderLineItemDao;
    private List<OrderLineItem> fixtures;

    private OrderLineItemFixture(OrderLineItemDao orderLineItem) {
        this.orderLineItemDao = orderLineItem;
    }

    public static OrderLineItemFixture createFixture() {
        OrderLineItemFixture orderLineItemFixture = new OrderLineItemFixture(
            new TestOrderLineItemDao());
        orderLineItemFixture.fixtures = orderLineItemFixture.createOrders();
        return orderLineItemFixture;
    }

    private List<OrderLineItem> createOrders() {
        return Arrays.asList(
            saveOrderLineItem(조리중인_첫번째테이블_주문, 후라이드치킨, 1),
            saveOrderLineItem(조리중인_첫번째테이블_주문, 양념치킨, 2),
            saveOrderLineItem(식사중인_두번째테이블_주문, 후라이드치킨, 1),
            saveOrderLineItem(식사중인_두번째테이블_주문, 반반치킨, 3),
            saveOrderLineItem(완료된_세번째테이블_주문, 통구이, 1)
        );
    }

    private OrderLineItem saveOrderLineItem(Long orderId, Long menuId, int quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
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

package kitchenpos.dao;

import static kitchenpos.fixture.OrderFixture.ORDER_FIXTURE_1;
import static kitchenpos.fixture.OrderFixture.ORDER_FIXTURE_2;
import static kitchenpos.fixture.OrderLineItemFixture.ORDER_LINE_ITEM_FIXTURE_1;
import static kitchenpos.fixture.OrderLineItemFixture.ORDER_LINE_ITEM_FIXTURE_2;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderLineItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderLineItemDaoTest {

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private OrderDao orderDao;

    @Test
    void save() {
        orderDao.save(ORDER_FIXTURE_1);
        OrderLineItem orderLineItem = ORDER_LINE_ITEM_FIXTURE_1;

        OrderLineItem persistOrderLineItem = orderLineItemDao.save(orderLineItem);

        assertThat(persistOrderLineItem.getSeq()).isNotNull();
    }

    @Test
    void findById() {
        orderDao.save(ORDER_FIXTURE_1);
        OrderLineItem persistOrderLineItem = orderLineItemDao.save(ORDER_LINE_ITEM_FIXTURE_1);

        OrderLineItem findOrderLineItem = orderLineItemDao.findById(persistOrderLineItem.getSeq()).get();

        assertThat(findOrderLineItem).isEqualToComparingFieldByField(persistOrderLineItem);
    }

    @Test
    void findAll() {
        orderDao.save(ORDER_FIXTURE_1);
        orderLineItemDao.save(ORDER_LINE_ITEM_FIXTURE_1);
        orderDao.save(ORDER_FIXTURE_2);
        orderLineItemDao.save(ORDER_LINE_ITEM_FIXTURE_2);

        List<OrderLineItem> orderLineItems = orderLineItemDao.findAll();
        List<Long> quantities = orderLineItems.stream()
            .map(OrderLineItem::getQuantity)
            .collect(Collectors.toList());

        assertThat(quantities)
            .contains(ORDER_LINE_ITEM_FIXTURE_1.getQuantity(), ORDER_LINE_ITEM_FIXTURE_2.getQuantity());
    }

    @Test
    void findAllByOrderId() {
        orderDao.save(ORDER_FIXTURE_1);
        orderLineItemDao.save(ORDER_LINE_ITEM_FIXTURE_1);
        orderDao.save(ORDER_FIXTURE_2);
        orderLineItemDao.save(ORDER_LINE_ITEM_FIXTURE_2);

        List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(1L);
        List<Long> quantities = orderLineItems.stream()
            .map(OrderLineItem::getQuantity)
            .collect(Collectors.toList());

        assertThat(quantities)
            .contains(ORDER_LINE_ITEM_FIXTURE_1.getQuantity());
    }
}

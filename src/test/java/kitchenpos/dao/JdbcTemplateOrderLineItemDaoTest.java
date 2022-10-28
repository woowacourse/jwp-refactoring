package kitchenpos.dao;

import static kitchenpos.fixture.OrderFixture.getOrderLineItem;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.fixture.OrderFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JdbcTemplateOrderLineItemDaoTest extends JdbcTemplateTest{

    private OrderLineItemDao orderLineItemDao;
    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        orderDao = new JdbcTemplateOrderDao(dataSource);
        orderLineItemDao = new JdbcTemplateOrderLineItemDao(dataSource);
    }

    @Test
    @DisplayName("데이터 베이스에 저장할 경우 id 값을 가진 엔티티로 반환한다.")
    void save() {
        final OrderLineItem saved = saveOrderLineItem();
        assertThat(saved.getSeq()).isNotNull();
    }

    @Test
    @DisplayName("목록을 조회한다.")
    void list() {
        // given
        saveOrderLineItem();

        // when
        final List<OrderLineItem> actual = orderLineItemDao.findAll();

        // then
        assertThat(actual).hasSize(1);
    }

    private OrderLineItem saveOrderLineItem() {
        final Order saveOrder = orderDao.save(OrderFixture.getOrderRequest());
        return orderLineItemDao.save(getOrderLineItem(saveOrder.getId()));
    }
}

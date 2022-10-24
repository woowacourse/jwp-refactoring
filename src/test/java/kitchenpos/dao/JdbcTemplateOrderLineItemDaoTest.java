package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
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
        final Order saveOrder = orderDao.save(getOrder());
        final OrderLineItem saved = orderLineItemDao.save(getOrderLineItem(saveOrder.getId()));
        assertThat(saved.getSeq()).isNotNull();
    }
}

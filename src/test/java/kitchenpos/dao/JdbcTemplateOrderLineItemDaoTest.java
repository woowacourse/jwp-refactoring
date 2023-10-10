package kitchenpos.dao;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Import(value = {JdbcTemplateOrderLineItemDao.class, JdbcTemplateOrderDao.class})
@JdbcTest
class JdbcTemplateOrderLineItemDaoTest {

    @Autowired
    private JdbcTemplateOrderLineItemDao jdbcTemplateOrderLineItemDao;

    @Autowired
    private JdbcTemplateOrderDao jdbcTemplateOrderDao;

    private Order order;

    @BeforeEach
    void setup() {
        order = new Order();
        order.setOrderTableId(1L);
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderLineItems(List.of());
        order.setOrderedTime(LocalDateTime.of(2023, 03, 03, 3, 30, 30));
        order = jdbcTemplateOrderDao.save(order);
    }


    @Test
    void 아이디를_기준으로_조회한다() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(2L);
        orderLineItem.setQuantity(1);
        orderLineItem.setMenuId(1L);
        jdbcTemplateOrderLineItemDao.save(orderLineItem);

        // when
        Optional<OrderLineItem> result = jdbcTemplateOrderLineItemDao.findById(1L);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).isPresent();
            softly.assertThat(result.get().getOrderId()).isEqualTo(order.getId());
        });
    }

    @Test
    void 주문_id를_가지고_모두_조회한다() {
        // when
        List<OrderLineItem> result = jdbcTemplateOrderLineItemDao.findAll();

        // then
        assertThat(result).hasSize(0);
    }
}

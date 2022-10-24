package kitchenpos.dao;

import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

@DataJdbcTest
class JdbcTemplateOrderLineItemDaoTest {

    private final OrderLineItemDao orderLineItemDao;
    private final OrderDao orderDao;

    private Long orderId;

    @Autowired
    JdbcTemplateOrderLineItemDaoTest(final DataSource dataSource) {
        this.orderLineItemDao = new JdbcTemplateOrderLineItemDao(dataSource);
        this.orderDao = new JdbcTemplateOrderDao(dataSource);
    }

    @BeforeEach
    void setup() {
        this.orderId = orderDao.save(new Order(null, 1L, COOKING.name(), LocalDateTime.now(), new ArrayList<>())).getId();
    }

    @Test
    void 저장한다() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(1);
        orderLineItem.setMenuId(1L);
        orderLineItem.setOrderId(orderId);

        // when
        OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItem);

        // then
        Assertions.assertAll(
                () -> assertThat(savedOrderLineItem.getSeq()).isNotNull(),
                () -> assertThat(savedOrderLineItem.getQuantity()).isEqualTo(1),
                () -> assertThat(savedOrderLineItem.getMenuId()).isEqualTo(1L),
                () -> assertThat(savedOrderLineItem.getOrderId()).isEqualTo(orderId)
        );
    }

    @Test
    void ID로_조회한다() {
        //before
        orderDao.save(new Order(null, 1L, COOKING.name(), LocalDateTime.now(), new ArrayList<>()));

        // given
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(1);
        orderLineItem.setMenuId(1L);
        orderLineItem.setOrderId(orderId);
        OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItem);

        // when
        Optional<OrderLineItem> foundOrderLineItem = orderLineItemDao.findById(savedOrderLineItem.getSeq());
        // then
        Assertions.assertAll(
                () -> assertThat(foundOrderLineItem).isPresent(),
                () -> assertThat(foundOrderLineItem.get())
                        .usingRecursiveComparison()
                        .ignoringFields("seq")
                        .isEqualTo(orderLineItem)
        );
    }

    @Test
    void 일치하는_ID가_없을_경우_empty를_반환한다() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(1);
        orderLineItem.setMenuId(1L);
        orderLineItem.setOrderId(orderId);
        OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItem);
        Long notExistId = -1L;

        // when
        Optional<OrderLineItem> foundOrderLineItem = orderLineItemDao.findById(notExistId);

        // then
        assertThat(foundOrderLineItem).isEmpty();
    }

    @Test
    void 목록을_조회한다() {
        // given
        OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setQuantity(1);
        orderLineItem1.setMenuId(1L);
        orderLineItem1.setOrderId(orderId);
        orderLineItemDao.save(orderLineItem1);

        OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setQuantity(3);
        orderLineItem2.setMenuId(2L);
        orderLineItem2.setOrderId(orderId);
        orderLineItemDao.save(orderLineItem2);

        // when
        List<OrderLineItem> orderLineItems = orderLineItemDao.findAll();

        // then
        assertThat(orderLineItems).hasSize(2)
                .usingRecursiveComparison()
                .ignoringFields("seq")
                .isEqualTo(
                        Arrays.asList(
                                orderLineItem1, orderLineItem2
                        )
                );
    }

    @Test
    void order_id로_조회한다() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(1);
        orderLineItem.setMenuId(1L);
        orderLineItem.setOrderId(orderId);
        orderLineItemDao.save(orderLineItem);

        // when
        List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(orderId);

        // then
        assertThat(orderLineItems).hasSize(1)
                .usingRecursiveComparison()
                .ignoringFields("seq")
                .isEqualTo(
                        Arrays.asList(orderLineItem)
                );
    }
}

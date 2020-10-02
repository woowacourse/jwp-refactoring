package kitchenpos.dao;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static kitchenpos.DomainFactory.createOrderLineItem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class JdbcTemplateOrderLineItemDaoTest extends JdbcTemplateDaoTest {
    @Autowired
    private JdbcTemplateOrderLineItemDao jdbcTemplateOrderLineItemDao;

    @BeforeEach
    void setUp() {
        orderLineItemSeqs = new ArrayList<>();

        saveOrderTable(1L, 0, true);
        saveOrderTable(2L, 0, true);
        saveOrders(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now());
        saveOrders(2L, 2L, OrderStatus.COOKING.name(), LocalDateTime.now());
        saveMenuGroup(1L, "한마리메뉴");
        saveMenu(1L, "후라이드치킨", BigDecimal.valueOf(16_000), 1L);
        saveMenu(2L, "양념치킨", BigDecimal.valueOf(16_000), 1L);
    }

    @DisplayName("주문 항목 저장")
    @Test
    void saveTest() {
        OrderLineItem orderLineItem = createOrderLineItem(1L, 1L, 1L);
        OrderLineItem savedOrderLineItem = jdbcTemplateOrderLineItemDao.save(orderLineItem);
        orderLineItemSeqs.add(savedOrderLineItem.getSeq());

        assertAll(
                () -> assertThat(savedOrderLineItem.getSeq()).isNotNull(),
                () -> assertThat(savedOrderLineItem.getOrderId()).isEqualTo(orderLineItem.getOrderId()),
                () -> assertThat(savedOrderLineItem.getMenuId()).isEqualTo(orderLineItem.getMenuId()),
                () -> assertThat(savedOrderLineItem.getQuantity()).isEqualTo(orderLineItem.getQuantity())
        );
    }

    @DisplayName("seq에 해당하는 주문 항목 반환")
    @Test
    void findByIdTest() {
        OrderLineItem orderLineItem = createOrderLineItem(1L, 1L, 1L);
        OrderLineItem savedOrderLineItem = jdbcTemplateOrderLineItemDao.save(orderLineItem);

        OrderLineItem findOrderLineItem = jdbcTemplateOrderLineItemDao.findById(savedOrderLineItem.getSeq()).get();
        orderLineItemSeqs.add(findOrderLineItem.getSeq());

        assertAll(
                () -> assertThat(findOrderLineItem.getSeq()).isEqualTo(savedOrderLineItem.getSeq()),
                () -> assertThat(findOrderLineItem.getOrderId()).isEqualTo(savedOrderLineItem.getOrderId()),
                () -> assertThat(findOrderLineItem.getMenuId()).isEqualTo(savedOrderLineItem.getMenuId()),
                () -> assertThat(findOrderLineItem.getQuantity()).isEqualTo(savedOrderLineItem.getQuantity())
        );
    }

    @DisplayName("잘못된 seq 입력 시 빈 객체 반환")
    @Test
    void findByIdWithInvalidSeqTest() {
        Optional<OrderLineItem> findOrderLineItem = jdbcTemplateOrderLineItemDao.findById(0L);

        assertThat(findOrderLineItem).isEqualTo(Optional.empty());
    }

    @DisplayName("모든 주문 항목 반환")
    @Test
    void findAllTest() {
        OrderLineItem friedOrderLineItem = createOrderLineItem(1L, 1L, 1L);
        OrderLineItem sourceOrderLineItem = createOrderLineItem(2L, 2L, 1L);
        jdbcTemplateOrderLineItemDao.save(friedOrderLineItem);
        jdbcTemplateOrderLineItemDao.save(sourceOrderLineItem);

        List<OrderLineItem> allOrderLineItems = jdbcTemplateOrderLineItemDao.findAll();
        allOrderLineItems.forEach(orderLineItem -> orderLineItemSeqs.add(orderLineItem.getSeq()));

        assertThat(allOrderLineItems).hasSize(2);
    }

    @DisplayName("주문 아이디에 해당하는 주문 항목 반환")
    @Test
    void findAllByOrderIdTest() {
        OrderLineItem friedOrderLineItem = createOrderLineItem(1L, 1L, 1L);
        OrderLineItem sourceOrderLineItem = createOrderLineItem(2L, 2L, 1L);
        OrderLineItem savedFriedOrderLineItem = jdbcTemplateOrderLineItemDao.save(friedOrderLineItem);
        OrderLineItem savedSourceOrderLineItem = jdbcTemplateOrderLineItemDao.save(sourceOrderLineItem);

        List<OrderLineItem> allOrderLineItemsByOrderId = jdbcTemplateOrderLineItemDao.findAllByOrderId(1L);
        orderLineItemSeqs.add(savedFriedOrderLineItem.getSeq());
        orderLineItemSeqs.add(savedSourceOrderLineItem.getSeq());

        assertThat(allOrderLineItemsByOrderId).hasSize(1);
    }

    @AfterEach
    void tearDown() {
        deleteOrderLineItem();
    }
}
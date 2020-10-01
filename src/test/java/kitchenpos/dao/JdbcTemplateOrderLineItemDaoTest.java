package kitchenpos.dao;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/truncate.sql")
@SpringBootTest
class JdbcTemplateOrderLineItemDaoTest {
    private static final String INSERT_ORDER_TABLE = "insert into order_table (id, number_of_guests, empty) " +
            "values (:id, :number_of_guests, :empty)";
    private static final String INSERT_ORDERS = "insert into orders (id, order_table_id, order_status, ordered_time) " +
            "values (:id, :order_table_id, :order_status, :ordered_time)";
    private static final String INSERT_MENU_GROUP = "insert into menu_group (id, name) values (:id, :name)";
    private static final String INSERT_MENU = "insert into menu (id, name, price, menu_group_id) " +
            "values (:id, :name, :price, :menu_group_id)";
    private static final String DELETE_ORDER_LINE_ITEM = "delete from order_line_item where seq in (:seqs)";

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private JdbcTemplateOrderLineItemDao jdbcTemplateOrderLineItemDao;

    private List<Long> orderLineItemSeqs;

    @BeforeEach
    void setUp() {
        orderLineItemSeqs = new ArrayList<>();

        createOrderTable();
        createOrders();
        createMenuGroup();
        createMenu();
    }

    private void createOrderTable() {
        Map<String, Object> firstOrderTable = new HashMap<>();
        firstOrderTable.put("id", 1L);
        firstOrderTable.put("number_of_guests", 0);
        firstOrderTable.put("empty", true);

        Map<String, Object> secondOrderTable = new HashMap<>();
        secondOrderTable.put("id", 2L);
        secondOrderTable.put("number_of_guests", 0);
        secondOrderTable.put("empty", true);

        namedParameterJdbcTemplate.update(INSERT_ORDER_TABLE, firstOrderTable);
        namedParameterJdbcTemplate.update(INSERT_ORDER_TABLE, secondOrderTable);
    }

    private void createOrders() {
        Map<String, Object> firstOrder = new HashMap<>();
        firstOrder.put("id", 1L);
        firstOrder.put("order_table_id", 1L);
        firstOrder.put("order_status", OrderStatus.COOKING.toString());
        firstOrder.put("ordered_time", LocalDateTime.now());

        Map<String, Object> secondOrder = new HashMap<>();
        secondOrder.put("id", 2L);
        secondOrder.put("order_table_id", 2L);
        secondOrder.put("order_status", OrderStatus.MEAL.toString());
        secondOrder.put("ordered_time", LocalDateTime.now());

        namedParameterJdbcTemplate.update(INSERT_ORDERS, firstOrder);
        namedParameterJdbcTemplate.update(INSERT_ORDERS, secondOrder);
    }

    private void createMenuGroup() {
        Map<String, Object> menuGroup = new HashMap<>();
        menuGroup.put("id", 1L);
        menuGroup.put("name", "한마리메뉴");
        namedParameterJdbcTemplate.update(INSERT_MENU_GROUP, menuGroup);
    }

    private void createMenu() {
        Map<String, Object> firstMenu = new HashMap<>();
        firstMenu.put("id", 1L);
        firstMenu.put("name", "후라이드치킨");
        firstMenu.put("price", BigDecimal.valueOf(16000));
        firstMenu.put("menu_group_id", 1L);

        Map<String, Object> secondMenu = new HashMap<>();
        secondMenu.put("id", 2L);
        secondMenu.put("name", "양념치킨");
        secondMenu.put("price", BigDecimal.valueOf(16000));
        secondMenu.put("menu_group_id", 1L);

        namedParameterJdbcTemplate.update(INSERT_MENU, firstMenu);
        namedParameterJdbcTemplate.update(INSERT_MENU, secondMenu);
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

    private OrderLineItem createOrderLineItem(Long orderId, Long menuId, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
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
        Map<String, Object> params = Collections.singletonMap("seqs", orderLineItemSeqs);
        namedParameterJdbcTemplate.update(DELETE_ORDER_LINE_ITEM, params);
    }
}
package kitchenpos.dao;

import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

@DisplayName("OrderLineItem Dao 테스트")
class JdbcTemplateOrderLineItemDaoTest extends JdbcTemplateDaoTest {

    private JdbcTemplateOrderLineItemDao jdbcTemplateOrderLineItemDao;
    private JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;
    private JdbcTemplateMenuDao jdbcTemplateMenuDao;
    private JdbcTemplateOrderDao jdbcTemplateOrderDao;
    private JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;
    private JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao;

    @BeforeEach
    void setUp() {
        jdbcTemplateOrderLineItemDao = new JdbcTemplateOrderLineItemDao(dataSource);
        jdbcTemplateMenuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
        jdbcTemplateMenuDao = new JdbcTemplateMenuDao(dataSource);
        jdbcTemplateOrderDao = new JdbcTemplateOrderDao(dataSource);
        jdbcTemplateOrderTableDao = new JdbcTemplateOrderTableDao(dataSource);
        jdbcTemplateTableGroupDao = new JdbcTemplateTableGroupDao(dataSource);
    }

    @DisplayName("OrderLineItem을 저장할 때")
    @Nested
    class Save {

        @DisplayName("정상적인 OrderLineItem은 저장에 성공한다.")
        @Test
        void success() {
            // given
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroup을_생성한다());
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(OrderTable을_생성한다(2, tableGroup.getId()));
            Order order = jdbcTemplateOrderDao.save(Order를_생성한다(orderTable.getId(), COOKING.name()));
            MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(MenuGroup을_생성한다("추천 메뉴"));
            Menu menu = jdbcTemplateMenuDao.save(Menu를_생성한다("더블 치즈 버거 세트", 6_500, menuGroup.getId()));

            OrderLineItem orderLineItem = OrderLineItem을_생성한다(order.getId(), menu.getId(), 5);

            // when
            OrderLineItem savedOrderLineItem = jdbcTemplateOrderLineItemDao.save(orderLineItem);

            // then
            assertThat(jdbcTemplateOrderLineItemDao.findById(savedOrderLineItem.getSeq())).isPresent();
            assertThat(savedOrderLineItem).usingRecursiveComparison()
                .ignoringFields("seq")
                .isEqualTo(orderLineItem);
        }

        @DisplayName("orderId가 Null인 경우 예외가 발생한다.")
        @Test
        void orderIdNullException() {
            // given
            MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(MenuGroup을_생성한다("추천 메뉴"));
            Menu menu = jdbcTemplateMenuDao.save(Menu를_생성한다("더블 치즈 버거 세트", 6_500, menuGroup.getId()));

            OrderLineItem orderLineItem = OrderLineItem을_생성한다(null, menu.getId(), 5);

            // when, then
            assertThatThrownBy(() -> jdbcTemplateOrderLineItemDao.save(orderLineItem))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
        }

        @DisplayName("price가 Null인 경우 예외가 발생한다.")
        @Test
        void priceNullException() {
            // given
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroup을_생성한다());
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(OrderTable을_생성한다(2, tableGroup.getId()));
            Order order = jdbcTemplateOrderDao.save(Order를_생성한다(orderTable.getId(), COOKING.name()));

            OrderLineItem orderLineItem = OrderLineItem을_생성한다(order.getId(), null, 5);

            // when, then
            assertThatThrownBy(() -> jdbcTemplateOrderLineItemDao.save(orderLineItem))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
        }
    }

    @DisplayName("ID를 통해 OrderLineItem을 조회할 때")
    @Nested
    class FindById {

        @DisplayName("ID가 존재한다면 OrderLineItem 조회에 성공한다.")
        @Test
        void present() {
            // given
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroup을_생성한다());
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(OrderTable을_생성한다(2, tableGroup.getId()));
            Order order = jdbcTemplateOrderDao.save(Order를_생성한다(orderTable.getId(), COOKING.name()));
            MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(MenuGroup을_생성한다("추천 메뉴"));
            Menu menu = jdbcTemplateMenuDao.save(Menu를_생성한다("더블 치즈 버거 세트", 6_500, menuGroup.getId()));

            OrderLineItem savedOrderLineItem = jdbcTemplateOrderLineItemDao.save(OrderLineItem을_생성한다(order.getId(), menu.getId(), 4));

            // when
            Optional<OrderLineItem> foundOrderLineItem = jdbcTemplateOrderLineItemDao.findById(savedOrderLineItem.getSeq());

            // then
            assertThat(foundOrderLineItem).isPresent();
            assertThat(foundOrderLineItem.get()).usingRecursiveComparison()
                .isEqualTo(savedOrderLineItem);
        }

        @DisplayName("ID가 존재하지 않는다면 OrderLineItem 조회에 실패한다.")
        @Test
        void isNotPresent() {
            // when
            Optional<OrderLineItem> foundOrderLineItem = jdbcTemplateOrderLineItemDao.findById(Long.MAX_VALUE);

            // then
            assertThat(foundOrderLineItem).isNotPresent();
        }
    }

    @DisplayName("모든 OrderLineItem을 조회한다.")
    @Test
    void findAll() {
        // given
        List<OrderLineItem> beforeSavedOrderTables = jdbcTemplateOrderLineItemDao.findAll();

        TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroup을_생성한다());
        OrderTable orderTable = jdbcTemplateOrderTableDao.save(OrderTable을_생성한다(2, tableGroup.getId()));
        Order order = jdbcTemplateOrderDao.save(Order를_생성한다(orderTable.getId(), COOKING.name()));
        MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(MenuGroup을_생성한다("추천 메뉴"));
        Menu menu = jdbcTemplateMenuDao.save(Menu를_생성한다("더블 치즈 버거 세트", 6_500, menuGroup.getId()));

        beforeSavedOrderTables.add(jdbcTemplateOrderLineItemDao.save(OrderLineItem을_생성한다(order.getId(), menu.getId(), 1)));
        beforeSavedOrderTables.add(jdbcTemplateOrderLineItemDao.save(OrderLineItem을_생성한다(order.getId(), menu.getId(), 5)));
        beforeSavedOrderTables.add(jdbcTemplateOrderLineItemDao.save(OrderLineItem을_생성한다(order.getId(), menu.getId(), 13)));

        // when
        List<OrderLineItem> afterSavedOrderTables = jdbcTemplateOrderLineItemDao.findAll();

        // then
        assertThat(afterSavedOrderTables).hasSize(beforeSavedOrderTables.size());
        assertThat(afterSavedOrderTables).usingRecursiveComparison()
            .isEqualTo(beforeSavedOrderTables);
    }

    @DisplayName("OrderId에 해당하는 모든 OrderLineItem을 조회한다.")
    @Test
    void findAllByOrderId() {
        // given
        TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroup을_생성한다());
        OrderTable orderTable = jdbcTemplateOrderTableDao.save(OrderTable을_생성한다(2, tableGroup.getId()));
        Order order = jdbcTemplateOrderDao.save(Order를_생성한다(orderTable.getId(), COOKING.name()));
        MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(MenuGroup을_생성한다("추천 메뉴"));
        Menu menu = jdbcTemplateMenuDao.save(Menu를_생성한다("더블 치즈 버거 세트", 6_500, menuGroup.getId()));

        OrderLineItem orderLineItem1 = jdbcTemplateOrderLineItemDao.save(OrderLineItem을_생성한다(order.getId(), menu.getId(), 1));
        OrderLineItem orderLineItem2 = jdbcTemplateOrderLineItemDao.save(OrderLineItem을_생성한다(order.getId(), menu.getId(), 5));
        OrderLineItem orderLineItem3 = jdbcTemplateOrderLineItemDao.save(OrderLineItem을_생성한다(order.getId(), menu.getId(), 13));

        // when
        List<OrderLineItem> foundOrderLineItems = jdbcTemplateOrderLineItemDao.findAllByOrderId(order.getId());

        // then
        assertThat(foundOrderLineItems).extracting("seq")
            .containsExactly(orderLineItem1.getSeq(), orderLineItem2.getSeq(), orderLineItem3.getSeq());
    }

    private OrderLineItem OrderLineItem을_생성한다(Long orderId, Long menuId, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);

        return orderLineItem;
    }

    private Menu Menu를_생성한다(String name, int price, Long menuGroupId) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroupId);

        return menu;
    }

    private MenuGroup MenuGroup을_생성한다(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);

        return menuGroup;
    }

    private Order Order를_생성한다(Long orderTableId, String orderStatus) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(LocalDateTime.now());

        return order;
    }

    private OrderTable OrderTable을_생성한다(int numberOfGuests, Long tableGroupId) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setTableGroupId(tableGroupId);

        return orderTable;
    }

    private TableGroup TableGroup을_생성한다() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());

        return tableGroup;
    }
}
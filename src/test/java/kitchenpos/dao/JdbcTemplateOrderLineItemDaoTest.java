package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;

@DaoTest
class JdbcTemplateOrderLineItemDaoTest {

    private final JdbcTemplateOrderLineItemDao jdbcTemplateOrderLineItemDao;
    private final JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao;
    private final JdbcTemplateOrderDao jdbcTemplateOrderDao;
    private final JdbcTemplateMenuDao jdbcTemplateMenuDao;
    private final JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;
    private final JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;

    public JdbcTemplateOrderLineItemDaoTest(
            final JdbcTemplateOrderLineItemDao jdbcTemplateOrderLineItemDao,
            final JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao,
            final JdbcTemplateOrderDao jdbcTemplateOrderDao,
            final JdbcTemplateMenuDao jdbcTemplateMenuDao,
            final JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao,
            final JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao
    ) {
        this.jdbcTemplateOrderLineItemDao = jdbcTemplateOrderLineItemDao;
        this.jdbcTemplateTableGroupDao = jdbcTemplateTableGroupDao;
        this.jdbcTemplateOrderDao = jdbcTemplateOrderDao;
        this.jdbcTemplateMenuDao = jdbcTemplateMenuDao;
        this.jdbcTemplateMenuGroupDao = jdbcTemplateMenuGroupDao;
        this.jdbcTemplateOrderTableDao = jdbcTemplateOrderTableDao;
    }

    @Test
    void save_order_line_item() {
        // given
        final OrderLineItem savedOrderLineItem = orderLineItemFixtureFrom(3L);

        // when
        final OrderLineItem savedOrderLineItemGroup = jdbcTemplateOrderLineItemDao.save(savedOrderLineItem);

        // then
        assertThat(savedOrderLineItemGroup.getSeq()).isNotNull();
    }

    @Test
    void find_by_seq() {
        // given
        final OrderLineItem savedOrderLineItem = orderLineItemFixtureFrom(3L);
        final OrderLineItem savedOrderLineItemGroup = jdbcTemplateOrderLineItemDao.save(savedOrderLineItem);
        final Long findSeq = savedOrderLineItemGroup.getSeq();

        // when
        final Optional<OrderLineItem> findBySeq = jdbcTemplateOrderLineItemDao.findById(findSeq);

        // then
        assertThat(findBySeq).isPresent();
        assertThat(findBySeq.get())
                .usingRecursiveComparison()
                .isEqualTo(savedOrderLineItemGroup);
    }

    @Test
    void find_by_id_return_empty_when_result_doesnt_exist() {
        // given
        final long doesntExistId = 10000L;

        // when
        final Optional<OrderLineItem> findBySeq = jdbcTemplateOrderLineItemDao.findById(doesntExistId);

        // then
        assertThat(findBySeq).isEmpty();
    }

    @Test
    void find_all() {
        // given
        jdbcTemplateOrderLineItemDao.save(orderLineItemFixtureFrom(3L));
        jdbcTemplateOrderLineItemDao.save(orderLineItemFixtureFrom(5L));

        // when
        List<OrderLineItem> findAll = jdbcTemplateOrderLineItemDao.findAll();

        // then
        assertThat(findAll).hasSize(2);
    }

    @Test
    void find_all_by_order_id() {
        // given
        final OrderLineItem orderLineItem = jdbcTemplateOrderLineItemDao.save(orderLineItemFixtureFrom(3L));
        jdbcTemplateOrderLineItemDao.save(orderLineItemFixtureFrom(5L));

        // when
        final List<OrderLineItem> findAll = jdbcTemplateOrderLineItemDao.findAllByOrderId(orderLineItem.getOrderId());

        // then
        assertThat(findAll).hasSize(1);
    }

    private OrderLineItem orderLineItemFixtureFrom(final Long quantity) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(getMenuFixtureIdOf("fried-chicken", "Chicken-group"));
        orderLineItem.setOrderId(getOrderFixtureId());
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    private Long getOrderFixtureId() {
        final Order order = new Order();
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(getOrderTableFixtureIdFrom(3));
        return jdbcTemplateOrderDao.save(order).getId();
    }

    private Long getOrderTableFixtureIdFrom(final int numberOfGuests) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(false);
        orderTable.setTableGroupId(getTableGroupFixtureId());
        return jdbcTemplateOrderTableDao.save(orderTable).getId();
    }

    private Long getTableGroupFixtureId() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        return jdbcTemplateTableGroupDao.save(tableGroup).getId();
    }

    private Long getMenuFixtureIdOf(final String name, final String menuGroupName) {
        final Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(22000L));
        menu.setMenuGroupId(getMenuGroupFixtureIdFrom(menuGroupName));
        return jdbcTemplateMenuDao.save(menu).getId();
    }

    private Long getMenuGroupFixtureIdFrom(final String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return jdbcTemplateMenuGroupDao.save(menuGroup).getId();
    }
}

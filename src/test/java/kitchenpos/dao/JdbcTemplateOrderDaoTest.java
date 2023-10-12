package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;

@DaoTest
class JdbcTemplateOrderDaoTest {

    private final JdbcTemplateOrderDao jdbcTemplateOrderDao;
    private final JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;
    private final JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao;

    public JdbcTemplateOrderDaoTest(
            final JdbcTemplateOrderDao jdbcTemplateOrderDao,
            final JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao,
            final JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao
    ) {
        this.jdbcTemplateOrderDao = jdbcTemplateOrderDao;
        this.jdbcTemplateOrderTableDao = jdbcTemplateOrderTableDao;
        this.jdbcTemplateTableGroupDao = jdbcTemplateTableGroupDao;
    }

    @Test
    void save_order() {
        // given
        final Order order = menuProductFixtureOf(OrderStatus.COOKING);

        // when
        final Order savedOrder = jdbcTemplateOrderDao.save(order);

        // then
        assertThat(savedOrder.getId()).isNotNull();
    }

    @Test
    void update_when_save_order_id_exist() {
        // given
        final Order order = menuProductFixtureOf(OrderStatus.COOKING);
        final Order savedOrder = jdbcTemplateOrderDao.save(order);
        savedOrder.setOrderStatus(String.valueOf(OrderStatus.MEAL));

        // when
        final Order updatedOrder = jdbcTemplateOrderDao.save(savedOrder);

        // then
        assertThat(updatedOrder.getOrderStatus()).isEqualTo(String.valueOf(OrderStatus.MEAL));
    }

    @Test
    void find_by_id() {
        // given
        final Order order = menuProductFixtureOf(OrderStatus.COOKING);
        final Order savedOrder = jdbcTemplateOrderDao.save(order);
        final Long savedOrderId = savedOrder.getId();

        // when
        final Optional<Order> menuGroupDaoById = jdbcTemplateOrderDao.findById(savedOrderId);

        // then
        assertThat(menuGroupDaoById).isPresent();
        assertThat(menuGroupDaoById.get().getId()).isEqualTo(savedOrderId);
    }

    @Test
    void find_by_id_return_empty_when_result_doesnt_exist() {
        // given
        final long doesntExistId = 10000L;

        // when
        final Optional<Order> menuGroupDaoById = jdbcTemplateOrderDao.findById(doesntExistId);

        // then
        assertThat(menuGroupDaoById).isEmpty();
    }

    @Test
    void find_all() {
        // given
        jdbcTemplateOrderDao.save(menuProductFixtureOf(OrderStatus.COOKING));
        jdbcTemplateOrderDao.save(menuProductFixtureOf(OrderStatus.COOKING));

        // when
        final List<Order> findAll = jdbcTemplateOrderDao.findAll();

        // then
        assertThat(findAll).hasSize(2);
    }

    @Test
    void exists_by_order_table_id_and_order_status_in() {
        // given
        final Order cookingOrder = menuProductFixtureOf(OrderStatus.COOKING);
        final Order mealOrder = menuProductFixtureOf(OrderStatus.MEAL);
        jdbcTemplateOrderDao.save(cookingOrder);
        jdbcTemplateOrderDao.save(mealOrder);
        final Long mealOrderTableId = mealOrder.getOrderTableId();

        // when
        boolean existsByOrderTableIdAndOrderStatusIn = jdbcTemplateOrderDao.existsByOrderTableIdAndOrderStatusIn(
                mealOrderTableId,
                List.of(OrderStatus.MEAL.name())
        );

        // then
        assertThat(existsByOrderTableIdAndOrderStatusIn).isTrue();
    }

    @Test
    void exists_by_order_table_ids_and_order_status_in() {
        // given
        final Order cookingOrder = menuProductFixtureOf(OrderStatus.COOKING);
        final Order mealOrder = menuProductFixtureOf(OrderStatus.MEAL);
        jdbcTemplateOrderDao.save(cookingOrder);
        jdbcTemplateOrderDao.save(mealOrder);
        final Long cookingOrderTableId = cookingOrder.getOrderTableId();
        final Long mealOrderTableId = mealOrder.getOrderTableId();

        // when
        boolean existsByOrderTableIdAndOrderStatusIn = jdbcTemplateOrderDao.existsByOrderTableIdInAndOrderStatusIn(
                List.of(mealOrderTableId, cookingOrderTableId),
                List.of(OrderStatus.MEAL.name(), OrderStatus.COOKING.name())
        );

        // then
        assertThat(existsByOrderTableIdAndOrderStatusIn).isTrue();
    }

    private Order menuProductFixtureOf(final OrderStatus orderStatus) {
        final Order order = new Order();
        order.setOrderStatus(orderStatus.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(getOrderTableId(3));
        return order;
    }

    private Long getOrderTableId(final int numberOfGuests) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(false);
        orderTable.setTableGroupId(getTableGroupId());
        return jdbcTemplateOrderTableDao.save(orderTable).getId();
    }

    private Long getTableGroupId() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        return jdbcTemplateTableGroupDao.save(tableGroup).getId();
    }

}

package kitchenpos.dao;

import static kitchenpos.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

@DisplayName("Order Dao 테스트")
class JdbcTemplateOrderDaoTest extends JdbcTemplateDaoTest {

    private JdbcTemplateOrderDao jdbcTemplateOrderDao;
    private JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;
    private JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao;

    @BeforeEach
    void setUp() {
        jdbcTemplateOrderDao = new JdbcTemplateOrderDao(dataSource);
        jdbcTemplateOrderTableDao = new JdbcTemplateOrderTableDao(dataSource);
        jdbcTemplateTableGroupDao = new JdbcTemplateTableGroupDao(dataSource);
    }

    @DisplayName("Order를 저장할 때")
    @Nested
    class Save {

        @DisplayName("정상적인 Order는 저장에 성공한다.")
        @Test
        void success() {
            // given
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroup을_생성한다());
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(OrderTable을_생성한다(2, tableGroup.getId()));
            Order order = Order를_생성한다(orderTable.getId(), COOKING.name());

            // when
            Order savedOrder = jdbcTemplateOrderDao.save(order);

            // then
            assertThat(jdbcTemplateOrderDao.findById(savedOrder.getId())).isPresent();
            assertThat(savedOrder).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(savedOrder);
        }

        @DisplayName("Order와 DB에 ID가 존재하는 Order는 수정을 진행한다.")
        @Test
        void createdThenUpdate() {
            // given
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroup을_생성한다());
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(OrderTable을_생성한다(2, tableGroup.getId()));
            Order beforeSaveOrderTable = Order를_생성한다(orderTable.getId(), COOKING.name());
            Order afterSaveOrderTable = jdbcTemplateOrderDao.save(beforeSaveOrderTable);

            assertThat(jdbcTemplateOrderDao.findById(afterSaveOrderTable.getId())).isPresent();
            assertThat(beforeSaveOrderTable).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(beforeSaveOrderTable);

            // when
            afterSaveOrderTable.setOrderStatus(MEAL.name());
            Order afterUpdateOrderTable = jdbcTemplateOrderDao.save(afterSaveOrderTable);

            // then
            assertThat(afterUpdateOrderTable).usingRecursiveComparison()
                .isEqualTo(afterSaveOrderTable);
            assertThat(afterSaveOrderTable.getOrderStatus()).isNotEqualTo(beforeSaveOrderTable.getOrderStatus());
        }

        @DisplayName("orderTableId가 Null인 경우 예외가 발생한다.")
        @Test
        void orderTableIdNullException() {
            // given
            Order order = Order를_생성한다(null, COOKING.name());

            // when, then
            assertThatThrownBy(() -> jdbcTemplateOrderDao.save(order))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
        }

        @DisplayName("orderStatus가 Null인 경우 예외가 발생한다.")
        @Test
        void orderStatusNullException() {
            // given
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroup을_생성한다());
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(OrderTable을_생성한다(2, tableGroup.getId()));
            Order order = Order를_생성한다(orderTable.getId(), null);

            // when, then
            assertThatThrownBy(() -> jdbcTemplateOrderDao.save(order))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
        }

        @DisplayName("orderedTime이 Null인 경우 예외가 발생한다.")
        @Test
        void orderedTimeNullException() {
            // given
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroup을_생성한다());
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(OrderTable을_생성한다(2, tableGroup.getId()));
            Order order = Order를_생성한다(orderTable.getId(), COOKING.name(), null);

            // when, then
            assertThatThrownBy(() -> jdbcTemplateOrderDao.save(order))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
        }
    }

    @DisplayName("ID를 통해 Order를 조회할 때")
    @Nested
    class FindById {

        @DisplayName("ID가 존재한다면 Order 조회에 성공한다.")
        @Test
        void present() {
            // given
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroup을_생성한다());
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(OrderTable을_생성한다(2, tableGroup.getId()));
            Order savedOrder = jdbcTemplateOrderDao.save(Order를_생성한다(orderTable.getId(), COOKING.name()));

            // when
            Optional<Order> foundOrder = jdbcTemplateOrderDao.findById(savedOrder.getId());

            // then
            assertThat(foundOrder).isPresent();
            assertThat(foundOrder.get()).usingRecursiveComparison()
                .isEqualTo(savedOrder);
        }

        @DisplayName("ID가 존재하지 않는다면 Order 조회에 실패한다.")
        @Test
        void isNotPresent() {
            // when
            Optional<Order> foundOrder = jdbcTemplateOrderDao.findById(Long.MAX_VALUE);

            // then
            assertThat(foundOrder).isNotPresent();
        }
    }

    @DisplayName("모든 Order를 조회한다.")
    @Test
    void findAll() {
        // given
        List<Order> beforeSavedOrders = jdbcTemplateOrderDao.findAll();

        TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroup을_생성한다());
        OrderTable orderTable = jdbcTemplateOrderTableDao.save(OrderTable을_생성한다(2, tableGroup.getId()));

        beforeSavedOrders.add(jdbcTemplateOrderDao.save(Order를_생성한다(orderTable.getId(), COOKING.name())));
        beforeSavedOrders.add(jdbcTemplateOrderDao.save(Order를_생성한다(orderTable.getId(), COOKING.name())));
        beforeSavedOrders.add(jdbcTemplateOrderDao.save(Order를_생성한다(orderTable.getId(), COOKING.name())));

        // when
        List<Order> afterSavedOrders = jdbcTemplateOrderDao.findAll();

        // then
        assertThat(afterSavedOrders).hasSize(beforeSavedOrders.size());
        assertThat(afterSavedOrders).usingRecursiveComparison()
            .isEqualTo(beforeSavedOrders);
    }

    @DisplayName("OrderTableId가 일치하고 OrderStatus 리스트에 포함되는 Order가 존재하는지 확인할 때")
    @Nested
    class CheckExistsByOrderTableInAndOrderStatusIn {

        @DisplayName("OrderTableId가 일치하고 OrderStatus 리스트에 포함되는 Order가 존재하면 true가 반환된다.")
        @Test
        void returnTrue() {
            // given
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroup을_생성한다());
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(OrderTable을_생성한다(2, tableGroup.getId()));
            Order order = jdbcTemplateOrderDao.save(Order를_생성한다(orderTable.getId(), COOKING.name()));

            // when
            boolean exists = jdbcTemplateOrderDao.existsByOrderTableIdAndOrderStatusIn(
                order.getOrderTableId(), Arrays.asList(COOKING.name(), MEAL.name())
            );

            // then
            assertThat(exists).isTrue();
        }

        @DisplayName("OrderTableId가 일치하는 Order가 없으면 False가 반환된다.")
        @Test
        void orderTableIdNotMatchFalse() {
            // given
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroup을_생성한다());
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(OrderTable을_생성한다(2, tableGroup.getId()));
            Order order = jdbcTemplateOrderDao.save(Order를_생성한다(orderTable.getId(), COOKING.name()));

            // when
            boolean exists = jdbcTemplateOrderDao.existsByOrderTableIdAndOrderStatusIn(
                Long.MAX_VALUE, Arrays.asList(COOKING.name(), MEAL.name())
            );

            // then
            assertThat(exists).isFalse();
        }

        @DisplayName("OrderStatus 리스트에 포함되는 Order가 없으면 False가 반환된다.")
        @Test
        void orderStatusNotInFalse() {
            // given
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroup을_생성한다());
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(OrderTable을_생성한다(2, tableGroup.getId()));
            Order order = jdbcTemplateOrderDao.save(Order를_생성한다(orderTable.getId(), COOKING.name()));

            // when
            boolean exists = jdbcTemplateOrderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(), Arrays.asList(MEAL.name(), COMPLETION.name())
            );

            // then
            assertThat(exists).isFalse();
        }
    }

    @DisplayName("OrderTableId 리스트에 포함되고 OrderStatus 리스트에 포함되는 Order가 존재하는지 확인할 때")
    @Nested
    class CheckExistsByOrderTableIdInAndOrderStatusIn {

        @DisplayName("OrderTableId가 일치하고 OrderStatus 리스트에 포함되는 Order가 존재하면 true가 반환된다.")
        @Test
        void returnTrue() {
            // given
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroup을_생성한다());
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(OrderTable을_생성한다(2, tableGroup.getId()));
            Order order = jdbcTemplateOrderDao.save(Order를_생성한다(orderTable.getId(), COOKING.name()));

            // when
            boolean exists = jdbcTemplateOrderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(orderTable.getId(), Long.MAX_VALUE), Arrays.asList(COOKING.name(), MEAL.name())
            );

            // then
            assertThat(exists).isTrue();
        }

        @DisplayName("OrderTableId 리스트에 포함되는 Order가 없으면 False가 반환된다.")
        @Test
        void orderTableIdNotInFalse() {
            // given
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroup을_생성한다());
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(OrderTable을_생성한다(2, tableGroup.getId()));
            Order order = jdbcTemplateOrderDao.save(Order를_생성한다(orderTable.getId(), COOKING.name()));

            // when
            boolean exists = jdbcTemplateOrderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(Long.MAX_VALUE, Long.MAX_VALUE - 1), Arrays.asList(COOKING.name(), MEAL.name())
            );

            // then
            assertThat(exists).isFalse();
        }

        @DisplayName("OrderStatus 리스트에 포함되는 Order가 없으면 False가 반환된다.")
        @Test
        void orderStatusNotInFalse() {
            // given
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroup을_생성한다());
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(OrderTable을_생성한다(2, tableGroup.getId()));
            Order order = jdbcTemplateOrderDao.save(Order를_생성한다(orderTable.getId(), COOKING.name()));

            // when
            boolean exists = jdbcTemplateOrderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(orderTable.getId(), Long.MAX_VALUE), Arrays.asList(MEAL.name(), COMPLETION.name())
            );

            // then
            assertThat(exists).isFalse();
        }
    }

    private Order Order를_생성한다(Long orderTableId, String orderStatus) {
        return Order를_생성한다(orderTableId, orderStatus, LocalDateTime.now());
    }

    private Order Order를_생성한다(Long orderTableId, String orderStatus, LocalDateTime localDateTime) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(localDateTime);

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
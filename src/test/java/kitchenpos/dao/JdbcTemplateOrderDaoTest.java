package kitchenpos.dao;

import static kitchenpos.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

@DisplayName("Order Dao 테스트")
class JdbcTemplateOrderDaoTest extends JdbcTemplateDaoTest {

    private JdbcTemplateOrderDao jdbcTemplateOrderDao;

    @BeforeEach
    void setUp() {
        jdbcTemplateOrderDao = new JdbcTemplateOrderDao(dataSource);
    }

    @DisplayName("Order를 저장할 때")
    @Nested
    class Save {

        @DisplayName("정상적인 Order는 저장에 성공한다.")
        @Test
        void success() {
            // given
            Order order = Order를_생성한다(1L, COOKING.name());

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
            Order beforeSaveOrderTable = Order를_생성한다(1L, COOKING.name());
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
            Order order = Order를_생성한다(1L, null);

            // when, then
            assertThatThrownBy(() -> jdbcTemplateOrderDao.save(order))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
        }

        @DisplayName("orderedTime이 Null인 경우 예외가 발생한다.")
        @Test
        void orderedTimeNullException() {
            // given
            Order order = Order를_생성한다(1L, COOKING.name(), null);

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
            Order savedOrder = jdbcTemplateOrderDao.save(Order를_생성한다(1L, COOKING.name()));

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

        beforeSavedOrders.add(jdbcTemplateOrderDao.save(Order를_생성한다(1L, COOKING.name())));
        beforeSavedOrders.add(jdbcTemplateOrderDao.save(Order를_생성한다(2L, COOKING.name())));
        beforeSavedOrders.add(jdbcTemplateOrderDao.save(Order를_생성한다(3L, COOKING.name())));

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
            Order order = jdbcTemplateOrderDao.save(Order를_생성한다(1L, COOKING.name()));

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
            Order order = jdbcTemplateOrderDao.save(Order를_생성한다(1L, COOKING.name()));

            // when
            boolean exists = jdbcTemplateOrderDao.existsByOrderTableIdAndOrderStatusIn(
                2L, Arrays.asList(COOKING.name(), MEAL.name())
            );

            // then
            assertThat(exists).isFalse();
        }

        @DisplayName("OrderStatus 리스트에 포함되는 Order가 없으면 False가 반환된다.")
        @Test
        void orderStatusNotInFalse() {
            // given
            Order order = jdbcTemplateOrderDao.save(Order를_생성한다(1L, COOKING.name()));

            // when
            boolean exists = jdbcTemplateOrderDao.existsByOrderTableIdAndOrderStatusIn(
                1L, Arrays.asList(MEAL.name(), COMPLETION.name())
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
            Order order = jdbcTemplateOrderDao.save(Order를_생성한다(1L, COOKING.name()));

            // when
            boolean exists = jdbcTemplateOrderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(1L, 2L), Arrays.asList(COOKING.name(), MEAL.name())
            );

            // then
            assertThat(exists).isTrue();
        }

        @DisplayName("OrderTableId 리스트에 포함되는 Order가 없으면 False가 반환된다.")
        @Test
        void orderTableIdNotInFalse() {
            // given
            Order order = jdbcTemplateOrderDao.save(Order를_생성한다(1L, COOKING.name()));

            // when
            boolean exists = jdbcTemplateOrderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(2L, 3L), Arrays.asList(COOKING.name(), MEAL.name())
            );

            // then
            assertThat(exists).isFalse();
        }

        @DisplayName("OrderStatus 리스트에 포함되는 Order가 없으면 False가 반환된다.")
        @Test
        void orderStatusNotInFalse() {
            // given
            Order order = jdbcTemplateOrderDao.save(Order를_생성한다(1L, COOKING.name()));

            // when
            boolean exists = jdbcTemplateOrderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(1L, 2L), Arrays.asList(MEAL.name(), COMPLETION.name())
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
}
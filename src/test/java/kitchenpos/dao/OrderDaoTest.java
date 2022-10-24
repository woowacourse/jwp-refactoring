package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;

@JdbcTest
class OrderDaoTest {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    @Autowired
    private OrderDaoTest(final DataSource dataSource) {
        this.orderDao = new JdbcTemplateOrderDao(dataSource);
        this.orderTableDao = new JdbcTemplateOrderTableDao(dataSource);
    }

    @Test
    @DisplayName("주문을 저장한다")
    void save() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(false);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderStatus(OrderStatus.MEAL.name());
        order.setOrderedTime(LocalDateTime.now());

        // when
        final Order saved = orderDao.save(order);

        // then
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getOrderTableId()).isEqualTo(savedOrderTable.getId()),
                () -> assertThat(saved.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name()),
                () -> assertThat(saved.getOrderedTime()).isBeforeOrEqualTo(LocalDateTime.now())
        );
    }

    @Test
    @DisplayName("존재하지 않는 주문 테이블로 주문을 저장하려고 하면 예외가 발생한다")
    void saveExceptionNotExistOrderTable() {
        // given
        final Order order = new Order();
        order.setOrderTableId(-1L);
        order.setOrderStatus(OrderStatus.MEAL.name());
        order.setOrderedTime(LocalDateTime.now());

        // when, then
        assertThatThrownBy(() -> orderDao.save(order))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("id로 주문을 조회한다")
    void findById() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(false);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderStatus(OrderStatus.MEAL.name());
        order.setOrderedTime(LocalDateTime.now());

        final Order saved = orderDao.save(order);

        // when
        final Order foundOrder = orderDao.findById(saved.getId())
                .get();

        // then
        assertThat(foundOrder).usingRecursiveComparison()
                .isEqualTo(saved);
    }

    @Test
    @DisplayName("id로 주문을 조회할 때 결과가 없다면 Optional.empty를 반환한다")
    void findByIdNotExist() {
        // when
        final Optional<Order> order = orderDao.findById(-1L);

        // then
        assertThat(order).isEmpty();
    }

    @Test
    @DisplayName("모든 주문을 조회한다")
    void findAll() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(false);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderStatus(OrderStatus.MEAL.name());
        order.setOrderedTime(LocalDateTime.now());

        final Order saved = orderDao.save(order);

        // when
        final List<Order> orders = orderDao.findAll();

        // then
        assertAll(
                () -> assertThat(orders).hasSizeGreaterThanOrEqualTo(1),
                () -> assertThat(orders).extracting("id")
                        .contains(saved.getId())
        );
    }

    @Test
    @DisplayName("주문 테이블 id와 주문 상태가 모두 일치하는 주문이 존재하는지 확인한다")
    void existsByOrderTableIdAndOrderStatusIn() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(false);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderStatus(OrderStatus.MEAL.name());
        order.setOrderedTime(LocalDateTime.now());

        orderDao.save(order);

        // when
        final boolean exists = orderDao.existsByOrderTableIdAndOrderStatusIn(savedOrderTable.getId(),
                Collections.singletonList(OrderStatus.MEAL.name()));

        final boolean notMatchOrderStatus = orderDao.existsByOrderTableIdAndOrderStatusIn(savedOrderTable.getId(),
                Collections.singletonList(OrderStatus.COMPLETION.name()));

        final boolean notExistsOrderTable = orderDao.existsByOrderTableIdAndOrderStatusIn(-1L,
                Collections.singletonList(OrderStatus.COMPLETION.name()));

        // then
        assertAll(
                () -> assertThat(exists).isTrue(),
                () -> assertThat(notMatchOrderStatus).isFalse(),
                () -> assertThat(notExistsOrderTable).isFalse()
        );
    }

    @Test
    @DisplayName("주어진 주문 테이블 id들 중에서 주문 상태가 일치하는 주문이 존재하는지 확인한다")
    void existsByOrderTableIdInAndOrderStatusIn() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(false);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderStatus(OrderStatus.MEAL.name());
        order.setOrderedTime(LocalDateTime.now());

        orderDao.save(order);

        // when
        final boolean exists = orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Collections.singletonList(savedOrderTable.getId()),
                Collections.singletonList(OrderStatus.MEAL.name()));

        final boolean notMatchOrderStatus = orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Collections.singletonList(savedOrderTable.getId()),
                Collections.singletonList(OrderStatus.COMPLETION.name()));

        final boolean notExistsOrderTable = orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Collections.singletonList(-1L),
                Collections.singletonList(OrderStatus.MEAL.name()));

        // then
        assertAll(
                () -> assertThat(exists).isTrue(),
                () -> assertThat(notMatchOrderStatus).isFalse(),
                () -> assertThat(notExistsOrderTable).isFalse()
        );
    }
}

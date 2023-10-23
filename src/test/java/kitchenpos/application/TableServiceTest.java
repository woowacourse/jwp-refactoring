package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Sql(scripts = "classpath:truncate.sql")
@Transactional
@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("주문 테이블을 정상적으로 생성한다.")
    void create() {
        // given
        final OrderTable orderTable = new OrderTable(null, 2, false);
        final TableGroup tableGroup = new TableGroup(List.of(orderTable));
        tableGroupRepository.save(tableGroup);

        // when
        final OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertAll(
                () -> assertThat(savedOrderTable.getId()).isEqualTo(1L),
                () -> assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(2),
                () -> assertThat(savedOrderTable.isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("주문 테이블 목록을 정상적으로 반환한다.")
    void list() {
        // given
        final OrderTable orderTableA = new OrderTable(null, 2, false);
        final OrderTable orderTableB = new OrderTable(null, 2, false);
        orderTableRepository.save(orderTableA);
        orderTableRepository.save(orderTableB);

        // when
        final List<OrderTable> orderTables = tableService.list();

        // then
        final OrderTable savedOrderTableA = orderTables.get(0);
        final OrderTable savedOrderTableB = orderTables.get(1);
        assertAll(
                () -> assertThat(orderTables).hasSize(2),
                () -> assertThat(savedOrderTableA.getId()).isEqualTo(1L),
                () -> assertThat(savedOrderTableA.getNumberOfGuests()).isEqualTo(2),
                () -> assertThat(savedOrderTableA.isEmpty()).isFalse(),
                () -> assertThat(savedOrderTableB.getId()).isEqualTo(2L),
                () -> assertThat(savedOrderTableB.getNumberOfGuests()).isEqualTo(2),
                () -> assertThat(savedOrderTableB.isEmpty()).isFalse()
        );
    }

    @Nested
    @DisplayName("주문 테이블은 테이블의 주문가능 상태를 변경할 때 ")
    class ChangeEmpty {

        @Test
        @DisplayName("빈 테이블로 정상적으로 변경된다.")
        void changeEmpty() {
            // given
            final OrderTable orderTable = new OrderTable(null, 2, false);
            orderTableRepository.save(orderTable);
            final OrderTable newOrderedTable = new OrderTable(null, 2, true);

            // when
            final OrderTable savedOrderTable = tableService.changeEmpty(1L, newOrderedTable);

            // then
            assertThat(savedOrderTable.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("주문 테이블 ID가 존재하지 않으면 예외가 발생한다.")
        void throwsExceptionWhenOrderTableIdNonExist() {
            // given
            final OrderTable orderTable = new OrderTable(null, 2, false);
            orderTableRepository.save(orderTable);
            final OrderTable newOrderedTable = new OrderTable(null, 2, false);

            // when, then
            assertThatThrownBy(() -> tableService.changeEmpty(2L, newOrderedTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("테이블 그룹 ID가 빈 값이 아니면 예외가 발생한다.")
        void throwsExceptionWhenTableGroupIdIsNoTNull() {
            // given
            final OrderTable orderTable = new OrderTable(null, 2, false);
            final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
            final TableGroup tableGroup = new TableGroup(List.of(orderTable));
            tableGroupRepository.save(tableGroup);
            final OrderTable newOrderedTable = new OrderTable(null, 2, true);

            // when, then
            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), newOrderedTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        @DisplayName("주문 테이블이 존재하면서, 주문이 결제완료 상태가 아니면 예외가 발생한다.")
        void throwsExceptionWhenOrderIsNotCompletion(OrderStatus orderStatus) {
            // given
            final OrderTable orderTable = new OrderTable(null, 2, false);
            orderTableRepository.save(orderTable);

            final Order order = new Order(new ArrayList<>(), orderTable, orderStatus);
            orderRepository.save(order);

            final OrderTable newOrderedTable = new OrderTable(null, 2, false);

            // when, then
            assertThatThrownBy(() -> tableService.changeEmpty(1L, newOrderedTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("주문 테이블의 손님 수를 변경할 때 ")
    class ChangeNumberOfGuests {

        @Test
        @DisplayName("정상적으로 변경된다.")
        void changeNumberOfGuests() {
            // given
            final OrderTable orderTable = new OrderTable(null, 2, false);
            final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

            final OrderTable newOrderedTable = new OrderTable(null, 3, false);

            // when
            final OrderTable changedOrderTable =
                    tableService.changeNumberOfGuests(savedOrderTable.getId(), newOrderedTable);

            // then
            assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(newOrderedTable.getNumberOfGuests());
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, Integer.MIN_VALUE, -100000})
        @DisplayName("변경하려는 손님 수가 0보다 작은 경우 예외가 발생한다.")
        void throwsExceptionWhenNumberOfGuestsIsUnderZero(int numberOfGuests) {
            // given
            final OrderTable orderTable = new OrderTable(null, 2, false);
            final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

            final OrderTable newOrderedTable = new OrderTable(null, numberOfGuests, false);
            final OrderTable savedNewOrderedTable = orderTableRepository.save(newOrderedTable);

            // when, then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), savedNewOrderedTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("변경하려는 주문 테이블이 존재하지 않는 경우 예외가 발생한다.")
        void throwsExceptionWhenOrderTableNonExist() {
            // given
            final OrderTable orderTable = new OrderTable(null, 2, false);
            orderTableRepository.save(orderTable);

            final OrderTable newOrderedTable = new OrderTable(null, 3, false);
            final OrderTable savedNewOrderedTable = orderTableRepository.save(newOrderedTable);

            // when, then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(3L, savedNewOrderedTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("변경하려는 주문 테이블이 빈 테이블인 경우 예외가 발생한다.")
        void throwsExceptionWhenOrderTableIsEmpty() {
            // given
            final OrderTable orderTable = new OrderTable(null, 2, true);
            final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

            final OrderTable newOrderedTable = new OrderTable(null, 3, true);
            final OrderTable savedNewOrderedTable = orderTableRepository.save(newOrderedTable);

            // when, then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), savedNewOrderedTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}

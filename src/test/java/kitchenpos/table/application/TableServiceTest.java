package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.common.OrderStatus;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableChangNumberOfGuestRequest;
import kitchenpos.table.dto.OrderTableChangeEmptyRequest;
import kitchenpos.table.dto.OrderTableCreateRequest;
import kitchenpos.tablegroup.domain.TableGroupGenerator;
import kitchenpos.tablegroup.domain.TableGroupRepository;
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

    @Autowired
    private TableGroupGenerator tableGroupGenerator;

    @Test
    @DisplayName("주문 테이블을 정상적으로 생성한다.")
    void create() {
        // given
        final OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(2, true);

        // when
        final OrderTable savedOrderTable = tableService.create(orderTableCreateRequest);

        // then
        assertAll(
                () -> assertThat(savedOrderTable.getId()).isEqualTo(1L),
                () -> assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(2),
                () -> assertThat(savedOrderTable.isEmpty()).isTrue()
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
            final OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(true);

            // when
            final OrderTable savedOrderTable = tableService.changeEmpty(1L, request);

            // then
            assertThat(savedOrderTable.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("주문 테이블 ID가 존재하지 않으면 예외가 발생한다.")
        void throwsExceptionWhenOrderTableIdNonExist() {
            // given
            final OrderTable orderTable = new OrderTable(null, 2, false);
            orderTableRepository.save(orderTable);
            final OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(false);

            // when, then
            assertThatThrownBy(() -> tableService.changeEmpty(2L, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블을 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("테이블 그룹이 빈 값이 아니면 예외가 발생한다.")
        void throwsExceptionWhenTableGroupIdIsNoTNull() {
            // given
            final OrderTable orderTableA = new OrderTable(1L, 2, false);
            orderTableRepository.save(orderTableA);

            final OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(true);

            // when, then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableA.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블 그룹ID는 비어있어야 합니다.");
        }

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        @DisplayName("주문 테이블이 존재하면서, 주문이 결제완료 상태가 아니면 예외가 발생한다.")
        void throwsExceptionWhenOrderIsNotCompletion(OrderStatus orderStatus) {
            // given
            final OrderTable orderTable = new OrderTable(null, 2, false);
            orderTableRepository.save(orderTable);

            final Order order = new Order(new ArrayList<>(), orderTable.getId(), orderStatus);
            orderRepository.save(order);

            final OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(false);

            // when, then
            assertThatThrownBy(() -> tableService.changeEmpty(1L, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블은 존재하면서 결제완료 상태가 아니어야 합니다.");
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
            final OrderTableChangNumberOfGuestRequest request = new OrderTableChangNumberOfGuestRequest(3);

            // when
            final OrderTable changedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(), request);

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

            final OrderTableChangNumberOfGuestRequest request = new OrderTableChangNumberOfGuestRequest(numberOfGuests);

            // when, then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("방문한 손님 수는 음수가 될 수 없습니다.");
        }

        @Test
        @DisplayName("변경하려는 주문 테이블이 존재하지 않는 경우 예외가 발생한다.")
        void throwsExceptionWhenOrderTableNonExist() {
            // given
            final OrderTable orderTable = new OrderTable(null, 2, false);
            orderTableRepository.save(orderTable);

            final OrderTableChangNumberOfGuestRequest request = new OrderTableChangNumberOfGuestRequest(3);

            // when, then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(3L, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블을 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("변경하려는 주문 테이블이 빈 테이블인 경우 예외가 발생한다.")
        void throwsExceptionWhenOrderTableIsEmpty() {
            // given
            final OrderTable orderTable = new OrderTable(null, 2, true);
            final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

            final OrderTableChangNumberOfGuestRequest request = new OrderTableChangNumberOfGuestRequest(3);

            // when, then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블은 비어있을 수 없습니다.");
        }
    }
}

package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.cleaner.ApplicationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Test
    @DisplayName("OrderTable을 생성한다.")
    void create() {
        OrderTable orderTable = tableService.create(new OrderTable(10, true));

        assertAll(
                () -> assertThat(orderTable.getTableGroupId()).isNull(),
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(10),
                () -> assertThat(orderTable.isEmpty()).isTrue()
        );
    }

    @Test
    @DisplayName("모든 OrderTable을 조회한다.")
    void list() {
        OrderTable orderTable1 = tableService.create(new OrderTable(10, true));
        OrderTable orderTable2 = tableService.create(new OrderTable(5, true));

        List<OrderTable> orderTables = tableService.list();
        assertThat(orderTables).containsExactly(orderTable1, orderTable2);
    }

    @Nested
    @DisplayName("비어있는 상태로 만들 때 ")
    class ChangeEmptyTest {

        @Test
        @DisplayName("OrderTable이 존재하지 않으면 예외가 발생한다.")
        void orderTableNotFoundFailed() {
            OrderTable orderTable = orderTableDao.save(new OrderTable(10, true));

            assertThatThrownBy(() -> tableService.changeEmpty(0L, orderTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블을 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("OrderTable이 테이블 그룹에 속해있지 않을 경우 예외가 발생한다.")
        void orderTableNotInTableGroupFailed() {
            TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));
            OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 10, true));

            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 테이블 그룹에 속해있습니다.");
        }

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        @DisplayName("OrderTable의 주문 상태가 COMPLETION이 아닐 경우 예외가 발생한다.")
        void orderTableOrdersNotCompletionFailed(final OrderStatus orderStatus) {
            OrderTable orderTable = tableService.create(new OrderTable(10, true));
            orderDao.save(new Order(orderTable.getId(), orderStatus.name(), LocalDateTime.now()));

            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("현재 조리 / 식사 중입니다.");
        }

        @Test
        @DisplayName("정상적인 경우 성공한다.")
        void changeEmpty() {
            OrderTable orderTable = tableService.create(new OrderTable(10, true));
            OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(), orderTable);

            assertThat(changedOrderTable.isEmpty()).isTrue();
        }
    }

    @Nested
    @DisplayName("테이블 인원 수를 바꿀 때 ")
    class ChangeNumberOfGuestsTest {

        @Test
        @DisplayName("음수로 바꿀 경우 예외가 발생한다.")
        void negativeNumberFailed() {
            OrderTable orderTable = tableService.create(new OrderTable(10, false));

            OrderTable changingOrderTable = new OrderTable(-1, false);
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), changingOrderTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블 인원은 음수일 수 없습니다.");
        }

        @Test
        @DisplayName("주문 테이블이 존재하지 않을 경우 예외가 발생한다.")
        void orderTableNotFoundFailed() {
            OrderTable changingOrderTable = new OrderTable(10, false);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, changingOrderTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블을 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("테이블이 비어있으면 예외가 발생한다.")
        void orderTableEmptyFailed() {
            OrderTable orderTable = tableService.create(new OrderTable(10, true));

            OrderTable changingOrderTable = new OrderTable(10, false);
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), changingOrderTable))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블이 비어있을 수 없습니다.");
        }

        @Test
        @DisplayName("정상적인 경우 인원 변경에 성공한다.")
        void changeNumberOfGuests() {
            OrderTable orderTable = tableService.create(new OrderTable(10, false));

            OrderTable changingOrderTable = new OrderTable(20, false);
            OrderTable result = tableService.changeNumberOfGuests(orderTable.getId(), changingOrderTable);
            assertThat(result.getNumberOfGuests()).isEqualTo(20);
        }
    }
}

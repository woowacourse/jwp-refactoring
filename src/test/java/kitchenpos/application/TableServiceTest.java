package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

class TableServiceTest extends ServiceTest {

    @Nested
    @DisplayName("create()")
    class create {

        @Test
        @DisplayName("예외사항이 존재하지 않는 경우 새로운 테이블을 생성한다.")
        void create() {
            // given
            OrderTable orderTable = createOrderTable(null);

            // when
            OrderTable savedOrderTable = tableService.create(orderTable);

            // then
            assertThat(savedOrderTable.getId()).isNotNull();
        }

    }

    @Nested
    @DisplayName("list()")
    class list {

        @Test
        @DisplayName("전체 테이블을 조회한다.")
        void list() {
            List<OrderTable> tables = tableService.list();
            assertThat(tables).isNotNull();
        }

    }

    @Nested
    @DisplayName("changeEmpty()")
    class changeEmpty {

        @Test
        @DisplayName("예외사항이 존재하지 않는 경우 상태를 변경한다.")
        void changeEmpty() {
            // given
            OrderTable savedOrderTable = createAndSaveOrderTable(null);

            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(true);

            // when
            OrderTable changedOrderTable = tableService.changeEmpty(savedOrderTable.getId(), orderTable);

            // then
            assertThat(changedOrderTable.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("존재하지 않는 테이블 id인 경우 예외가 발생한다.")
        void invalidOrderTableId() {
            // given
            OrderTable orderTable = createOrderTable(null);

            // when, then
            assertThatThrownBy(() -> tableService.changeEmpty(0L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("table group id가 null이 아닌 경우 예외가 발생한다.")
        void notNullTableGroupId() {
            // given
            TableGroup tableGroup = createAndSaveTableGroup();
            OrderTable savedOrderTable = createAndSaveOrderTable(tableGroup.getId());

            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(true);

            // when, then
            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        @DisplayName("COOKING 혹은 MEAL 상태인 경우 예외가 발생한다.")
        void invalidStatus(String status) {
            // given
            OrderTable savedOrderTable = createAndSaveOrderTable(null);
            createAndSaveOrder(savedOrderTable.getId(), status);

            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(true);

            // when, then
            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Nested
    @DisplayName("changeNumberOfGuests()")
    class changeNumberOfGuests {

        @Test
        @DisplayName("예외사항이 존재하지 않는 경우 방문자 수를 변경한다.")
        void changeNumberOfGuests() {
            // given
            OrderTable savedOrderTable = createAndSaveOrderTable(null);

            OrderTable orderTable = new OrderTable();
            orderTable.setNumberOfGuests(20);

            // when
            OrderTable changedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable);

            // then
            assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(20);
        }

        @Test
        @DisplayName("존재하지 않은 테이블 id의 경우 예외가 발생한다.")
        void invalidOrderTableId() {
            // given
            OrderTable orderTable = new OrderTable();
            orderTable.setNumberOfGuests(20);

            // when, then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("0 미만의 수인 경우 예외가 발생한다.")
        void lessThanZero() {
            // given
            OrderTable savedOrderTable = createAndSaveOrderTable(null);

            OrderTable orderTable = new OrderTable();
            orderTable.setNumberOfGuests(-1);

            // when, then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
        }

    }

    private OrderTable createOrderTable(Long tableGroupId) {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(10);
        orderTable.setEmpty(false);

        return orderTable;
    }

    private OrderTable createAndSaveOrderTable(Long tableGroupId) {
        OrderTable orderTable = createOrderTable(tableGroupId);

        return orderTableDao.save(orderTable);
    }

    private TableGroup createAndSaveTableGroup() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());

        return tableGroupDao.save(tableGroup);
    }

    private Order createAndSaveOrder(long orderTableId, String status) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(status);

        return orderDao.save(order);
    }

}

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
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
    @DisplayName("테이블을 생성한다.")
    void createTable() {
        final int actualNumberOfGuests = 2;
        final OrderTable orderTable = new OrderTable(null, actualNumberOfGuests, true);

        final OrderTable actual = tableService.create(orderTable);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(actualNumberOfGuests),
                () -> assertThat(actual.getTableGroupId()).isNull(),
                () -> assertThat(actual.isEmpty()).isTrue()
        );
    }

    @Test
    @DisplayName("테이블 목록들을 조회한다.")
    void getTables() {
        final List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).hasSize(8);
    }

    @Test
    @DisplayName("테이블의 상태를 빈 테이블로 변경한다.")
    void changeEmptyTable() {
        final OrderTable orderTable = tableService.create(new OrderTable(null, 0, true));

        final OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(),
                new OrderTable(null, 2, false));

        assertThat(changedOrderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("테이블의 상태를 주문 테이블로 변경한다.")
    void changeOrderTable() {
        final OrderTable orderTable = tableService.create(new OrderTable(null, 0, false));

        final OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(),
                new OrderTable(null, 2, true));

        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("주문 테이블이 기존 테이블 그룹에 포함되어 있는 경우 예외 발생")
    void whenOrderTableIsIncludeInTableGroup() {
        final TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null));
        final OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 2, false));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTable(null, 0, true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 주문의 상태가 조리 상태면 예외 발생")
    void whenOrderTableWithCookingStatus() {
        final long orderTableId = 1L;
        final Order order = new Order(orderTableId, OrderStatus.COOKING.name(), LocalDateTime.now(), null);
        orderDao.save(order);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, new OrderTable(null, 2, false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 주문의 상태가 식사 상태면 예외 발생")
    void whenOrderTableWithMealStatus() {
        final long orderTableId = 1L;
        final Order order = new Order(orderTableId, OrderStatus.MEAL.name(), LocalDateTime.now(), null);
        orderDao.save(order);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, new OrderTable(null, 2, false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 손님 수를 변경한다.")
    void changeNumberOrGuests() {
        final OrderTable orderTable = tableService.create(new OrderTable(null, 1, false));

        final OrderTable changedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(),
                new OrderTable(null, 5, true));

        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    @DisplayName("손님의 수가 0이면 예외 발생")
    void whenNumberOfGuestsIsZero() {
        final OrderTable orderTable = tableService.create(new OrderTable(null, 0, true));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), new OrderTable(null, 5, false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("손님의 수가 음수이면 예외 발생")
    void whenNumberOfGuestsIsNegative() {
        final OrderTable orderTable = tableService.create(new OrderTable(null, 1, false));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), new OrderTable(null, -1, true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("기존의 주문 테이블이 존재하지 않을 경우 예외 발생")
    void whenInvalidOrderTableId() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(99999L, new OrderTable(null, 1, false)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

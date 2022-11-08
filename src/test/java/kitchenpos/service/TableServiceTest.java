package kitchenpos.service;

import kitchenpos.application.TableService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.OrderTableUpdateEmptyRequest;
import kitchenpos.dto.OrderTableUpdateGuestsRequest;
import kitchenpos.util.FakeOrderDao;
import kitchenpos.util.FakeOrderTableDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TableServiceTest {

    private final OrderTableDao orderTableDao = new FakeOrderTableDao();
    private final OrderDao orderDao = new FakeOrderDao();
    private final TableService tableService = new TableService(orderDao, orderTableDao);
    @DisplayName("테이블을 생성한다")
    @Test
    void create() {
        preprocessWhenCreate(new OrderTable(3, false));
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(3, false);

        OrderTableResponse orderTable = tableService.create(orderTableCreateRequest);

        assertAll(
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(3),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }
    @DisplayName("인원이 null인 테이블을 생성한다")
    @Test
    void create_numberOfGuestsNull() {
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(null, false);

        assertThatThrownBy(() -> tableService.create(orderTableCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @DisplayName("인원이 음수인 테이블을 생성한다")
    @Test
    void create_numberOfGuestsNegative() {
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(-1, false);

        assertThatThrownBy(() -> tableService.create(orderTableCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @DisplayName("테이블 목록을 조회한다")
    @Test
    void list() {
        preprocessWhenList(2);
        List<OrderTableResponse> orderTables = tableService.list();

        assertThat(orderTables.size()).isEqualTo(2);
    }
    @DisplayName("테이블의 주문 가능 여부를 바꾼다")
    @Test
    void changeEmpty() {
        preprocessWhenChangeEmpty(new OrderTable(1L, null, 0, false),
                List.of(new Order(1L, OrderStatus.COMPLETION, LocalDateTime.now()),
                        new Order(1L, OrderStatus.COMPLETION, LocalDateTime.now())));
        OrderTableUpdateEmptyRequest orderTableUpdateEmptyRequest = new OrderTableUpdateEmptyRequest(true);

        OrderTableResponse savedOrderTable = tableService.changeEmpty(1L, orderTableUpdateEmptyRequest);

        assertAll(
                () -> assertThat(savedOrderTable.getTableGroupId()).isNull(),
                () -> assertThat(savedOrderTable.getId()).isEqualTo(1L),
                () -> assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(0),
                () -> assertThat(savedOrderTable.isEmpty()).isTrue()
        );
    }
    @DisplayName("테이블 그룹으로 묶은 테이블의 주문 가능 여부를 바꿀 수 없다")
    @Test
    void changeEmpty_groupIdNotNull() {
        preprocessWhenChangeEmpty(new OrderTable(1L, 1L, 0, false),
                List.of(new Order(1L, OrderStatus.COMPLETION, LocalDateTime.now()),
                        new Order(1L, OrderStatus.COMPLETION, LocalDateTime.now())));
        OrderTableUpdateEmptyRequest orderTableUpdateEmptyRequest = new OrderTableUpdateEmptyRequest(true);

        assertThatThrownBy(() -> tableService.changeEmpty(4L, orderTableUpdateEmptyRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @DisplayName("조리 중인 주문이 있는 테이블의 주문 가능 여부를 바꿀 수 없다")
    @Test
    void changeEmpty_statusCooking() {
        preprocessWhenChangeEmpty(new OrderTable(1L, null, 0, false),
                List.of(new Order(1L, OrderStatus.COOKING, LocalDateTime.now()),
                        new Order(1L, OrderStatus.COMPLETION, LocalDateTime.now())));
        OrderTableUpdateEmptyRequest orderTableUpdateEmptyRequest = new OrderTableUpdateEmptyRequest(true);

        assertThatThrownBy(() -> tableService.changeEmpty(2L, orderTableUpdateEmptyRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @DisplayName("식사 중인 주문이 있는 테이블의 주문 가능 여부를 바꿀 수 없다")
    @Test
    void changeEmpty_statusMeal() {
        preprocessWhenChangeEmpty(new OrderTable(1L, null, 0, false),
                List.of(new Order(1L, OrderStatus.MEAL, LocalDateTime.now()),
                        new Order(1L, OrderStatus.COMPLETION, LocalDateTime.now())));
        OrderTableUpdateEmptyRequest orderTableUpdateEmptyRequest = new OrderTableUpdateEmptyRequest(true);

        assertThatThrownBy(() -> tableService.changeEmpty(3L, orderTableUpdateEmptyRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @DisplayName("테이블의 인원 수를 바꾼다")
    @Test
    void changeNumberOfGuests() {
        preprocessWhenCreate(new OrderTable(2L, null, 2, false));
        OrderTableUpdateGuestsRequest orderTableUpdateGuestsRequest = new OrderTableUpdateGuestsRequest(10);

        OrderTableResponse orderTable = tableService.changeNumberOfGuests(2L, orderTableUpdateGuestsRequest);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(10);
    }
    @DisplayName("주문이 가능하지 않은 테이블의 인원 수를 바꿀 수 없다")
    @Test
    void changeNumberOfGuests_EmptyFalse() {
        preprocessWhenCreate(new OrderTable(2L, null, 2, true));
        OrderTableUpdateGuestsRequest orderTableUpdateGuestsRequest = new OrderTableUpdateGuestsRequest(10);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTableUpdateGuestsRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @DisplayName("테이블의 인원 수를 음수로 바꿀 수 없다")
    @Test
    void changeNumberOfGuests_numberOfGuestNegative() {
        preprocessWhenCreate(new OrderTable(2L, null, 2, false));
        OrderTableUpdateGuestsRequest orderTableUpdateGuestsRequest = new OrderTableUpdateGuestsRequest(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(2L, orderTableUpdateGuestsRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private void preprocessWhenCreate(OrderTable orderTable) {
        orderTableDao.save(orderTable);
    }

    private void preprocessWhenList(int count) {
        for (int i = 0; i < count; i++) {
            orderTableDao.save(new OrderTable(3, false));
        }
    }

    private void preprocessWhenChangeEmpty(OrderTable orderTable, List<Order> orders) {
        orderTableDao.save(orderTable);
        orders.forEach(orderDao::save);
    }
}

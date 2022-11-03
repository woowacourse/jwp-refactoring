package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.ServiceTest;
import kitchenpos.order.ui.request.OrderTableCreateReqeust;
import kitchenpos.order.response.OrderTableResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableServiceTest extends ServiceTest {

    @Test
    @DisplayName("테이블을 생성한다.")
    void createTable() {
        final int actualNumberOfGuests = 2;
        final OrderTableCreateReqeust request = new OrderTableCreateReqeust(actualNumberOfGuests, true);

        final OrderTableResponse actual = tableService.create(request);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(actualNumberOfGuests),
                () -> assertThat(actual.isEmpty()).isTrue()
        );
    }

    @Test
    @DisplayName("테이블 목록들을 조회한다.")
    void getTables() {
        final List<OrderTableResponse> orderTables = tableService.list();

        assertThat(orderTables).hasSize(2);
    }

    @Test
    @DisplayName("테이블의 상태를 변경한다.")
    void changeEmptyTable() {
        final OrderTable orderTable = orderTableDao.findById(1L)
                .orElseThrow();

        final OrderTableResponse changedOrderTable = tableService.changeEmpty(orderTable.getId(), false);

        assertThat(changedOrderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("주문 테이블의 주문의 상태가 조리 상태면 예외 발생")
    void whenOrderTableWithCookingStatus() {
        final OrderTable savedOrderTableId = orderTableDao.save(new OrderTable(null, 3, false));
        orderDao.save(Order.of(savedOrderTableId.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(),
                List.of(new OrderLineItem(1L, 1L))));

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTableId.getId(), true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 주문의 상태가 식사 상태면 예외 발생")
    void whenOrderTableWithMealStatus() {
        final OrderTable savedOrderTableId = orderTableDao.save(new OrderTable(null, 3, false));
        orderDao.save(Order.of(savedOrderTableId.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(),
                List.of(new OrderLineItem(1L, 1L))));

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTableId.getId(), true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 손님 수를 변경한다.")
    void changeNumberOrGuests() {
        final OrderTable savedOrderTableId = orderTableDao.save(new OrderTable(null, 3, false));

        final OrderTableResponse changedOrderTable = tableService.changeNumberOfGuests(savedOrderTableId.getId(), 5);

        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(5);
    }
}

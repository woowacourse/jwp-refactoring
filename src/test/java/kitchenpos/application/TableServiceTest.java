package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderRepository;
import kitchenpos.ui.request.TableCreateRequest;
import kitchenpos.ui.request.TableEmptyUpdateRequest;
import kitchenpos.ui.request.TableGuestUpdateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ServiceTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void 주문_테이블을_생성한다() {
        // given
        OrderTable orderTable = new OrderTable(null, 3, true);
        TableCreateRequest tableCreateRequest =
                new TableCreateRequest(3, true);


        // when
        OrderTable createdOrderTable = tableService.create(tableCreateRequest);

        // then
        assertThat(createdOrderTable).usingRecursiveComparison()
                .ignoringFields("id", "tableGroupId").isEqualTo(orderTable);
    }

    @Test
    void 주문_테이블_리스트를_조회한다() {
        // given
        OrderTable orderTable = new OrderTable(null, 3, true);
        TableCreateRequest tableCreateRequest =
                new TableCreateRequest(3, true);
        tableService.create(tableCreateRequest);

        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).usingRecursiveComparison()
                .ignoringFields("id", "tableGroup")
                .isEqualTo(List.of(new OrderTable(null, 0, true), orderTable));
    }

    @Test
    void 주문_테이블의_empty_상태를_변경한다() {
        // given
        OrderTable orderTable = tableService.create(new TableCreateRequest(3, true));

        // when
        OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(), new TableEmptyUpdateRequest(false));

        // then
        assertThat(changedOrderTable.isEmpty()).isFalse();
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"MEAL", "COOKING"})
    void 주문_테이블의_주문_상태가_MEAL_이나_COOKING이면_empty_상태_변경_요청시_예외_발생(OrderStatus status) {
        // given
        OrderTable orderTable = tableService.create(new TableCreateRequest(3, true));

        orderRepository.save(new Order(null, orderTable, status, LocalDateTime.now(), List.of(new OrderLineItem())));

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new TableEmptyUpdateRequest(false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블의_게스트_인원_수를_변경한다() {
        // given
        OrderTable orderTable = tableService.create(new TableCreateRequest(3, false));

        // when
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(),
                new TableGuestUpdateRequest(10));

        // then
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(10);
    }

    @Test
    void 변경할_게스트_인원이_음수면_인원_수_변경_요청_시_예외_발생() {
        // given
        OrderTable orderTable = tableService.create(new TableCreateRequest(3, false));

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(),
                new TableGuestUpdateRequest(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블의_상태가_빈_상태면_게스트_인원_수_변경_요청_시_예외_발생() {
        // given
        OrderTable orderTable = tableService.create(new TableCreateRequest(3, true));

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(),
                new TableGuestUpdateRequest(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

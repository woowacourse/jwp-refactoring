package kitchenpos.application;

import kitchenpos.Order.domain.OrderStatus;
import kitchenpos.OrderTable.application.TableGroupService;
import kitchenpos.OrderTable.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class TableServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("OrderTable을 추가할 수 있다.")
    public void enrollOrderTable() {
        //given & when
        OrderTable orderTable = new OrderTable(10, false);

        //then
        assertDoesNotThrow(() -> tableService.create(orderTable));
    }

    @Test
    @DisplayName("등록된 OrderTable을 조회할 수 있다.")
    public void findAll() {
        //given
        List<OrderTable> orderTables = tableService.list();

        //when & then
        assertThat(orderTables).hasSize(4);
    }

    @Test
    @DisplayName("OrderTable Empty 수정 시, 존재하지않은 OrderTable Id가 주어져서는 안된다.")
    public void notExistOrderTableIdException() {
        assertThatThrownBy(() -> tableService.changeEmpty(10L, false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("TableGroup에 속한 OrderTable의 Empty는 수정할 수 없다.")
    public void cannotChangeTableStatusIncludedInTableGroup() {
        //given
        List<Long> orderTableIds = emptyTrueOrderTableIds();
        tableGroupService.create(orderTableIds);

        //when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableIds.get(0), true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("OrderTable이 현재 이용 중이면(COMPLETION이 아니면) 상태를 변경할 수 없다.")
    public void cannotChangeTableStatusWhenOrderActivated() {
        //given
        Long orderTableId = orderService.list().get(0).getOrderTableId();

        //when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("OrderTable이 COMPLETION이면 상태를 변경할 수 있다.")
    public void changeTableStatusWhenOrderCompleted() {
        //given
        Long orderTableId = emptyFalseOrderTableIds().get(0);

        //when
        orderService.changeOrderStatus(order.getId(), OrderStatus.COMPLETION.name());

        //then
        assertDoesNotThrow(() -> tableService.changeEmpty(orderTableId, true));
    }

    @Test
    @DisplayName("OrderTable의 Empty 여부를 수정할 수 있다.")
    public void updateEmptyStatus() {
        assertDoesNotThrow(() -> tableService.changeEmpty(emptyTrueOrderTableIds().get(0), false));
    }

    @Test
    @DisplayName("NumberOfGuests를 0 미만의 값으로 수정할 수 없다.")
    public void cannotChangeNumberOfGuestsUnderZero() {
        //given & when
        Long orderTableId = orderService.list().get(0).getOrderTableId();

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, -1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("NumberOfGuests 수정 시, 존재하지않은 OrderTable Id가 주어져서는 안된다.")
    public void cannotChangeNumberOfGuestsWhenNonExistOrderTableId() {
        //given & when
        Long invalidOrderTableId = 100L;

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(invalidOrderTableId, 5))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("NumberOfGuests 수정 시, empty가 true인 OrderTable이어서는 안된다.")
    public void cannotChangeNumberOfGuestsWhenEmptyTable() {
        //given & when
        Long orderTableId = emptyTrueOrderTableIds().get(0);

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, 5))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("OrderTable의 NumberOfGuests를 수정할 수 있다.")
    public void updateNumberOfGuests() {
        //given
        Long orderTableId = orderService.list().get(0).getOrderTableId();

        //then
        assertDoesNotThrow(() -> tableService.changeNumberOfGuests(orderTableId, 5));
    }
}

package kitchenpos.application;

import kitchenpos.dto.OrderTableChangeEmptyRequest;
import kitchenpos.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableId;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.TableGroupCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class TableServiceTest extends ServiceTest {

    private OrderTableCreateRequest orderTableCreateRequest;

    @BeforeEach
    void setUp() {
        orderTableCreateRequest = makeOrderTableCreate();
    }

    @Test
    void 주문_테이블을_생성한다() {
        OrderTableResponse response = tableService.create(orderTableCreateRequest);
        assertAll(
                () -> assertThat(response.getNumberOfGuests()).isEqualTo(orderTableCreateRequest.getNumberOfGuests()),
                () -> assertThat(response.isEmpty()).isEqualTo(orderTableCreateRequest.getEmpty())
        );
    }

    @Test
    void 주문_테이블_목록을_조회한다() {

        List<OrderTableResponse> responses = tableService.list();

        assertThat(responses.size()).isEqualTo(8);
    }

    @Test
    void 주문_테이블에서_주문을_받을_수_있는지_여부를_변경한다() {

        OrderTableResponse response = tableService.changeEmpty(1L, makeOrderTableChangeEmptyRequest(false));

        assertThat(response.isEmpty()).isFalse();
    }

    @Test
    void 잘못된_주문테이블_id_조회시_예외발생() {
        assertThatThrownBy(
                () -> tableService.changeEmpty(11L, makeOrderTableChangeEmptyRequest(false))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹에_속한_주문테이블_상태변경시_예외발생() {
        tableGroupService.create(makeTableGroupCreate());

        assertThatThrownBy(
                () -> tableService.changeEmpty(1L, makeOrderTableChangeEmptyRequest(false))
        ).isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void 주문_참여_인원을_변경한다() {
        tableService.changeEmpty(1L, makeOrderTableChangeEmptyRequest(false));
        OrderTableResponse response = tableService.changeNumberOfGuests(1L, makeOrderTableGuestNumberChange(1));
        assertThat(response.getNumberOfGuests()).isEqualTo(1);
    }

    @Test
    void 주문_불가상태_주문테이블_참여인원_변경시_예외발생() {
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(1L, makeOrderTableGuestNumberChange(1))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_참여인원을_0미만으로_변경시_예외발생() {
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(1L, makeOrderTableGuestNumberChange(-1))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTableCreateRequest makeOrderTableCreate() {
        return new OrderTableCreateRequest(5, true);
    }

    private OrderTableChangeEmptyRequest makeOrderTableChangeEmptyRequest(boolean empty) {
        return new OrderTableChangeEmptyRequest(empty);
    }

    private TableGroupCreateRequest makeTableGroupCreate() {
        return new TableGroupCreateRequest(List.of(
                new OrderTableId(1L),
                new OrderTableId(2L)));
    }

    private OrderTableChangeNumberOfGuestsRequest makeOrderTableGuestNumberChange(int num) {
        return new OrderTableChangeNumberOfGuestsRequest(num);
    }
}

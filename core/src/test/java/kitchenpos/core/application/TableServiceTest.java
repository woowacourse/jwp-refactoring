package kitchenpos.core.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Objects;
import kitchenpos.core.IntegrationTest;
import kitchenpos.core.domain.ordertable.OrderTable;
import kitchenpos.core.dto.request.OrderTableCreateRequest;
import kitchenpos.core.dto.request.OrderTableUpdateEmptyRequest;
import kitchenpos.core.dto.request.OrderTableUpdateNumberOfGuestsRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest extends IntegrationTest {

    @Autowired
    private TableService tableService;

    @Test
    @DisplayName("주문 테이블 등록 시 전달받은 정보를 새 id로 저장한다.")
    void 주문_테이블_등록_성공_저장() {
        // given
        final OrderTable saved = tableService.create(new OrderTableCreateRequest(0, true));

        // expected
        assertThat(tableService.list())
                .map(OrderTable::getId)
                .filteredOn(id -> Objects.equals(id, saved.getId()))
                .hasSize(1);
    }

    @Test
    @DisplayName("모든 주문 테이블은 등록 시점에는 소속된 단체가 없다.")
    void 주문_테이블_등록_성공_단체_미지정() {
        // given
        final OrderTable saved = tableService.create(new OrderTableCreateRequest(0, false));

        // expected
        assertThat(saved.getTableGroupId()).isNull();
    }

    @Test
    @DisplayName("특정 테이블을 빈 테이블로 변경하면 해당하는 id의 정보를 업데이트한다.")
    void 주문_테이블_비우기_성공_빈_테이블_여부() {
        // given
        final OrderTable table = tableService.create(new OrderTableCreateRequest(0, false));

        // when
        tableService.changeEmpty(table.getId(), new OrderTableUpdateEmptyRequest(true));

        // then
        assertThat(table.isEmpty()).isTrue();
        assertThat(tableService.list())
                .filteredOn(found -> Objects.equals(found.getId(), table.getId()))
                .filteredOn(OrderTable::isEmpty)
                .hasSize(1);
    }

    @Test
    @DisplayName("존재하지 않는 테이블을 비울 수 없다.")
    void 주문_테이블_비우기_실패_존재하지_않는_테이블() {
        assertThatThrownBy(() -> tableService.changeEmpty(-1L, new OrderTableUpdateEmptyRequest(true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("특정 주문 테이블의 방문한 손님 수를 변경할 수 있다.")
    void 주문_테이블_방문한_손님수_변경_성공() {
        // given
        final OrderTable table = tableService.create(new OrderTableCreateRequest(0, false));

        // when
        int numberOfGuests = 10;
        OrderTableUpdateNumberOfGuestsRequest request = new OrderTableUpdateNumberOfGuestsRequest(10);
        table.changeNumberOfGuests(numberOfGuests);
        tableService.changeNumberOfGuests(table.getId(), request);

        // then
        assertThat(table.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
        assertThat(tableService.list())
                .filteredOn(found -> Objects.equals(found.getId(), table.getId()))
                .filteredOn(found -> Objects.equals(found.getNumberOfGuests(), request.getNumberOfGuests()))
                .hasSize(1);
    }

    @Test
    @DisplayName("존재하지 않는 테이블의 손님 수를 변경할 수 없다.")
    void 주문_테이블_방문한_손님수_변경_실패() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(-1L, new OrderTableUpdateNumberOfGuestsRequest(10)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

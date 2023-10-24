package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.List;
import kitchenpos.common.annotation.ServiceTest;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.presentation.dto.ChangeEmptyRequest;
import kitchenpos.order.presentation.dto.ChangeNumberOfGuestsRequest;
import kitchenpos.order.presentation.dto.CreateOrderTableRequest;
import kitchenpos.support.TestSupporter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class OrderTableServiceTest {

    @Autowired
    private OrderTableService orderTableService;

    @Autowired
    private TestSupporter testSupporter;

    @Test
    void 주문_테이블을_생성한다() {
        // given
        final CreateOrderTableRequest createOrderTableRequest = new CreateOrderTableRequest(0, false);

        // when
        final OrderTable orderTable = orderTableService.create(createOrderTableRequest);

        // then
        assertThat(orderTable).isNotNull();
    }

    @Test
    void 주문_테이블에_대해_전체_조회한다() {
        // given
        final OrderTable orderTable = testSupporter.createOrderTable();

        // when
        final List<OrderTable> orderTables = orderTableService.list();

        // then
        assertThat(orderTables.get(0)).isEqualTo(orderTable);
    }

    @Test
    void 주문_테이블의_empty_상태를_변경한다() {
        // given
        final OrderTable orderTable = testSupporter.createOrderTable(false);
        final ChangeEmptyRequest changeEmptyRequest = new ChangeEmptyRequest(true);

        // when
        final OrderTable actual = orderTableService.changeEmpty(orderTable.getId(),
                                                                changeEmptyRequest);

        // then
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    void 주문_테이블의_방문_손님_수를_변경한다() {
        // given
        final OrderTable orderTable = testSupporter.createOrderTable();
        final ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest = new ChangeNumberOfGuestsRequest(100);

        // when
        final OrderTable actual = orderTableService.changeNumberOfGuests(orderTable.getId(),
                                                                         changeNumberOfGuestsRequest);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(100);
    }

    @ParameterizedTest(name = "주문 상태가 {0}일때 예외가 발생한다.")
    @ValueSource(strings = {"COOKING", "MEAL"})
    void 주문_테이블의_empty_속성을_변경할_때_주문_상태가_COMPLETION_이_아니라면_예외가_발생한다(final String orderStatus) {
        // given
        final OrderTable orderTable = testSupporter.createOrderTable(false);
        testSupporter.createOrder(orderTable);

        final ChangeEmptyRequest changeEmptyRequest = new ChangeEmptyRequest(true);

        // when & then
        assertThatThrownBy(() -> orderTableService.changeEmpty(orderTable.getId(), changeEmptyRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("진행중인 주문이 있어, 주문 테이블의 상태를 변경할 수 없습니다.");
    }

    @Test
    void 주문_테이블의_방문한_손님_수가_음수라면_예외가_발생한다() {
        // given
        final OrderTable orderTable = testSupporter.createOrderTable(false);
        final ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest = new ChangeNumberOfGuestsRequest(-100);

        // when & then
        assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(orderTable.getId(), changeNumberOfGuestsRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("방문 손님 수는 음수가 될 수 없습니다.");
    }

    @Test
    void 비어있는_주문_테이블의_방문한_손님_수를_변경하면_예외가_발생한다() {
        // given
        final OrderTable orderTable = testSupporter.createOrderTable(true);
        final ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest = new ChangeNumberOfGuestsRequest(100);

        // when & then
        assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(orderTable.getId(), changeNumberOfGuestsRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 상태의 주문 테이블에서는 방문 손님 수를 변경할 수 없습니다.");
    }
}

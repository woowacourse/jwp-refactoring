package kitchenpos.application;

import java.util.List;
import kitchenpos.fixtures.Fixtures;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.TableService;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceTest {

    @Autowired
    Fixtures fixtures;

    @Autowired
    TableService tableService;

    @Test
    void 주문_테이블을_등록한다() {
        // given
        OrderTableRequest request = new OrderTableRequest(0, false);

        // when
        OrderTableResponse result = tableService.create(request);

        // then
        Assertions.assertThat(result.getId()).isNotNull();
        Assertions.assertThat(result.getNumberOfGuests()).isEqualTo(0);
        Assertions.assertThat(result.isEmpty()).isFalse();
    }

    @Test
    void 모든_주문_테이블_목록을_불러온다() {
        // given
        fixtures.주문_테이블_저장();

        // when
        List<OrderTableResponse> results = tableService.list();

        // then
        Assertions.assertThat(results).hasSize(1);
    }

    @Nested
    class 빈_테이블_설정 {

        @Test
        void 주문_테이블을_빈_테이블로_설정한다() {
            // given
            OrderTable orderTable = fixtures.주문_테이블_저장();

            OrderTableRequest request = new OrderTableRequest(0, true);

            // when
            OrderTableResponse result = tableService.changeEmpty(orderTable.getId(), request);

            // then
            Assertions.assertThat(result.getId()).isEqualTo(orderTable.getId());
            Assertions.assertThat(result.isEmpty()).isTrue();
        }

        @Test
        void 존재하지_않는_주문_테이블을_설정하는_경우_예외가_발생한다() {
            // given
            OrderTableRequest request = new OrderTableRequest(0, true);

            // when, then
            Assertions.assertThatThrownBy(() -> tableService.changeEmpty(-1L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

//        @ParameterizedTest
//        @EnumSource(value = OrderStatus.class, names = {"COMPLETION"}, mode = Mode.EXCLUDE)
//        void 주문_테이블의_상태가_계산완료_상태가_아닌_경우_예외가_발생한다(OrderStatus orderStatus) {
//            // given
//            OrderTable orderTable = fixtures.주문_테이블_저장();
//            fixtures.주문_저장(orderTable, orderStatus);
//
//            OrderTableRequest request = new OrderTableRequest(0, true);
//
//            // when, then
//            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), request))
//                    .isInstanceOf(IllegalArgumentException.class);
//        }
    }

    @Nested
    class 방문한_손님_수_설정 {

        @Test
        void 방문한_손님_수를_설정한다() {
            // given
            OrderTable orderTable = fixtures.주문_테이블_저장();

            OrderTableRequest request = new OrderTableRequest(2, null);

            // when
            OrderTableResponse result = tableService.changeNumberOfGuests(orderTable.getId(), request);

            // then
            Assertions.assertThat(result.getId()).isEqualTo(orderTable.getId());
            Assertions.assertThat(result.getNumberOfGuests()).isEqualTo(2);
        }

        @Test
        void 존재하지_않는_주문_테이블을_설정하는_경우_예외가_발생한다() {
            // given
            OrderTableRequest request = new OrderTableRequest(2, null);

            // when, then
            Assertions.assertThatThrownBy(
                            () -> tableService.changeNumberOfGuests(-1L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }
}

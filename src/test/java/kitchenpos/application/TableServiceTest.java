package kitchenpos.application;

import kitchenpos.application.dto.request.CreateOrderTableRequest;
import kitchenpos.application.dto.request.UpdateOrderTableEmptyRequest;
import kitchenpos.application.dto.request.UpdateOrderTableGuestsRequest;
import kitchenpos.application.dto.response.CreateOrderTableResponse;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE;
import static kitchenpos.fixture.OrderTableFixture.REQUEST;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @Nested
    class 주문_테이블_생성 {

        @Test
        void 주문_테이블을_생성한다() {
            // given
            CreateOrderTableRequest request = REQUEST.주문_테이블_생성_요청_3명();
            OrderTable orderTable = ORDER_TABLE.주문_테이블_1();
            given(orderTableDao.save(any(OrderTable.class)))
                    .willReturn(orderTable);

            // when
            CreateOrderTableResponse result = tableService.create(request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(result.getId()).isEqualTo(orderTable.getId());
                softly.assertThat(result.getTableGroupId()).isEqualTo(orderTable.getTableGroupId());
                softly.assertThat(result.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
                softly.assertThat(result.isEmpty()).isEqualTo(orderTable.isEmpty());
            });
        }
    }

    @Nested
    class 주문_테이블_목록_조회 {

        @Test
        void 주문_테이블_목록을_조회한다() {
            // given
            OrderTable orderTable = ORDER_TABLE.주문_테이블_1();
            given(orderTableDao.findAll())
                    .willReturn(List.of(orderTable));

            // when
            List<OrderTableResponse> result = tableService.list();

            // then
            assertSoftly(softly -> {
                softly.assertThat(result).hasSize(1);
                softly.assertThat(result.get(0).getId()).isEqualTo(orderTable.getId());
                softly.assertThat(result.get(0).getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
                softly.assertThat(result.get(0).isEmpty()).isEqualTo(orderTable.isEmpty());
            });
        }
    }

    @Nested
    class 테이블_상태_변경 {

        @Test
        void 빈_테이블_상태를_변경한다() {
            // given
            UpdateOrderTableEmptyRequest request = REQUEST.주문_테이블_비움_요청();
            OrderTable orderTable = ORDER_TABLE.비어있는_테이블();
            given(orderTableDao.findById(any()))
                    .willReturn(Optional.of(orderTable));
            given(orderTableDao.save(any()))
                    .willReturn(orderTable);

            // when
            OrderTableResponse result = tableService.changeEmpty(1L, request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(result.getId()).isEqualTo(orderTable.getId());
                softly.assertThat(result.getTableGroupId()).isEqualTo(orderTable.getTableGroupId());
                softly.assertThat(result.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
                softly.assertThat(result.isEmpty()).isEqualTo(result.isEmpty());
            });
        }

        @Test
        void 테이블이_존재하지_않으면_예외() {
            // given
            UpdateOrderTableEmptyRequest request = REQUEST.주문_테이블_비움_요청();
            given(orderTableDao.findById(any()))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(1L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 빈_테이블_상태를_변경할_때_테이블_그룹에_속해있으면_예외() {
            // given
            UpdateOrderTableEmptyRequest request = REQUEST.주문_테이블_비움_요청();
            OrderTable orderTable = ORDER_TABLE.주문_테이블_1_비어있는가(true);
            given(orderTableDao.findById(any()))
                    .willReturn(Optional.of(orderTable));

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(1L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 빈_테이블_상태를_변경할_때_주문_상태가_조리중_또는_식사중이면_예외() {
            // given
            UpdateOrderTableEmptyRequest request = REQUEST.주문_테이블_비움_요청();
            OrderTable orderTable = ORDER_TABLE.비어있는_테이블();
            given(orderTableDao.findById(anyLong()))
                    .willReturn(Optional.of(orderTable));
            given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
                    .willReturn(true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(1L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 방문한_손님_수_변경 {

        @Test
        void 방문한_손님_수를_변경한다() {
            // given
            UpdateOrderTableGuestsRequest request = REQUEST.주문_테이블_인원_변경_요청(133);
            OrderTable orderTable = ORDER_TABLE.주문_테이블_1_비어있는가(false);
            given(orderTableDao.findById(anyLong()))
                    .willReturn(Optional.of(orderTable));
            given(orderTableDao.save(any(OrderTable.class)))
                    .willReturn(orderTable.updateNumberOfGuests(133));

            // when
            OrderTableResponse result = tableService.changeNumberOfGuests(1L, request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(result.getId()).isEqualTo(orderTable.getId());
                softly.assertThat(result.getTableGroupId()).isEqualTo(orderTable.getTableGroupId());
                softly.assertThat(result.getNumberOfGuests()).isEqualTo(result.getNumberOfGuests());
                softly.assertThat(result.isEmpty()).isEqualTo(result.isEmpty());
            });
        }

        @Test
        void 방문한_손님_수를_변경할_때_손님_수가_음수이면_예외() {
            // given
            UpdateOrderTableGuestsRequest request = REQUEST.주문_테이블_인원_변경_요청(-1);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 방문한_손님_수를_변경할_때_테이블이_비어있으면_예외() {
            // given
            UpdateOrderTableGuestsRequest request = REQUEST.주문_테이블_인원_변경_요청(33);
            OrderTable orderTable = ORDER_TABLE.주문_테이블_1_비어있는가(true);
            given(orderTableDao.findById(any()))
                    .willReturn(Optional.of(orderTable));

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}

package kitchenpos.application;

import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.helper.IntegrationTestHelper;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.application.dto.OrderTableChangeEmptyRequest;
import kitchenpos.ordertable.application.dto.OrderTableChangeNumberOfGuestRequest;
import kitchenpos.ordertable.application.dto.OrderTableCreateRequest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.exception.CannotChangeNumberOfGuestBecauseOfEmptyTableException;
import kitchenpos.ordertable.exception.CannotUnGroupBecauseOfStatusException;
import kitchenpos.ordertable.exception.NumberOfGuestsInvalidException;
import kitchenpos.ordertable.exception.OrderTableNotFoundException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.fixture.OrderFixture.주문_생성;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_상태_업데이트_요청;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_생성;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_생성_요청;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_손님_수_업데이트_요청;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest extends IntegrationTestHelper {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void 주문_테이블을_생성한다() {
        // given
        OrderTable orderTable = 주문_테이블_생성(null, 10, false);
        OrderTableCreateRequest req = 주문_테이블_생성_요청(orderTable);

        // when
        OrderTable result = tableService.create(req);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getNumberOfGuests()).isEqualTo(10);
            softly.assertThat(result.isEmpty()).isEqualTo(false);
        });
    }

    @Test
    void 모든_주문_테이블을_반환한다() {
        // given
        OrderTable orderTable = 주문_테이블_생성(null, 10, false);
        OrderTableCreateRequest req = 주문_테이블_생성_요청(orderTable);
        OrderTable savedOrderTable = tableService.create(req);

        // when
        List<OrderTable> result = tableService.list();

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).hasSize(1);
            softly.assertThat(result.get(0)).usingRecursiveComparison()
                    .isEqualTo(savedOrderTable);
        });
    }

    @Test
    void 주문_테이블을_빈_상태로_변경한다() {
        // given
        OrderTableCreateRequest req = 주문_테이블_생성_요청(주문_테이블_생성(null, 1, false));
        OrderTable orderTable = tableService.create(req);

        OrderTableChangeEmptyRequest changedTableRequest = 주문_테이블_상태_업데이트_요청(주문_테이블_생성(null, 1, true));

        // when
        OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(), changedTableRequest);

        // then
        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @Test
    void 주문_테이블을_빈_상태로_변경시에_주문_테이블이_없다면_예외를_발생한다() {
        // given
        Long invalidOrderTableId = -1L;

        OrderTable orderTable = tableService.create(주문_테이블_생성_요청(주문_테이블_생성(null, 1, false)));
        OrderTableChangeEmptyRequest changedTableRequest = 주문_테이블_상태_업데이트_요청(주문_테이블_생성(null, 1, true));

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(invalidOrderTableId, changedTableRequest))
                .isInstanceOf(OrderTableNotFoundException.class);
    }

    @Test
    void 주문_테이블을_빈_상태로_변경시에_밥을_먹는중이라면_예외를_발생시킨다() {
        // given
        OrderTable orderTable = tableService.create(주문_테이블_생성_요청(주문_테이블_생성(null, 1, false)));
        OrderTableChangeEmptyRequest changedTableRequest = 주문_테이블_상태_업데이트_요청(주문_테이블_생성(null, 1, true));
        orderRepository.save(주문_생성(orderTable, COOKING.name(), LocalDateTime.now(), List.of(
                OrderLineItemFixture.주문_품목_생성(null, 10L)
        )));

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), changedTableRequest))
                .isInstanceOf(CannotUnGroupBecauseOfStatusException.class);
    }

    @Test
    void 주문_테이블의_손님_수를_변경한다() {
        // given
        OrderTable orderTable = tableService.create(주문_테이블_생성_요청(주문_테이블_생성(null, 1, false)));
        OrderTableChangeNumberOfGuestRequest changeTableRequest = 주문_테이블_손님_수_업데이트_요청(주문_테이블_생성(null, 10, true));


        // when
        OrderTable result = tableService.changeNumberOfGuests(orderTable.getId(), changeTableRequest);

        // then
        assertThat(result.getNumberOfGuests()).isEqualTo(changeTableRequest.getNumberOfGuests());
    }

    @Test
    void 변경하려는_주문_테이블의_손님_수가_0보다_작다면_예외를_발생시킨다() {
        // given
        OrderTable orderTable = tableService.create(주문_테이블_생성_요청(주문_테이블_생성(null, 1, false)));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), 주문_테이블_손님_수_업데이트_요청(주문_테이블_생성(null, -1, true))))
                .isInstanceOf(NumberOfGuestsInvalidException.class);
    }

    @Test
    void 인원_변경하려는_주문_테이블이_빈_테이블이면_예외를_발생시킨다() {
        // given
        OrderTable orderTable = tableService.create(주문_테이블_생성_요청(주문_테이블_생성(null, 0, true)));
        OrderTableChangeNumberOfGuestRequest changeTableRequest = 주문_테이블_손님_수_업데이트_요청(주문_테이블_생성(null, 0, false));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), changeTableRequest))
                .isInstanceOf(CannotChangeNumberOfGuestBecauseOfEmptyTableException.class);
    }
}

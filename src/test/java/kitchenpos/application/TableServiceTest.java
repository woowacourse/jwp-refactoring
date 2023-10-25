package kitchenpos.application;

import static java.lang.Long.MAX_VALUE;
import static kitchenpos.fixture.OrderTableFixture.단체_지정이_없는_주문_테이블_생성;
import static kitchenpos.fixture.OrderTableFixture.단체_지정이_있는_주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.TableGroupFixture;
import kitchenpos.ui.request.OrderTableCreateRequest;
import kitchenpos.ui.request.OrderTableUpdateEmptyRequest;
import kitchenpos.ui.request.OrderTableUpdateNumberOfGuestsRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest extends ServiceIntegrationTest {

    @Autowired
    private TableService tableService;

    @Test
    void 주문_테이블_생성에_성공한다() {
        // given
        OrderTableCreateRequest request = new OrderTableCreateRequest(1, false);

        // when
        Long orderTableId = tableService.create(request)
                .getId();

        // then
        OrderTable actual = orderTableRepository.findById(orderTableId).get();
        assertAll(
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(1),
                () -> assertThat(actual.isEmpty()).isFalse()
        );
    }

    @Test
    void 주문_테이블_목록을_반환한다() {
        // given
        List<OrderTable> orderTables = List.of(
                단체_지정이_없는_주문_테이블_생성(1, false),
                단체_지정이_없는_주문_테이블_생성(1, false),
                단체_지정이_없는_주문_테이블_생성(1, false)
        );
        List<OrderTable> expected = new ArrayList<>();
        for (OrderTable orderTable : orderTables) {
            OrderTableCreateRequest request = new OrderTableCreateRequest(
                    orderTable.getNumberOfGuests(),
                    orderTable.isEmpty()
            );
            expected.add(tableService.create(request));
        }

        // when
        List<OrderTable> actual = tableService.list();

        // then
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    void 저장되어_있지_않은_주문_테이블의_empty를_변경하는_경우_실패한다() {
        // given
        OrderTableUpdateEmptyRequest request = new OrderTableUpdateEmptyRequest(false);

        // expect
        assertThatThrownBy(() -> tableService.changeEmpty(MAX_VALUE, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_이미_단체_지정에_속해_있으면_empty_변경이_안된다() {
        // given
        TableGroup savedTableGroup = tableGroupRepository.save(TableGroupFixture.빈_테이블_그룹_생성());
        OrderTable orderTable = 단체_지정이_있는_주문_테이블_생성(
                savedTableGroup,
                1,
                true
        );
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        OrderTableUpdateEmptyRequest request = new OrderTableUpdateEmptyRequest(false);

        // expect
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블에_속해_있는_주문_중_단_하나라도_요리중이면_empty_변경이_안된다() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(
                단체_지정이_없는_주문_테이블_생성(1, false)
        );
        Order order = 주문을_저장하고_반환받는다(savedOrderTable);
        주문의_상태를_변환한다(order, OrderStatus.COOKING);
        OrderTableUpdateEmptyRequest request = new OrderTableUpdateEmptyRequest(true);

        // expect
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블에_속해_있는_주문_중_단_하나라도_식사중이면_empty_변경이_안된다() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(단체_지정이_없는_주문_테이블_생성(1, false));
        Order order = 주문을_저장하고_반환받는다(savedOrderTable);
        주문의_상태를_변환한다(order, OrderStatus.MEAL);
        OrderTableUpdateEmptyRequest request = new OrderTableUpdateEmptyRequest(true);

        // expect
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블_empty를_성공적으로_변경한다() {
        // given
        OrderTable orderTable = 단체_지정이_없는_주문_테이블_생성(
                1,
                false
        );
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        Order order = 주문을_저장하고_반환받는다(savedOrderTable);
        order.changeOrderStatus(OrderStatus.COMPLETION);
        orderRepository.save(order);
        OrderTableUpdateEmptyRequest request = new OrderTableUpdateEmptyRequest(true);

        // when
        tableService.changeEmpty(savedOrderTable.getId(), request);

        // then
        OrderTable actual = orderTableRepository.findById(savedOrderTable.getId()).get();
        assertAll(
                () -> assertThat(actual.getNumberOfGuests()).isOne(),
                () -> assertThat(actual.isEmpty()).isTrue()
        );
    }

    @Test
    void 존재하지_않는_주문_테이블은_numberOfGuest를_변경할_수_없다() {
        // given
        OrderTableUpdateNumberOfGuestsRequest request = new OrderTableUpdateNumberOfGuestsRequest(1);

        // expect
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(MAX_VALUE, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문이_불가능한_상태의_주문_테이블은_numberOfGuest_를_변경할_수_없다() {
        // given
        OrderTable originalOrderTable = 단체_지정이_없는_주문_테이블_생성(
                1,
                true
        );
        OrderTable savedOrderTable = orderTableRepository.save(originalOrderTable);
        OrderTableUpdateNumberOfGuestsRequest request = new OrderTableUpdateNumberOfGuestsRequest(2);

        // expect
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블의_numberOfGuest를_0_미만으로_바꿀_수_없다() {
        // given
        OrderTable originalOrderTable = 단체_지정이_없는_주문_테이블_생성(
                1,
                false
        );
        OrderTable savedOrderTable = orderTableRepository.save(originalOrderTable);
        OrderTableUpdateNumberOfGuestsRequest request = new OrderTableUpdateNumberOfGuestsRequest(-1);

        // expect
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블의_numberOfGuest를_성공적으로_변경한다() {
        // given
        OrderTable originalOrderTable = 단체_지정이_없는_주문_테이블_생성(
                1,
                false
        );
        OrderTable savedOrderTable = orderTableRepository.save(originalOrderTable);
        OrderTableUpdateNumberOfGuestsRequest request = new OrderTableUpdateNumberOfGuestsRequest(100);

        // when
        tableService.changeNumberOfGuests(savedOrderTable.getId(), request);

        // then
        OrderTable actual = orderTableRepository.findById(savedOrderTable.getId()).get();
        assertAll(
                () -> assertThat(actual).usingRecursiveComparison()
                        .ignoringFields("numberOfGuests")
                        .isEqualTo(savedOrderTable),
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests())
        );
    }

}

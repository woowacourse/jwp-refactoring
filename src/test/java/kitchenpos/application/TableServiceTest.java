package kitchenpos.application;

import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.ordertable.ui.request.OrderTableRequest;
import kitchenpos.ordertable.ui.request.UpdateOrderTableEmptyRequest;
import kitchenpos.ordertable.ui.request.UpdateOrderTableNumberOfGuestsRequest;
import kitchenpos.ordertable.ui.response.OrderTableResponse;
import kitchenpos.tablegroup.domain.repository.TableGroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableServiceTest extends ServiceTest {

    @Autowired
    TableService tableService;

    @Autowired
    OrderTableRepository orderTableRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    TableGroupRepository tableGroupRepository;

    @Test
    void 테이블그룹을_저장한다() {
        final OrderTableRequest orderTableRequest = new OrderTableRequest(10, false);

        final OrderTableResponse created = tableService.create(orderTableRequest);

        assertThat(created.getId()).isNotNull();
    }

    @Test
    void 모든_주문테이블을_조회한다() {
        final List<OrderTableResponse> expected = orderTableRepository.findAll().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());

        final List<OrderTableResponse> actual = tableService.list();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void 주문테이블을_빈_테이블로_바꾼다() {
        final UpdateOrderTableEmptyRequest request = new UpdateOrderTableEmptyRequest(true);

        final OrderTableResponse edit = tableService.changeEmpty(10L, request);

        assertThat(edit.isEmpty()).isTrue();
    }

    @Test
    void 테이블그룹에_속한_상태로_빈_테이블로_바꾸면_예외를_발생한다() {
        final UpdateOrderTableEmptyRequest request = new UpdateOrderTableEmptyRequest(true);

        assertThatThrownBy(() -> tableService.changeEmpty(9L, request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문테이블의_아이디로_조리중_식사중_상태인_주문이_존재하면_예외를_발생한다() {
        final UpdateOrderTableEmptyRequest request = new UpdateOrderTableEmptyRequest(true);

        assertThatThrownBy(() -> tableService.changeEmpty(1L, request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블_고객수를_변경한다() {
        final int changeGuestCount = 11;
        final UpdateOrderTableNumberOfGuestsRequest request = new UpdateOrderTableNumberOfGuestsRequest(changeGuestCount);

        final OrderTableResponse edit = tableService.changeNumberOfGuests(9L, request);

        assertThat(edit.getNumberOfGuests()).isEqualTo(changeGuestCount);
    }

    @Test
    void 주문_테이블의_상태가_빈_상태면_주문_테이블_고객수를_변경하지_못한다() {
        final int changeGuestCount = 11;
        final UpdateOrderTableNumberOfGuestsRequest request = new UpdateOrderTableNumberOfGuestsRequest(changeGuestCount);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 고객수가_음수이면_예외를_발생한다() {
        final int changeGuestCount = -11;
        final UpdateOrderTableNumberOfGuestsRequest request = new UpdateOrderTableNumberOfGuestsRequest(changeGuestCount);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문테이블이_존재하지_않으면_예외를_발생한다() {
        final int changeGuestCount = 11;
        final UpdateOrderTableNumberOfGuestsRequest request = new UpdateOrderTableNumberOfGuestsRequest(changeGuestCount);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(100L, request)).isInstanceOf(IllegalArgumentException.class);
    }
}

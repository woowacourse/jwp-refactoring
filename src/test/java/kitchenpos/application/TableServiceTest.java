package kitchenpos.application;

import fixture.OrderBuilder;
import fixture.OrderTableBuilder;
import fixture.TableGroupBuilder;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.ui.request.OrderTableRequest;
import kitchenpos.ui.response.OrderTableResponse;
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
        final OrderTableRequest orderTableRequest = new OrderTableRequest(null,1L, 10, false);

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
        final OrderTableRequest orderTableRequest = new OrderTableRequest(null, 1L, 10, true);

        final OrderTableResponse edit = tableService.changeEmpty(10L, orderTableRequest);

        assertThat(edit.isEmpty()).isTrue();
    }

    @Test
    void 테이블그룹에_속한_상태로_빈_테이블로_바꾸면_예외를_발생한다() {
        final OrderTableRequest orderTableRequest = new OrderTableRequest(null,null, 10, true);

        assertThatThrownBy(() -> tableService.changeEmpty(9L, orderTableRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문테이블의_아이디로_조리중_식사중_상태인_주문이_존재하면_예외를_발생한다() {
        final TableGroup saveTableGroup = tableGroupRepository.save(TableGroupBuilder.init().build());
        final OrderTable saveOrderTable = orderTableRepository.save(OrderTableBuilder.init().tableGroup(saveTableGroup).empty(false).build());
        orderRepository.save(OrderBuilder.init().orderTable(saveOrderTable).orderStatus(OrderStatus.MEAL).build());

        assertThatThrownBy(() -> tableService.changeEmpty(saveOrderTable.getId(), new OrderTableRequest(null, saveTableGroup.getId(), saveOrderTable.getNumberOfGuests(), true))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블_고객수를_변경한다() {
        final int changeGuestCount = 11;
        final OrderTableRequest orderTableRequest = new OrderTableRequest(null, 1L, changeGuestCount, false);

        final OrderTableResponse edit = tableService.changeNumberOfGuests(9L, orderTableRequest);

        assertThat(edit.getNumberOfGuests()).isEqualTo(changeGuestCount);
    }

    @Test
    void 주문_테이블의_상태가_빈_상태면_주문_테이블_고객수를_변경하지_못한다() {
        final int changeGuestCount = 11;
        final OrderTableRequest orderTableRequest = new OrderTableRequest(null, 1L, changeGuestCount, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTableRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 고객수가_음수이면_예외를_발생한다() {
        final int changeGuestCount = -11;
        final OrderTableRequest orderTableRequest = new OrderTableRequest(null, 1L, changeGuestCount, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTableRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문테이블이_존재하지_않으면_예외를_발생한다() {
        final int changeGuestCount = 11;
        final OrderTableRequest orderTableRequest = new OrderTableRequest(null, 1L, changeGuestCount, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(100L, orderTableRequest)).isInstanceOf(IllegalArgumentException.class);
    }
}

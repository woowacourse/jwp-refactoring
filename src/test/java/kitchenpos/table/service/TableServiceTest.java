package kitchenpos.table.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.common.event.ValidateAllOrderCompletedEvent;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.OrderIsNotCompletedException;
import kitchenpos.supports.ServiceTestContext;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.request.ChangeEmptyTableRequest;
import kitchenpos.table.dto.request.ChangeTableGuestRequest;
import kitchenpos.table.dto.request.CreateOrderTableRequest;
import kitchenpos.table.dto.response.OrderTableResponse;
import kitchenpos.table.exception.NotEnoughGuestsException;
import kitchenpos.table.exception.OrderTableEmptyException;
import kitchenpos.table.exception.OrderTableNotFoundException;
import kitchenpos.table.exception.TableGroupExistsException;
import kitchenpos.tablegroup.domain.TableGroup;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;

@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest extends ServiceTestContext {

    @Test
    void 테이블을_정상_생성하면_생성한_테이블이_반환된다() {
        // given
        CreateOrderTableRequest request = new CreateOrderTableRequest(2, false);

        // when
        OrderTableResponse response = tableService.create(request);

        // then
        assertThat(response.getId()).isNotNull();
    }

    @Test
    void 모든_테이블을_조회할_수_있다() {
        // given
        CreateOrderTableRequest request = new CreateOrderTableRequest(2, false);
        tableService.create(request);

        // when
        List<OrderTableResponse> response = tableService.findAll();

        // then
        assertThat(response).hasSize(1);
    }

    @Test
    void 빈_테이블로_변경할_시_해당_테이블이_없다면_예외를_던진다() {
        // given
        Long orderTableId = Long.MAX_VALUE;

        ChangeEmptyTableRequest request = new ChangeEmptyTableRequest(false);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, request))
                .isInstanceOf(OrderTableNotFoundException.class);
    }

    @Test
    void 테이블_그룹이_있는_경우_빈_테이블로_변경하려_할_때_예외를_던진다() {
        // given
        TableGroup tableGroup = TableGroupFixture.from(LocalDateTime.now());
        tableGroupRepository.save(tableGroup);

        OrderTable orderTable = OrderTableFixture.of(tableGroup.getId(), 2, false);
        orderTableRepository.save(orderTable);

        ChangeEmptyTableRequest request = new ChangeEmptyTableRequest(false);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), request))
                .isInstanceOf(TableGroupExistsException.class);
    }

    @ParameterizedTest
    @EnumSource(mode = Mode.INCLUDE, names = {"COOKING", "MEAL"})
    void 빈_테이블로_변경하려_할_때_주문_상태가_COOKING이거나_MEAL이면_예외를_던진다(OrderStatus orderStatus) {
        // given
        OrderTable orderTable = OrderTableFixture.of(null, 2, false);
        orderTableRepository.save(orderTable);

        Order order = OrderFixture.of(orderTable.getId(), orderStatus, LocalDateTime.now());
        orderRepository.save(order);

        ChangeEmptyTableRequest request = new ChangeEmptyTableRequest(false);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), request))
                .isInstanceOf(OrderIsNotCompletedException.class);
    }

    @Test
    void 빈_테이블로_변경할_때_주문이_모두_마쳤는지에_대한_검증_이벤트가_발행된다() {
        // given
        OrderTable orderTable = OrderTableFixture.of(null, 2, false);
        orderTableRepository.save(orderTable);

        Order order = OrderFixture.of(orderTable.getId(), OrderStatus.COMPLETION, LocalDateTime.now());
        orderRepository.save(order);

        ChangeEmptyTableRequest request = new ChangeEmptyTableRequest(false);

        tableService.changeEmpty(orderTable.getId(), request);

        // when
        long eventOccurredCount = applicationEvents.stream(ValidateAllOrderCompletedEvent.class)
                .count();

        // then
        assertThat(eventOccurredCount).isEqualTo(1);
    }

    @Test
    void 손님_수를_0명_미만으로_변경하려고_하면_예외를_던진다() {
        // given
        OrderTable orderTable = OrderTableFixture.of(null, 1, false);
        orderTableRepository.save(orderTable);

        ChangeTableGuestRequest request = new ChangeTableGuestRequest(-1);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), request))
                .isInstanceOf(NotEnoughGuestsException.class);
    }

    @Test
    void 손님_수를_변경할_때_테이블이_없다면_예외를_던진다() {
        // given
        Long orderTableId = Long.MAX_VALUE;
        ChangeTableGuestRequest request = new ChangeTableGuestRequest(2);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, request))
                .isInstanceOf(OrderTableNotFoundException.class);
    }

    @Test
    void 빈_테이블에_대해_손님_수를_변경하려_하면_예외를_던진다() {
        // given
        OrderTable orderTable = OrderTableFixture.of(null, -1, true);
        orderTableRepository.save(orderTable);

        ChangeTableGuestRequest request = new ChangeTableGuestRequest(2);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), request))
                .isInstanceOf(OrderTableEmptyException.class);
    }

    @Test
    void 테이블을_정상적으로_변경하면_변경된_테이블을_반환한다() {
        // given
        OrderTable orderTable = OrderTableFixture.of(null, 1, false);
        orderTableRepository.save(orderTable);

        ChangeTableGuestRequest request = new ChangeTableGuestRequest(5);

        // when
        OrderTableResponse response = tableService.changeNumberOfGuests(orderTable.getId(), request);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getId()).isNotNull();
            softly.assertThat(response.getNumberOfGuests()).isEqualTo(5);
        });
    }

    @Test
    void 존재하지_않는_테이블에_대해_그룹화하려_하면_예외를_던진다() {
        // given
        TableGroup tableGroup = TableGroupFixture.from(LocalDateTime.now());
        OrderTable orderTable = OrderTableFixture.of(null, 1, false);
        orderTableRepository.save(orderTable);
        tableGroupRepository.save(tableGroup);

        // when, then
        assertThatThrownBy(() -> tableService.groupOrderTables(tableGroup.getId(), List.of(orderTable.getId(), 999L)))
                .isInstanceOf(OrderTableNotFoundException.class);
    }
}

package kitchenpos.table.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.OrderIsNotCompletedException;
import kitchenpos.supports.ServiceTestContext;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.request.CreateTableGroupRequest;
import kitchenpos.table.dto.request.OrderTableRequest;
import kitchenpos.table.dto.response.TableGroupResponse;
import kitchenpos.table.exception.OrderTableCountNotEnoughException;
import kitchenpos.table.exception.OrderTableNotEmptyException;
import kitchenpos.table.exception.OrderTableNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends ServiceTestContext {

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1})
    void 두개_이상의_테이블을_그룹으로_지정하지_않으면_예외를_던진다(int tableSize) {
        // given
        List<OrderTableRequest> orderTableRequests = new ArrayList<>();
        for (int i = 0; i < tableSize; i++) {
            OrderTable orderTable = OrderTableFixture.of(null, 1, true);
            orderTableRepository.save(orderTable);

            orderTableRequests.add(new OrderTableRequest(orderTable.getId()));
        }

        CreateTableGroupRequest request = new CreateTableGroupRequest(orderTableRequests);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(OrderTableCountNotEnoughException.class);
    }

    @Test
    void 존재하지_않는_테이블을_참조하면_예외를_던진다() {
        // given
        List<OrderTableRequest> orderTableRequests = List.of(
                new OrderTableRequest(Long.MAX_VALUE),
                new OrderTableRequest(Long.MAX_VALUE - 1L)
        );

        CreateTableGroupRequest request = new CreateTableGroupRequest(orderTableRequests);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(OrderTableNotFoundException.class);
    }

    @Test
    void 그룹_지정_대상이_빈_테이블이_아니라면_예외를_던진다() {
        // given
        OrderTable orderTable1 = OrderTableFixture.of(null, 1, false);
        OrderTable orderTable2 = OrderTableFixture.of(null, 1, false);

        orderTableRepository.save(orderTable1);
        orderTableRepository.save(orderTable2);

        List<OrderTableRequest> orderTableRequests = List.of(
                new OrderTableRequest(orderTable1.getId()),
                new OrderTableRequest(orderTable2.getId())
        );

        CreateTableGroupRequest request = new CreateTableGroupRequest(orderTableRequests);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(OrderTableNotEmptyException.class);
    }

    @Test
    void 그룹_지정_대상이_이미_그룹이_존재한다면_예외를_던진다() {
        // given
        TableGroup tableGroup = TableGroupFixture.from(LocalDateTime.now());
        tableGroupRepository.save(tableGroup);

        OrderTable orderTable1 = OrderTableFixture.of(tableGroup, 1, false);
        OrderTable orderTable2 = OrderTableFixture.of(tableGroup, 1, false);

        orderTableRepository.save(orderTable1);
        orderTableRepository.save(orderTable2);

        List<OrderTableRequest> orderTableRequests = List.of(
                new OrderTableRequest(orderTable1.getId()),
                new OrderTableRequest(orderTable2.getId())
        );

        CreateTableGroupRequest request = new CreateTableGroupRequest(orderTableRequests);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(OrderTableNotEmptyException.class);
    }

    @Test
    void 정상적으로_그룹을_생성하면_생성한_그룹을_반환한다() {
        // given
        OrderTable orderTable1 = OrderTableFixture.of(null, 0, true);
        OrderTable orderTable2 = OrderTableFixture.of(null, 0, true);

        orderTableRepository.save(orderTable1);
        orderTableRepository.save(orderTable2);

        List<OrderTableRequest> orderTableRequests = List.of(
                new OrderTableRequest(orderTable1.getId()),
                new OrderTableRequest(orderTable2.getId())
        );

        CreateTableGroupRequest request = new CreateTableGroupRequest(orderTableRequests);

        // when
        TableGroupResponse response = tableGroupService.create(request);

        // then
        assertThat(response.getId()).isNotNull();
    }

    @ParameterizedTest
    @EnumSource(mode = Mode.INCLUDE, names = {"COOKING", "MEAL"})
    void 주문_상태가_COOKING이거나_MEAL인_경우_그룹을_해체할_수_없다(OrderStatus orderStatus) {
        // given
        TableGroup tableGroup = TableGroupFixture.from(LocalDateTime.now());
        tableGroupRepository.save(tableGroup);

        OrderTable orderTable = OrderTableFixture.of(tableGroup, 0, false);
        orderTableRepository.save(orderTable);

        Order order = OrderFixture.of(orderTable, orderStatus, LocalDateTime.now());
        orderRepository.save(order);

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(OrderIsNotCompletedException.class);
    }
}

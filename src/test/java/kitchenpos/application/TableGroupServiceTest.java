package kitchenpos.application;

import kitchenpos.application.dto.request.CreateTableGroupRequest;
import kitchenpos.application.dto.request.CreateTableGroupRequest.TableInfo;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    @ParameterizedTest
    @MethodSource("generateInvalidOrderTables")
    void 테이블_그룹을_만드려는_테이블_없으면_예외_발생(List<TableInfo> orderTables) {
        // given
        // when, then
        CreateTableGroupRequest createTableGroupRequest = new CreateTableGroupRequest(orderTables);
        assertThatThrownBy(() -> tableGroupService.create(createTableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 합치려는_테이블과_실제_존재하는_테이블이_다르면_예외발생() {
        // given
        given(orderTableRepository.findAllByIdIn(anyList()))
                .willReturn(List.of(new OrderTable(1, true)));

        // when, then
        CreateTableGroupRequest createTableGroupRequest = new CreateTableGroupRequest(List.of(new TableInfo(1L), new TableInfo(2L)));
        assertThatThrownBy(() -> tableGroupService.create(createTableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
        then(tableGroupRepository).should(never()).save(any());
    }

    @Test
    void 합치려는_테이블이_속한_테이블_그룹이_있으면_예외발생() {
        // given
        TableGroup tableGroup = new TableGroup();

        OrderTable orderTable1 = new OrderTable(1, true);
        orderTable1.changeTableGroup(new TableGroup());

        OrderTable orderTable2 = new OrderTable(2, true);

        given(orderTableRepository.findAllByIdIn(anyList()))
                .willReturn(List.of(orderTable1, orderTable2));

        // when, then
        CreateTableGroupRequest createTableGroupRequest = new CreateTableGroupRequest(List.of(new TableInfo(1L), new TableInfo(2L)));
        assertThatThrownBy(() -> tableGroupService.create(createTableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
        then(tableGroupRepository).should(never()).save(any());
    }

    @Test
    void 합치려는_테이블_중_빈_테이블이_아닌_테이블이_있으면_예외발생() {
        // given
        TableGroup tableGroup = new TableGroup();

        OrderTable orderTable1 = new OrderTable(1, false);

        OrderTable orderTable2 = new OrderTable(1, true);

        given(orderTableRepository.findAllByIdIn(anyList()))
                .willReturn(List.of(orderTable1, orderTable2));

        // when, then
        CreateTableGroupRequest createTableGroupRequest = new CreateTableGroupRequest(List.of(new TableInfo(1L), new TableInfo(2L)));
        assertThatThrownBy(() -> tableGroupService.create(createTableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
        then(tableGroupRepository).should(never()).save(any());
    }

    @Test
    void 여러_테이블을_하나의_그룹으로_합친다() {
        // given
        OrderTable orderTable1 = new OrderTable(1, true);
        OrderTable orderTable2 = new OrderTable(2, true);

        given(orderTableRepository.findAllByIdIn(anyList()))
                .willReturn(List.of(orderTable1, orderTable2));

        // when
        CreateTableGroupRequest createTableGroupRequest = new CreateTableGroupRequest(List.of(new TableInfo(1L), new TableInfo(2L)));
        tableGroupService.create(createTableGroupRequest);

        // then
//        then(tableGroupRepository).should(times(1)).save(any());
    }

    @Test
    void 테이블_그룹을_해체할_때_식사_완료가_아닌_테이블이_존재하면_예외발생() {
        // given
        OrderTable orderTable1 = new OrderTable(1, true);
        OrderTable orderTable2 = new OrderTable(1, true);

        given(orderTableRepository.findAllByTableGroupId(anyLong()))
                .willReturn(List.of(orderTable1, orderTable2));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), eq(List.of(OrderStatus.COOKING, OrderStatus.MEAL))))
                .willReturn(true);

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderTableRepository).should(never()).save(any());
    }

    @Test
    void 테이블_그룹을_해체한다() {
        // given
        OrderTable orderTable1 = new OrderTable(1, true);
        OrderTable orderTable2 = new OrderTable(1, true);

        given(orderTableRepository.findAllByTableGroupId(anyLong()))
                .willReturn(List.of(orderTable1, orderTable2));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), eq(List.of(OrderStatus.COOKING, OrderStatus.MEAL))))
                .willReturn(false);

        // when, then
        tableGroupService.ungroup(1L);
    }

    static Stream<Arguments> generateInvalidOrderTables() {
        return Stream.of(
                Arguments.of(Collections.emptyList()),
                Arguments.of(Arrays.asList(new TableInfo(1L)))
        );
    }
}

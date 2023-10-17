package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
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
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @ParameterizedTest
    @MethodSource("generateInvalidOrderTables")
    void 테이블_그룹을_만드려는_테이블_없으면_예외_발생(List<OrderTable> orderTables) {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderTableDao).should(never()).findAllByIdIn(anyList());
    }

    @Test
    void 합치려는_테이블과_실제_존재하는_테이블이_다르면_예외발생() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(new OrderTable(), new OrderTable()));

        given(orderTableDao.findAllByIdIn(anyList()))
                .willReturn(List.of(new OrderTable()));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        then(tableGroupDao).should(never()).save(any());
    }

    @Test
    void 합치려는_테이블이_속한_테이블_그룹이_있으면_예외발생() {
        // given
        TableGroup tableGroup = new TableGroup();

        OrderTable orderTable1 = new OrderTable();
        orderTable1.setTableGroupId(1L);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setTableGroupId(2L);
        tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

        given(orderTableDao.findAllByIdIn(anyList()))
                .willReturn(List.of(orderTable1, orderTable2));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        then(tableGroupDao).should(never()).save(any());
    }

    @Test
    void 합치려는_테이블_중_빈_테이블이_아닌_테이블이_있으면_예외발생() {
        // given
        TableGroup tableGroup = new TableGroup();

        OrderTable orderTable1 = new OrderTable();

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(false);
        tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

        given(orderTableDao.findAllByIdIn(anyList()))
                .willReturn(List.of(orderTable1, orderTable2));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        then(tableGroupDao).should(never()).save(any());
    }

    @Test
    void 여러_테이블을_하나의_그룹으로_합친다() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);

        OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);

        tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

        given(orderTableDao.findAllByIdIn(anyList()))
                .willReturn(List.of(orderTable1, orderTable2));
        given(tableGroupDao.save(any()))
                .willReturn(tableGroup);

        // when
        tableGroupService.create(tableGroup);

        // then
        then(tableGroupDao).should(times(1)).save(tableGroup);
        then(orderTableDao).should(times(2)).save(any());
    }

    @Test
    void 테이블_그룹을_해체할_때_식사_완료가_아닌_테이블이_존재하면_예외발생() {
        // given
        OrderTable orderTable1 = new OrderTable();
        OrderTable orderTable2 = new OrderTable();

        given(orderTableDao.findAllByTableGroupId(anyLong()))
                .willReturn(List.of(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), eq(List.of("COOKING", "MEAL"))))
                .willReturn(true);

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderTableDao).should(never()).save(any());
    }

    @Test
    void 테이블_그룹을_해체한다() {
        // given
        OrderTable orderTable1 = new OrderTable();
        OrderTable orderTable2 = new OrderTable();

        given(orderTableDao.findAllByTableGroupId(anyLong()))
                .willReturn(List.of(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), eq(List.of("COOKING", "MEAL"))))
                .willReturn(false);

        // when, then
        tableGroupService.ungroup(1L);
        then(orderTableDao).should(times(2)).save(any());
    }

    static Stream<Arguments> generateInvalidOrderTables() {
        return Stream.of(
                Arguments.of(Collections.emptyList()),
                Arguments.of(Arrays.asList(new OrderTable()))
        );
    }
}

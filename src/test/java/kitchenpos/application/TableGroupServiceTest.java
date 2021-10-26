package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @DisplayName("단체 지정을 등록할 수 있다.")
    @Test
    void create() {
        //given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);

        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(true);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(true);

        List<OrderTable> orderTables = List.of(orderTable1, orderTable2);

        tableGroup.setOrderTables(orderTables);

        given(orderTableDao.findAllByIdIn(List.of(orderTable1.getId(), orderTable2.getId())))
                .willReturn(orderTables);
        given(tableGroupDao.save(tableGroup))
                .willReturn(tableGroup);
        //when

        TableGroup actual = tableGroupService.create(tableGroup);
        //then
        assertThat(actual).isEqualTo(tableGroup);

        verify(orderTableDao, times(1)).findAllByIdIn(anyList());
        verify(tableGroupDao, times(1)).save(tableGroup);
        verify(orderTableDao, times(2)).save(any());
    }

    @DisplayName("단체 지정 등록 실패 - 주문 테이블이 없거나 하나인 경우")
    @Test
    void createFailInvalidOrderTableCount() {
        //given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Collections.emptyList());
        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정시 주문 테이블은 두 개 이상이여야합니다.");
    }

    @DisplayName("단체 지정 등록 실패 - 존재하지 않는 주문 테이블인 경우")
    @Test
    void createFailNotExistOrderTable() {
        //given
        TableGroup tableGroup = new TableGroup();

        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);

        List<OrderTable> orderTables = List.of(orderTable1, orderTable2);
        tableGroup.setOrderTables(orderTables);

        given(orderTableDao.findAllByIdIn(List.of(1L, 2L)))
                .willReturn(List.of(orderTable1));
        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문 테이블입니다.");
    }

    @DisplayName("단체 지정 등록 실패 - 빈 테이블이 아닐 경우")
    @Test
    void createFailNotEmptyTable() {
        //given
        TableGroup tableGroup = new TableGroup();

        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(false);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);

        List<OrderTable> orderTables = List.of(orderTable1, orderTable2);
        tableGroup.setOrderTables(orderTables);

        given(orderTableDao.findAllByIdIn(List.of(1L, 2L)))
                .willReturn(List.of(orderTable1, orderTable2));
        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블이 아니거나 그룹 테이블 ID가 null이 아닙니다.");
    }

    @DisplayName("단체 지정 등록 실패 - 그룹테이블이 null일 경우")
    @Test
    void createFailNonNullGroupTable() {
        //given
        TableGroup tableGroup = new TableGroup();

        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(1L);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(true);

        List<OrderTable> orderTables = List.of(orderTable1, orderTable2);
        tableGroup.setOrderTables(orderTables);

        given(orderTableDao.findAllByIdIn(List.of(1L, 2L)))
                .willReturn(List.of(orderTable1, orderTable2));
        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블이 아니거나 그룹 테이블 ID가 null이 아닙니다.");
    }

    @DisplayName("단체 지정을 해제할 수 있다.")
    @Test
    void ungroup() {
        //given
        Long tableGroupId = 1L;

        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setTableGroupId(1L);
        orderTable1.setEmpty(false);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(false);
        orderTable2.setTableGroupId(1L);

        given(orderTableDao.findAllByTableGroupId(tableGroupId))
                .willReturn(List.of(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                List.of(1L, 2L),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(false);
        //when
        tableGroupService.ungroup(tableGroupId);
        //then
        verify(orderTableDao, times(1)).findAllByTableGroupId(tableGroupId);
        verify(orderDao, times(1)).existsByOrderTableIdInAndOrderStatusIn(
                List.of(1L, 2L),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
        verify(orderTableDao, times(2)).save(any());
    }

    @DisplayName("단체 지정을 해제 실패 - 계산 완료가 안된 테이블이 있는 경우")
    @Test
    void ungroupFailExistNotCompletionTable() {
        //given
        Long tableGroupId = 1L;

        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setTableGroupId(1L);
        orderTable1.setEmpty(false);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(false);
        orderTable2.setTableGroupId(1L);

        given(orderTableDao.findAllByTableGroupId(tableGroupId))
                .willReturn(List.of(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                List.of(1L, 2L),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(true);
        //when
        //then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("계산 완료 상태가 아닌 주문 테이블이 있을 경우 단체 지정을 해제할 수 없습니다.");

    }
}
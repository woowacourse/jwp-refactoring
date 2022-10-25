package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Test
    void 생성_시_order_table이_비어있으면_예외를_반환한다() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성_시_order_table의_크기가_2보다_작으면_예외를_반환한다() {
        // given
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable = new OrderTable();
        tableGroup.setOrderTables(Arrays.asList(orderTable));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성_시_order_table의_크기가_찾은_order_table의_크기와_일치하지_않으면_예외를_반환한다() {
        // given
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(orderTable1));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성_시_저장된_order_table이_비어있지_않으면_예외를_반환한다() {
        // given
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(false);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(orderTable1, orderTable2));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성_시_저장된_order_table의_table_group_id가_null이_아니면_예외를_반환한다() {
        // given
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(11L);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(true);
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(orderTable1, orderTable2));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void table_group을_생성할_수_있다() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(true);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(true);
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(orderTable1, orderTable2));

        when(tableGroupDao.save(any(TableGroup.class))).thenReturn(tableGroup);

        // when
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        Assertions.assertAll(
                () -> assertThat(savedTableGroup.getCreatedDate()).isNotNull(),
                () -> assertThat(savedTableGroup.getOrderTables().get(0)).extracting("empty").isEqualTo(false),
                () -> assertThat(savedTableGroup.getOrderTables().get(0)).extracting("tableGroupId").isEqualTo(1L)
        );
    }

    @Test
    void table_group을_취소시_order가_COOKING_또는_MEAL_상태이면_예외를_반환한다() {
        // given
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        when(orderTableDao.findAllByTableGroupId(1L)).thenReturn(Arrays.asList(orderTable1, orderTable2));

        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(1L, 2L),
                Arrays.asList(COOKING.name(), MEAL.name()))).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void table_group을_취소할_수_있다() {
        // given
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setTableGroupId(1L);
        orderTable1.setEmpty(true);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setTableGroupId(1L);
        orderTable2.setEmpty(true);
        when(orderTableDao.findAllByTableGroupId(1L)).thenReturn(Arrays.asList(orderTable1, orderTable2));

        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(false);

        // when
        tableGroupService.ungroup(1L);

        // then
        Assertions.assertAll(
                () -> assertThat(orderTable1.getTableGroupId()).isNull(),
                () -> assertThat(orderTable1.isEmptyOrderTable()).isFalse()
        );
    }
}

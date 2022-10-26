package kitchenpos.application;

import static kitchenpos.Fixture.TABLE_GROUP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
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

    @DisplayName("테이블 그룹을 추가한다.")
    @Test
    void create() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, 0, true);
        OrderTable orderTable2 = new OrderTable(2L, 1, true);
        given(orderTableDao.findAllByIdIn(anyList()))
                .willReturn(List.of(orderTable1, orderTable2));
        given(tableGroupDao.save(any(TableGroup.class)))
                .willReturn(TABLE_GROUP);

        //when
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        //then
        assertThat(savedTableGroup).isEqualTo(TABLE_GROUP);
        assertThat(orderTable1.getTableGroupId()).isEqualTo(TABLE_GROUP.getId());
        assertThat(orderTable2.getTableGroupId()).isEqualTo(TABLE_GROUP.getId());
        assertThat(orderTable1.isEmpty()).isFalse();
        assertThat(orderTable2.isEmpty()).isFalse();
    }

    @Test
    void create_오더테이블개수가_2개_이하면_예외반환() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, 0, true);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1));

        //when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("테이블 그룹을 취소한다.")
    @Test
    void ungroup() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, 1L, 0, true);
        OrderTable orderTable2 = new OrderTable(2L, 1L, 1, true);
        given(orderTableDao.findAllByTableGroupId(anyLong()))
                .willReturn(List.of(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
                .willReturn(false);

        //when
        tableGroupService.ungroup(1L);

        //then
        assertThat(orderTable1.getTableGroupId()).isNull();
        assertThat(orderTable2.getTableGroupId()).isNull();
        assertThat(orderTable1.isEmpty()).isFalse();
        assertThat(orderTable2.isEmpty()).isFalse();
    }

    @Test
    void ungroup_테이블그룹을_취소시_주문이_COOKING_또는_MEAL_상태이면_예외를_반환한다() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, 1L, 0, true);
        OrderTable orderTable2 = new OrderTable(2L, 1L, 1, true);
        given(orderTableDao.findAllByTableGroupId(anyLong()))
                .willReturn(List.of(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
                .willReturn(true);

        //when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

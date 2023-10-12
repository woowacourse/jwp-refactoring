package kitchenpos.application;

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

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("테이블 그룹 생성 테스트")
    public void createTableGroupTest() {
        //given
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(true);
        orderTable.setTableGroupId(null);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(orderTable, orderTable));

        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable, orderTable));
        given(tableGroupDao.save(any(TableGroup.class))).willReturn(tableGroup);

        //when
        TableGroup result = tableGroupService.create(tableGroup);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getOrderTables().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("테이블 그룹 해제 테스트")
    public void ungroupTableTest() {
        //given
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(true);
        orderTable.setTableGroupId(1L);

        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(List.of(orderTable));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(false);

        //when
        tableGroupService.ungroup(1L);

        //then
        assertThat(orderTable.getTableGroupId()).isNull();
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("테이블 그룹 생성 실패 테스트")
    public void createTableGroupFailTest() {
        //given
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(false);
        orderTable.setTableGroupId(1L);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(orderTable));

        //when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }
}

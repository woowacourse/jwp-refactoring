package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.List;
import kitchenpos.application.dto.TableGroupCreateRequest;
import kitchenpos.application.support.domain.OrderTableTestSupport;
import kitchenpos.application.support.domain.TableGroupTestSupport;
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

    @Mock
    OrderDao orderDao;
    @Mock
    OrderTableDao orderTableDao;
    @Mock
    TableGroupDao tableGroupDao;
    @InjectMocks
    TableGroupService target;

    @DisplayName("단체 지정을 생성한다.")
    @Test
    void create() {
        //given
        final OrderTable table1 = OrderTableTestSupport.builder().empty(true).build();
        final OrderTable table2 = OrderTableTestSupport.builder().empty(true).build();
        final List<OrderTable> orderTables = List.of(table1, table2);

        final TableGroupTestSupport.Builder builder = TableGroupTestSupport.builder();
        final TableGroup tableGroup = builder.orderTables(orderTables).build();
        final TableGroupCreateRequest request = builder.buildToTableGroupCreateRequest();

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);
        given(tableGroupDao.save(any(TableGroup.class))).willReturn(tableGroup);
        for (OrderTable orderTable : orderTables) {
            given(orderTableDao.save(orderTable)).willReturn(orderTable);
        }

        //when

        //then
        assertDoesNotThrow(() -> target.create(request));
    }

    @DisplayName("테이블이 2개 미만이면 예외 처리한다.")
    @Test
    void create_fail_lessThan_2() {
        //given
        final OrderTable orderTable = OrderTableTestSupport.builder().build();

        final TableGroupCreateRequest request = TableGroupTestSupport.builder().orderTables(List.of(orderTable))
                .buildToTableGroupCreateRequest();

        //when

        //then
        assertThatThrownBy(() -> target.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 정보가 존재하지 않으면 예외 처리한다.")
    @Test
    void create_fail_invalid_table() {
        ///given
        final TableGroupCreateRequest request = TableGroupTestSupport.builder().buildToTableGroupCreateRequest();
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Collections.emptyList());

        //when

        //then
        assertThatThrownBy(() -> target.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블이 아닌 경우 예외 처리한다.")
    @Test
    void create_fail_not_empty_table() {
        ///given
        final OrderTable table1 = OrderTableTestSupport.builder().empty(false).build();
        final OrderTable table2 = OrderTableTestSupport.builder().build();
        final List<OrderTable> orderTables = List.of(table1, table2);
        final TableGroupCreateRequest request = TableGroupTestSupport.builder().orderTables(orderTables)
                .buildToTableGroupCreateRequest();
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);

        //when

        //then
        assertThatThrownBy(() -> target.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 다른 단체 지정이 되어있으면 예외 처리한다.")
    @Test
    void create_fail_already_group() {
        ///given
        final OrderTable table1 = OrderTableTestSupport.builder().empty(true).tableGroupId(1L).build();
        final OrderTable table2 = OrderTableTestSupport.builder().empty(true).tableGroupId(1L).build();
        final List<OrderTable> orderTables = List.of(table1, table2);
        final TableGroupCreateRequest request = TableGroupTestSupport.builder().orderTables(orderTables)
                .buildToTableGroupCreateRequest();
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);

        //when

        //then
        assertThatThrownBy(() -> target.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() {
        //given
        final TableGroup tableGroup = TableGroupTestSupport.builder().build();
        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(tableGroup.getOrderTables());
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);

        //when

        //then
        assertDoesNotThrow(() -> target.ungroup(tableGroup.getId()));
    }

    @DisplayName("주문한 테이블들이 계산 완료 상태가 아니라면 예외 처리한다.")
    @Test
    void ungroup_fail_not_COMPLETION() {
        //given
        final TableGroup tableGroup = TableGroupTestSupport.builder().build();
        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(tableGroup.getOrderTables());
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        //when

        //then
        assertThatThrownBy(() -> target.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

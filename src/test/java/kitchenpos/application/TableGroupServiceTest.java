package kitchenpos.application;

import static kitchenpos.fixture.TableFixture.getOrderTable;
import static kitchenpos.fixture.TableFixture.getTableGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceTest{

    @Autowired
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("테이블 그룹을 생성하면 테이블 그룹에 속한 테이블은 비어있지 않아야 한다.")
    void createThenOrderTableEmpty() {
        // given
        final OrderTable orderTable1 = getOrderTable(true);
        final OrderTable orderTable2 = getOrderTable(true);
        final TableGroup tableGroup = getTableGroupRequest(Arrays.asList(orderTable1, orderTable2));
        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable1, orderTable2));

        // when
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        final List<OrderTable> orderTables = savedTableGroup.getOrderTables();

        // then
        assertAll(
                () -> assertThat(orderTables.get(0).isEmpty()).isFalse(),
                () -> assertThat(orderTables.get(1).isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("테이블 그룹을 생성하면 테이블 그룹에 속한 테이블은 테이블 그룹 id를 가져야 한다..")
    void createThenOrderTableHaveTableGroupId() {
        // given
        final OrderTable orderTable1 = getOrderTable(true);
        final OrderTable orderTable2 = getOrderTable(true);
        final TableGroup tableGroup = getTableGroupRequest(Arrays.asList(orderTable1, orderTable2));
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable1, orderTable2));

        // when
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        final List<OrderTable> orderTables = savedTableGroup.getOrderTables();

        // then
        assertAll(
                () -> assertThat(orderTables.get(0).getTableGroupId()).isNotNull(),
                () -> assertThat(orderTables.get(1).getTableGroupId()).isNotNull()
        );
    }

    @Test
    @DisplayName("존재하지 않는 테이블로 테이블 그룹을 생성하면 에외가 발생한다.")
    void createWithNonExistTables() {
        // given
        final OrderTable orderTable1 = getOrderTable(1L);
        final OrderTable orderTable2 = getOrderTable(1L);
        final OrderTable orderTable3 = getOrderTable(1L);
        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable1, orderTable2));

        // when
        final TableGroup tableGroup = getTableGroupRequest(Arrays.asList(orderTable1,
                orderTable2,
                orderTable3));

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 비어있지 않을 경우 테이블 그룹을 생성하면 예외가 발생한다.")
    void createWithEmptyTables() {
        // given
        final OrderTable orderTable1 = getOrderTable(false);
        final OrderTable orderTable2 = getOrderTable(false);
        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable1, orderTable2));

        // when
//        orderTable1.setEmpty(false);
//        orderTable2.setEmpty(false);
        final TableGroup tableGroup = getTableGroupRequest(Arrays.asList(orderTable1, orderTable2));

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹의 사이즈가 1이면 예외가 발생한다.")
    void createWithInvalidSIze() {
        // given
        final TableGroup tableGroup = getTableGroupRequest(Arrays.asList(getOrderTable()));

        // when
        final OrderTable savedOrderTable = getOrderTable(1L);
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(savedOrderTable));

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 테이블 그룹이 있는 주문 테이블로 테이블 그룹을 생성하면 예외가 발생한다.")
    void createWithInvalidOrderTables() {
        // given
        final List<OrderTable> orderTables = Arrays.asList(getOrderTable(1L), getOrderTable(1L));
        final TableGroup tableGroup = getTableGroupRequest(orderTables);

        // when
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(orderTables.get(0), orderTables.get(1)));

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 해제한다.")
    void unGroup() {
        // given
        final List<OrderTable> orderTables = Arrays.asList(getOrderTable(1L), getOrderTable(1L));
        given(orderTableDao.findAllByTableGroupId(1L)).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(false);

        // when
        tableGroupService.ungroup(1L);

        // then
        assertAll(
                () -> assertThat(orderTables.get(0).getTableGroupId()).isNull(),
                () -> assertThat(orderTables.get(0).isEmpty()).isFalse(),
                () -> assertThat(orderTables.get(1).getTableGroupId()).isNull(),
                () -> assertThat(orderTables.get(1).isEmpty()).isFalse()
        );
    }
}

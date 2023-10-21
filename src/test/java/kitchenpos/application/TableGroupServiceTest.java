package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Collections;
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

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void create() {
        // given
        final List<OrderTable> orderTables = List.of(
            OrderTable.forSave(1L, 2, true),
            OrderTable.forSave(2L, 2, true)
        );
        final TableGroup tableGroup = TableGroup.forSave(LocalDateTime.now(), orderTables);

        given(orderTableDao.findAllByIdIn(any()))
            .willReturn(List.of(
                new OrderTable(1L, null, 2, true),
                new OrderTable(2L, null, 2, true))
            );

        given(tableGroupDao.save(any()))
            .willReturn(new TableGroup(1L, LocalDateTime.now(), orderTables));

        // when
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(savedTableGroup.getId()).isEqualTo(1L);
        assertThat(savedTableGroup.getOrderTables()).hasSize(2);
        assertThat(savedTableGroup.getOrderTables().get(0).getId()).isEqualTo(1L);
        assertThat(savedTableGroup.getOrderTables().get(0).isEmpty()).isFalse();
        assertThat(savedTableGroup.getOrderTables().get(1).getId()).isEqualTo(2L);
        assertThat(savedTableGroup.getOrderTables().get(1).isEmpty()).isFalse();
    }

    @DisplayName("테이블 그룹의 테이블이 비어있으면 예외가 발생한다.")
    @Test
    void create_failEmptyTables() {
        // given
        final TableGroup tableGroup = TableGroup.forSave(LocalDateTime.now(), Collections.emptyList());

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹의 테이블이 2개 미만이면 예외가 발생한다.")
    @Test
    void create_failOneTable() {
        // given
        final TableGroup tableGroup = TableGroup.forSave(LocalDateTime.now(), List.of(
            OrderTable.forSave(1L, 2, true)
        ));

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 개수가 저장된 테이블 개수와 다르면 예외가 발생한다.")
    @Test
    void create_failDifferentSize() {
        // given
        final TableGroup tableGroup = TableGroup.forSave(LocalDateTime.now(), List.of(
            OrderTable.forSave(1L, 2, true),
            OrderTable.forSave(2L, 2, true)
        ));

        given(orderTableDao.findAllByIdIn(any()))
            .willReturn(List.of(
                new OrderTable(1L, null, 2, true)
            ));

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("포함된 테이블들이 비어있지 않으면 예외가 발생한다.")
    @Test
    void create_failNotEmptyTable() {
        // given
        final List<OrderTable> orderTables = List.of(
            new OrderTable(1L, null, 2, false),
            new OrderTable(2L, null, 2, true)
        );
        final TableGroup tableGroup = TableGroup.forSave(LocalDateTime.now(), orderTables);

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블들의 tableGroupId 가 null 이 아니면 예외가 발생한다.")
    @Test
    void create_failNotEmptyGroupId() {
        // given
        final List<OrderTable> orderTables = List.of(
            new OrderTable(1L, null, 2, false),
            new OrderTable(2L, null, 2, true)
        );
        final TableGroup tableGroup = TableGroup.forSave(LocalDateTime.now(), orderTables);

        given(orderTableDao.findAllByIdIn(any()))
            .willReturn(List.of(
                new OrderTable(1L, 1L, 2, true),
                new OrderTable(2L, 1L, 2, true))
            );

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroup() {
        // given
        final Long tableGroupId = 1L;

        given(orderTableDao.findAllByTableGroupId(any()))
            .willReturn(List.of(
                new OrderTable(1L, tableGroupId, 2, false),
                new OrderTable(2L, tableGroupId, 2, false))
            );

        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
            .willReturn(false);

        // when
        // then
        assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroupId));
    }

    @DisplayName("주문이 COMPLETION 상태가 아니면 예외가 발생한다.")
    @Test
    void ungroup_failNotOrderEnd() {
        // given
        final Long tableGroupId = 1L;
        given(orderTableDao.findAllByTableGroupId(any()))
            .willReturn(List.of(
                new OrderTable(1L, tableGroupId, 2, false),
                new OrderTable(2L, tableGroupId, 2, false))
            );

        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
            .willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
            .isInstanceOf(IllegalArgumentException.class);
    }
}

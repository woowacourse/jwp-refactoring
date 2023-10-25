package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.List;
import kitchenpos.application.dto.OrderTableIdRequest;
import kitchenpos.application.dto.TableGroupCreateRequest;
import kitchenpos.application.dto.TableGroupResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void create() {
        // given
        final List<OrderTable> orderTables = List.of(
            new OrderTable(1L, 2, true, Collections.emptyList()),
            new OrderTable(2L, 2, true, Collections.emptyList())
        );

        final TableGroup tableGroup = new TableGroup(1L, orderTables);

        given(orderTableRepository.getAllById(any()))
            .willReturn(orderTables);
        given(tableGroupRepository.save(any()))
            .willReturn(tableGroup);

        // when
        final TableGroupResponse savedTableGroup = tableGroupService.create(
            new TableGroupCreateRequest(List.of(
                new OrderTableIdRequest(1L),
                new OrderTableIdRequest(2L)
            )));

        // then
        assertThat(savedTableGroup.getId()).isEqualTo(tableGroup.getId());
    }

    @DisplayName("테이블 그룹의 테이블이 비어있으면 예외가 발생한다.")
    @Test
    void create_failEmptyTables() {
        // given
        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupCreateRequest(Collections.emptyList())))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹의 테이블이 2개 미만이면 예외가 발생한다.")
    @Test
    void create_failOneTable() {
        // given
        given(orderTableRepository.getAllById(any()))
            .willReturn(List.of(
                new OrderTable(1L, 2, true, Collections.emptyList())
            ));

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupCreateRequest(List.of(
            new OrderTableIdRequest(1L)
        )))).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 개수가 저장된 테이블 개수와 다르면 예외가 발생한다.")
    @Test
    void create_failDifferentSize() {
        // given
        given(orderTableRepository.getAllById(any()))
            .willReturn(List.of(
                new OrderTable(1L, 2, true, Collections.emptyList())
            ));

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupCreateRequest(List.of(
            new OrderTableIdRequest(1L),
            new OrderTableIdRequest(2L)
        )))).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("포함된 테이블들이 비어있지 않으면 예외가 발생한다.")
    @Test
    void create_failNotEmptyTable() {
        // given
        final List<OrderTable> orderTables = List.of(
            new OrderTable(1L, 2, false, Collections.emptyList()),
            new OrderTable(2L, 2, true, Collections.emptyList())
        );

        given(orderTableRepository.getAllById(any()))
            .willReturn(orderTables);

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupCreateRequest(List.of(
            new OrderTableIdRequest(1L),
            new OrderTableIdRequest(2L)
        )))).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블들의 tableGroupId 가 null 이 아니면 예외가 발생한다.")
    @Test
    void create_failNotEmptyGroupId() {
        // given
        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupCreateRequest(List.of(
            new OrderTableIdRequest(1L),
            new OrderTableIdRequest(2L)
        )))).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroup() {
        // given
        final List<OrderTable> orderTables = List.of(
            new OrderTable(1L, 2, true, Collections.emptyList()),
            new OrderTable(2L, 2, true, Collections.emptyList())
        );
        final TableGroup tableGroup = new TableGroup(1L, orderTables);

        given(tableGroupRepository.getById(any()))
            .willReturn(tableGroup);

        // when
        // then
        assertDoesNotThrow(() -> tableGroupService.ungroup(1L));
    }

    @DisplayName("주문이 COMPLETION 상태가 아니면 예외가 발생한다.")
    @Test
    void ungroup_failNotOrderEnd() {
        // given
        final List<OrderTable> orderTables = List.of(
            new OrderTable(1L, 2, false, List.of(new Order(1L, OrderStatus.COOKING,
                                                           List.of(new OrderLineItem(1L, 1L, null))))),
            new OrderTable(2L, 2, false, List.of(new Order(1L, OrderStatus.COOKING,
                                                           List.of(new OrderLineItem(1L, 1L, null)))))
        );
        final TableGroup tableGroup = new TableGroup(1L, orderTables);

        given(tableGroupRepository.getById(any()))
            .willReturn(tableGroup);

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}

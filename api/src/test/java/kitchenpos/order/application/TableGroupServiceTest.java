package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.application.TableGroupService;
import kitchenpos.order.application.dto.OrderTablesRequest;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.exception.OrderTableException.NotExistsOrderTableException;
import kitchenpos.order.domain.exception.TableGroupException.CannotAssignOrderTableException;
import kitchenpos.order.domain.exception.TableGroupException.ExistsNotCompletionOrderException;
import kitchenpos.order.domain.exception.TableGroupException.InsufficientOrderTableSizeException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.order.repository.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    private final OrderTable orderTable1 = new OrderTable(10);
    private final OrderTable orderTable2 = new OrderTable(10);
    @InjectMocks
    private TableGroupService tableGroupService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;

    @Test
    @DisplayName("테이블 그룹을 생성할 수 있다.")
    void create_success() {
        when(orderTableRepository.findAllById(anyList())).thenReturn(List.of(orderTable1, orderTable2));
        when(tableGroupRepository.save(any())).thenReturn(new TableGroup());

        OrderTablesRequest orderTablesRequest = new OrderTablesRequest(List.of(1L, 2L));

        tableGroupService.create(orderTablesRequest);

        assertSoftly(softAssertions -> {
                    softAssertions.assertThat(orderTable1.getTableGroup()).isNotNull();
                    softAssertions.assertThat(orderTable1.isEmpty()).isFalse();
                }
        );
    }

    @Test
    @DisplayName("주문 테이블이 두 개 미만이면 테이블 그룹을 생성할 수 없다.")
    void create_fail_orderTable_count_less_than_2() {
        when(orderTableRepository.findAllById(anyList())).thenReturn(List.of(orderTable1));

        OrderTablesRequest orderTablesRequest = new OrderTablesRequest(List.of(1L));

        assertThatThrownBy(() -> tableGroupService.create(orderTablesRequest))
                .isInstanceOf(InsufficientOrderTableSizeException.class);
    }

    @Test
    @DisplayName("테이블 그룹에 포함된 주문 테이블 번호는 현재 존재하는 주문 테이블 번호에 포함되어 있지 않으면 예외가 발생한다.")
    void create_fail_no_OrderTable() {
        when(orderTableRepository.findAllById(anyList())).thenReturn(List.of(orderTable2));

        OrderTablesRequest orderTablesRequest = new OrderTablesRequest(List.of(1L, 2L));

        assertThatThrownBy(() -> tableGroupService.create(orderTablesRequest))
                .isInstanceOf(NotExistsOrderTableException.class);
    }

    @Test
    @DisplayName("주문 테이블은 매핑된 테이블 그룹의 번호가 있으면 예외가 발생한다.")
    void create_fail_cannot_assign_tableGroup1() {
        orderTable1.group(new TableGroup());

        when(orderTableRepository.findAllById(anyList())).thenReturn(List.of(orderTable1, orderTable2));

        OrderTablesRequest orderTablesRequest = new OrderTablesRequest(List.of(1L, 2L));

        assertThatThrownBy(() -> tableGroupService.create(orderTablesRequest))
                .isInstanceOf(CannotAssignOrderTableException.class);
    }

    @Test
    @DisplayName("주문 테이블은 빈 테이블이 아니면 예외가 발생한다.")
    void create_fail_cannot_assign_tableGroup2() {
        orderTable1.changeEmpty(false);
        when(orderTableRepository.findAllById(anyList())).thenReturn(List.of(orderTable1, orderTable2));

        OrderTablesRequest orderTablesRequest = new OrderTablesRequest(List.of(1L, 2L));

        assertThatThrownBy(() -> tableGroupService.create(orderTablesRequest))
                .isInstanceOf(CannotAssignOrderTableException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 해제할 수 있다.")
    void ungroup_success() {
        TableGroup tableGroup = new TableGroup();
        orderTable1.group(tableGroup);

        when(orderTableRepository.findByTableGroupId(1L)).thenReturn(List.of(orderTable1));
        when(orderRepository.existsByOrderTableInAndOrderStatusIn(anyList(), anyList())).thenReturn(false);

        // ungroup 이전
        assertThat(orderTable1.getTableGroup()).isEqualTo(tableGroup);

        tableGroupService.ungroup(1L);

        // ungroup 이후
        assertSoftly(softAssertions -> {
                    softAssertions.assertThat(orderTable1.getTableGroup()).isNull();
                    softAssertions.assertThat(orderTable1.isEmpty()).isFalse();
                }
        );
    }

    @Test
    @DisplayName("테이블 그룹에 포함된 주문 테이블 중 COOKING 상태이거나 MEAL 상태인 주문의 주문 테이블이 있으면 예외가 발생한다.")
    void ungroup_fail() {
        when(orderTableRepository.findByTableGroupId(1L)).thenReturn(List.of(orderTable1));
        when(orderRepository.existsByOrderTableInAndOrderStatusIn(anyList(), anyList())).thenReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(ExistsNotCompletionOrderException.class);
    }
}


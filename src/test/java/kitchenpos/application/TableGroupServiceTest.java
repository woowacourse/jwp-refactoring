package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.exception.OrderTableException.NotExistsOrderTableException;
import kitchenpos.domain.exception.TableGroupException.CannotAssignOrderTableException;
import kitchenpos.domain.exception.TableGroupException.ExistsNotCompletionOrderException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
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
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;

    private OrderTable orderTable1 = new OrderTable(10);
    private OrderTable orderTable2 = new OrderTable(10);

    @BeforeEach
    void init() {
        orderTable1.setId(1L);
        orderTable2.setId(2L);

        orderTable1.setEmpty(true);
        orderTable2.setEmpty(true);

        orderTable1.setTableGroup(null);
        orderTable2.setTableGroup(null);
    }

    @Test
    @DisplayName("테이블 그룹을 생성할 수 있다.")
    void create_success() {
        LocalDateTime start = LocalDateTime.now();
        when(orderTableRepository.countByIdIn(List.of(orderTable1.getId(), orderTable2.getId()))).thenReturn(2L);
        when(tableGroupRepository.save(any())).thenReturn(TableGroup.from(List.of(new OrderTable(10), new OrderTable(10))));

        TableGroup actual = tableGroupService.create(List.of(orderTable1, orderTable2));
        LocalDateTime end = LocalDateTime.now();

        // 테이블 그룹 생성 이후 변경 상태
        assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual.getCreatedDate()).isBetween(start, end);
                    softAssertions.assertThat(actual.getOrderTables().get(0).getTableGroup()).isEqualTo(actual);
                    softAssertions.assertThat(actual.getOrderTables().get(0).isEmpty()).isFalse();
                }
        );
    }

    @Test
    @DisplayName("테이블 그룹에 포함된 주문 테이블 번호는 현재 존재하는 주문 테이블 번호에 포함되어 있지 않으면 예외가 발생한다.")
    void create_fail_no_OrderTable() {
        when(orderTableRepository.countByIdIn(List.of(orderTable1.getId(), orderTable2.getId())))
                .thenReturn(1L);

        assertThatThrownBy(() -> tableGroupService.create(List.of(orderTable1, orderTable2)))
                .isInstanceOf(NotExistsOrderTableException.class);
    }

    @Test
    @DisplayName("주문 테이블은 매핑된 테이블 그룹의 번호가 있으면 예외가 발생한다.")
    void create_fail_cannot_assign_tableGroup1() {
        orderTable1.setTableGroup(TableGroup.from(List.of(new OrderTable(10), new OrderTable(10))));

        assertThatThrownBy(() -> tableGroupService.create(List.of(orderTable1, orderTable2)))
                .isInstanceOf(CannotAssignOrderTableException.class);
    }

    @Test
    @DisplayName("주문 테이블은 빈 테이블이 아니면 예외가 발생한다.")
    void create_fail_cannot_assign_tableGroup2() {
        orderTable1.setEmpty(false);

        assertThatThrownBy(() -> tableGroupService.create(List.of(orderTable1, orderTable2)))
                .isInstanceOf(CannotAssignOrderTableException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 해제할 수 있다.")
    void ungroup_success() {
        TableGroup tableGroup = TableGroup.from(List.of(orderTable1, orderTable2));

        when(tableGroupRepository.getById(1L)).thenReturn(tableGroup);
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
        long tableGroupId = 1L;
        TableGroup tableGroup = TableGroup.from(List.of(orderTable1, orderTable2));
        when(tableGroupRepository.getById(1L)).thenReturn(tableGroup);
        when(orderRepository.existsByOrderTableInAndOrderStatusIn(anyList(), anyList())).thenReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(ExistsNotCompletionOrderException.class);
    }
}


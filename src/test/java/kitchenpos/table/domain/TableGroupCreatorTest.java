package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.TestFixtures;
import kitchenpos.table.application.dto.OrderTableRequest;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.tablegroup.application.dto.TableGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("테이블그룹 생성시 주문의 유효성 검사 후, 생성한다")
@ExtendWith(MockitoExtension.class)
class TableGroupCreatorTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableGroupCreatable tableGroupCreator;

    @DisplayName("주문 테이블이 비어있지 않아야 된다")
    @Test
    void createExceptionEmpty() {
        final OrderTable wrongOrderTable = OrderTable.builder()
                .id(1L)
                .empty(false)
                .build();
        final OrderTable normalOrderTable = OrderTable.builder()
                .id(2L)
                .empty(true)
                .build();
        final List<OrderTable> orderTables = Arrays.asList(wrongOrderTable, normalOrderTable);
        final TableGroupRequest tableGroupRequest = TestFixtures.createTableGroupRequest(Arrays.asList(1L, 2L));

        when(orderTableRepository.findAllByIdIn(any())).thenReturn(orderTables);

        assertThatThrownBy(() -> tableGroupCreator.create(any(), tableGroupRequest)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 목록이 2 보다 작으면 안 된다")
    @Test
    void createExceptionUnderTwo() {
        final TableGroupRequest request = new TableGroupRequest(Collections.singletonList(new OrderTableRequest(1L)));

        assertThatThrownBy(() -> tableGroupCreator.create(1L, request)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블로 등록되어 있는 목록과 단체 주문에 있는 주문 테이블 목록과 크기가 같아야 한다")
    @Test
    void createExceptionTablesSize() {
        final OrderTable savedOrderTable1 = OrderTable.builder()
                .id(1L)
                .empty(true)
                .build();
        final OrderTable savedOrderTable2 = OrderTable.builder()
                .id(2L)
                .empty(true)
                .build();
        final OrderTable orderTable3 = OrderTable.builder()
                .id(3L)
                .empty(true)
                .build();
        final TableGroupRequest request = TestFixtures.createTableGroupRequest(
                Arrays.asList(savedOrderTable1.getId(), savedOrderTable2.getId())
        );
        final List<OrderTable> savedOrderTables = Arrays.asList(savedOrderTable1, savedOrderTable2, orderTable3);
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(savedOrderTables);

        assertThatThrownBy(() -> tableGroupCreator.create(any(), request)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되어 있는 주문 테이블이 비어 있지 않으면 예외가 발생한다")
    @Test
    void createExceptionSavedEmpty() {
        final OrderTable savedOrderTable1 = OrderTable.builder()
                .id(1L)
                .empty(false)
                .build();
        final OrderTable savedOrderTable2 = OrderTable.builder()
                .id(2L)
                .empty(true)
                .build();
        final List<OrderTable> savedOrderTables = Arrays.asList(savedOrderTable1, savedOrderTable2);
        final TableGroupRequest request = TestFixtures.createTableGroupRequest(
                Arrays.asList(savedOrderTable1.getId(), savedOrderTable2.getId())
        );
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(savedOrderTables);

        assertThatThrownBy(() -> tableGroupCreator.create(any(), request)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("등록되어 있는 주문 테이블의 그룹 지정이 있다면 예외를 발생한다")
    @Test
    void createExceptionEmptyAndGroup() {
        final OrderTable savedOrderTable1 = OrderTable.builder()
                .id(1L)
                .empty(true)
                .build();
        final OrderTable savedOrderTable2 = OrderTable.builder()
                .id(2L)
                .empty(true)
                .tableGroupId(1L)
                .build();
        final List<OrderTable> savedOrderTables = Arrays.asList(savedOrderTable1, savedOrderTable2);
        final TableGroupRequest request = TestFixtures.createTableGroupRequest(
                Arrays.asList(savedOrderTable1.getId(), savedOrderTable2.getId())
        );

        when(orderTableRepository.findAllByIdIn(any())).thenReturn(savedOrderTables);

        assertThatThrownBy(() -> tableGroupCreator.create(any(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

}

package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kitchenpos.application.dto.request.OrderTableRequest;
import kitchenpos.application.dto.request.TableGroupCreateRequest;
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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

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
    @DisplayName("테이블 그룹을 성공적으로 생성한다")
    void testCreateSuccess() {
        //given
        final OrderTable orderTable1 = new OrderTable(1L, null, 1, true);
        final OrderTable orderTable2 = new OrderTable(2L, null, 1, true);
        final LocalDateTime now = LocalDateTime.now();
        final TableGroup savedTableGroup = new TableGroup(1L, LocalDateTime.now(), List.of(orderTable1, orderTable2));

        final List<OrderTableRequest> orderTableRequests = List.of(new OrderTableRequest(1, true), new OrderTableRequest(1, true));
        final TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableRequests);


        when(orderTableDao.findAllByIdIn(any()))
                .thenReturn(List.of(orderTable1, orderTable2));
        when(tableGroupDao.save(any()))
                .thenReturn(savedTableGroup);


        //when
        final TableGroup result = tableGroupService.create(tableGroupCreateRequest);

        //then
        assertThat(result).isEqualTo(new TableGroup(1L, now, List.of(orderTable1, orderTable2)));
    }

    @Test
    @DisplayName("테이블 그룹 생성 시 테이블이 비어있을 경우 예외가 발생한다")
    void testCreateWhenOrderTablesEmptyFailure() {
        //given
        final TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(Collections.emptyList());

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹 생성 시 주문 테이블들의 개수가 2미만일 경우 예외가 발생한다")
    void testCreateWhenOrderTablesSizeLowerThanTwoFailure() {
        //given
        final List<OrderTableRequest> orderTableRequests = List.of(new OrderTableRequest(1, true));
        final TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableRequests);

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹 생성 시 저장된 테이블 수와 요청 테이블 수가 일치하지 않을 경우 예외가 발생한다")
    void testCreateWhenSavedOrderTablesAndOrderTableSizeNotMatchFailure() {
        //given
        final OrderTable orderTable1 = new OrderTable(1L, null, 1, true);

        final List<OrderTableRequest> orderTableRequests = List.of(new OrderTableRequest(1, true), new OrderTableRequest(1, true));
        final TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableRequests);

        when(orderTableDao.findAllByIdIn(any()))
                .thenReturn(List.of(orderTable1));

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹 생성 시 빈 테이블이 아닐 경우 예외가 발생한다")
    void testCreateWhenSavedOrderTableIsNotEmptyFailure() {
        //given
        final OrderTable orderTable1 = new OrderTable(1L, null, 1, true);
        final OrderTable orderTable2 = new OrderTable(2L, null, 1, false);

        final List<OrderTableRequest> orderTableRequests = List.of(new OrderTableRequest(1, true),
                new OrderTableRequest(1, false));
        final TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableRequests);

        when(orderTableDao.findAllByIdIn(any()))
                .thenReturn(List.of(orderTable1, orderTable2));

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹 생성 시 저장된 테이블이 테이블 그룹을 가지고 있을 경우 예외가 발생한다")
    void testCreateWhenSavedOrderTableHasTableGroupIdFailure() {
        //given
        final OrderTable orderTable1 = new OrderTable(1L, 1L, 1, true);
        final OrderTable orderTable2 = new OrderTable(2L, 1L, 1, true);

        final List<OrderTableRequest> orderTableRequests = List.of(new OrderTableRequest(1, true),
                new OrderTableRequest(1, false));
        final TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableRequests);

        when(orderTableDao.findAllByIdIn(any()))
                .thenReturn(List.of(orderTable1, orderTable2));

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 성공적으로 해제한다")
    void testUngroupSuccess() {
        // given
        final Long tableGroupId = 1L;
        final OrderTable orderTable = new OrderTable(1L, 1L, 1, false);

        final List<OrderTable> mockOrderTables = List.of(orderTable);

        when(orderTableDao.findAllByTableGroupId(any(Long.class)))
                .thenReturn(mockOrderTables);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
                .thenReturn(false);

        // when
        tableGroupService.ungroup(tableGroupId);

        // then
        verify(orderTableDao).findAllByTableGroupId(tableGroupId);
        verify(orderDao).existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList());
        verify(orderTableDao).save(any(OrderTable.class));

    }

    @Test
    @DisplayName("테이블 그룹 해제 시 이미 요리 중이거나 식사중일 경우 예외가 발생한다")
    void testUngroupWhenOrderTableAlreadyCookOrMealFailure() {
        //given
        final Long tableGroupId = 1L;
        final OrderTable orderTable = new OrderTable(1L, 1L, 1, false);

        final List<OrderTable> mockOrderTables = List.of(orderTable);

        when(orderTableDao.findAllByTableGroupId(any()))
                .thenReturn(mockOrderTables);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
                .thenReturn(true);

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(IllegalArgumentException.class);

    }
}

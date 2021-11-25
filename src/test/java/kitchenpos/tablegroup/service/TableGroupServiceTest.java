package kitchenpos.tablegroup.service;

import static kitchenpos.fixtures.TableFixtures.createGroupedOrderTable;
import static kitchenpos.fixtures.TableFixtures.createOrderTable;
import static kitchenpos.fixtures.TableFixtures.createOrderTables;
import static kitchenpos.fixtures.TableFixtures.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.fixtures.OrderFixtures;
import kitchenpos.fixtures.ServiceTest;
import kitchenpos.fixtures.TableFixtures;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import kitchenpos.tablegroup.service.dto.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class TableGroupServiceTest extends ServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    private TableGroup tableGroup;
    private TableGroupRequest request;

    @BeforeEach
    void setUp() {
        tableGroup = createTableGroup();
        request = new TableGroupRequest(Arrays.asList(1L, 2L));
    }

    @Test
    void 단체_지정을_생성한다() {
        List<OrderTable> orderTables = createOrderTables(true);
        given(orderTableRepository.findAllByIdIn(any())).willReturn(orderTables);
        given(tableGroupRepository.save(any())).willReturn(tableGroup);

        assertDoesNotThrow(() -> tableGroupService.create(request));
        orderTables.forEach(orderTable -> assertThat(orderTable.getTableGroupId()).isNotNull());
    }

    @Test
    void 생성_시_주문_테이블들이_존재하지_않으면_예외를_반환한다() {
        given(orderTableRepository.findAllByIdIn(any()))
            .willReturn(Collections.singletonList(TableFixtures.createOrderTable(true)));

        assertThrows(NoSuchElementException.class, () -> tableGroupService.create(request));
    }

    @Test
    void 생성_시_지정_할_주문_테이블들이_2개_미만_이면_예외를_반환한다() {
        given(orderTableRepository.findAllByIdIn(any())).willReturn(createOrderTables(true));
        TableGroupRequest invalidRequest = new TableGroupRequest(Collections.singletonList(1L));

        assertThrows(IllegalStateException.class, () -> tableGroupService.create(invalidRequest));
    }

    @Test
    void 생성_시_주문_테이블들이_비어있지_않으면_예외를_반환한다() {
        given(orderTableRepository.findAllByIdIn(any())).willReturn(createOrderTables(false));
        given(tableGroupRepository.save(any())).willReturn(tableGroup);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> tableGroupService.create(request));
        assertThat(exception.getMessage()).isEqualTo("빈 테이블이 아닙니다.");
    }

    @Test
    void 생성_시_주문_테이블들이_단체_지정_되어있으면_예외를_반환한다() {
        List<OrderTable> groupedTables = Arrays.asList(createGroupedOrderTable(true), createGroupedOrderTable(true));
        given(orderTableRepository.findAllByIdIn(any())).willReturn(groupedTables);
        given(tableGroupRepository.save(any())).willReturn(tableGroup);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> tableGroupService.create(request));
        assertThat(exception.getMessage()).isEqualTo("이미 단체 지정된 테이블입니다.");
    }

    @Test
    void 단체_지정을_해제한다() {
        List<OrderTable> groupedTables = new ArrayList<>();
        groupedTables.add(createOrderTable(1L, 1L, 10, true));
        groupedTables.add(createOrderTable(2L, 1L, 10, true));
        given(orderTableRepository.findAllByTableGroupId(any())).willReturn(groupedTables);
        given(orderRepository.findAllByOrderTableIdIn(any())).willReturn(OrderFixtures.createCompletedOrders());

        assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroup.getId()));
        groupedTables
            .forEach(orderTable -> assertThat(orderTable.isGrouped()).isFalse());
    }

    @Test
    void 해제_시_주문_테이블들의_주문_상태가_모두_완료되지_않았으면_예외를_반환한다() {
        List<OrderTable> groupedTables = new ArrayList<>();
        groupedTables.add(createOrderTable(1L, 1L, 10, true));
        groupedTables.add(createOrderTable(2L, 1L, 10, true));
        given(orderTableRepository.findAllByTableGroupId(any())).willReturn(groupedTables);
        given(orderRepository.findAllByOrderTableIdIn(any())).willReturn(OrderFixtures.createMealOrders());

        assertThrows(IllegalStateException.class, () -> tableGroupService.ungroup(tableGroup.getId()));
    }
}
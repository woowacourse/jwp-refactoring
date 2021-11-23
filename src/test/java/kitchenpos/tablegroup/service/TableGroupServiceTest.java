package kitchenpos.tablegroup.service;

import static kitchenpos.fixtures.TableFixtures.createOrderTable;
import static kitchenpos.fixtures.TableFixtures.createOrderTables;
import static kitchenpos.fixtures.TableFixtures.createTableGroup;
import static kitchenpos.fixtures.TableFixtures.createTableGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
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
import kitchenpos.tablegroup.service.dto.TableGroupResponse;
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
        request = createTableGroupRequest();
    }

    @Test
    void 단체_지정을_생성한다() {
        given(orderTableRepository.findAllByIdIn(any())).willReturn(createOrderTables(true));
        given(tableGroupRepository.save(any())).willReturn(tableGroup);

        TableGroupResponse savedTableGroup = assertDoesNotThrow(() -> tableGroupService.create(request));
        savedTableGroup.getOrderTables()
            .forEach(orderTable -> assertThat(orderTable.getTableGroup()).isNotNull());
    }

    @Test
    void 생성_시_주문_테이블들이_존재하지_않으면_예외를_반환한다() {
        given(orderTableRepository.findAllByIdIn(any()))
            .willReturn(Collections.singletonList(TableFixtures.createOrderTable(true)));

        assertThrows(NoSuchElementException.class, () -> tableGroupService.create(request));
    }

    @Test
    void 단체_지정을_해제한다() {
        List<OrderTable> groupedTables = new ArrayList<>();
        groupedTables.add(createOrderTable(1L, createTableGroup(), 10, true));
        groupedTables.add(createOrderTable(2L, createTableGroup(), 10, true));
        given(orderTableRepository.findAllByTableGroupId(any())).willReturn(groupedTables);
        given(orderRepository.findAllByOrderTableIdIn(any())).willReturn(OrderFixtures.createCompletedOrders());

        assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroup.getId()));
        groupedTables
            .forEach(orderTable -> assertThat(orderTable.isGrouped()).isFalse());
    }

    @Test
    void 해제_시_주문_테이블들의_주문_상태가_모두_완료되지_않았으면_예외를_반환한다() {
        List<OrderTable> groupedTables = new ArrayList<>();
        groupedTables.add(createOrderTable(1L, createTableGroup(), 10, true));
        groupedTables.add(createOrderTable(2L, createTableGroup(), 10, true));
        given(orderTableRepository.findAllByTableGroupId(any())).willReturn(groupedTables);
        given(orderRepository.findAllByOrderTableIdIn(any())).willReturn(OrderFixtures.createMealOrders());

        assertThrows(IllegalStateException.class, () -> tableGroupService.ungroup(tableGroup.getId()));
    }
}
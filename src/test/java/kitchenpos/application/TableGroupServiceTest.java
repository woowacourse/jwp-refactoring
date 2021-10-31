package kitchenpos.application;

import static kitchenpos.fixtures.OrderFixtures.createCompletedOrders;
import static kitchenpos.fixtures.OrderFixtures.createMealOrders;
import static kitchenpos.fixtures.TableFixtures.createOrderTable;
import static kitchenpos.fixtures.TableFixtures.createTableGroup;
import static kitchenpos.fixtures.TableFixtures.createTableGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.dto.TableGroupRequest;
import kitchenpos.application.dto.TableGroupResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixtures.TableFixtures;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
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
        given(orderTableRepository.findAllByIdIn(any())).willReturn(tableGroup.getOrderTables());
        given(tableGroupRepository.save(any())).willReturn(tableGroup);

        TableGroupResponse savedTableGroup = assertDoesNotThrow(() -> tableGroupService.create(request));
        savedTableGroup.getOrderTables()
            .forEach(orderTable -> assertThat(orderTable.getTableGroup()).isNotNull());
    }

    @Test
    void 생성_시_지정_할_주문_테이블들이_2개_미만_이면_예외를_반환한다() {
        TableGroupRequest invalidTableGroup = createTableGroupRequest(
            createTableGroup(Collections.singletonList(TableFixtures.createOrderTable(true)))
        );

        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(invalidTableGroup));
    }

    @Test
    void 생성_시_주문_테이블들이_존재하지_않으면_예외를_반환한다() {
        given(orderTableRepository.findAllByIdIn(any()))
            .willReturn(Collections.singletonList(TableFixtures.createOrderTable(true)));

        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(request));
    }

    @Test
    void 생성_시_주문_테이블들이_비어있으면_예외를_반환한다() {
        given(orderTableRepository.findAllByIdIn(any())).willReturn(TableFixtures.createOrderTables(false));

        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(request));
    }

    @Test
    void 생성_시_주문_테이블들이_단체_지정_되어있으면_예외를_반환한다() {
        List<OrderTable> groupedTables = new ArrayList<>();
        groupedTables.add(TableFixtures.createOrderTable(1L, createTableGroup(), createCompletedOrders(), 10, true));
        groupedTables.add(TableFixtures.createOrderTable(2L, createTableGroup(), createCompletedOrders(), 10, true));
        given(orderTableRepository.findAllByIdIn(any())).willReturn(groupedTables);

        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(request));
    }

    @Test
    void 단체_지정을_해제한다() {
        List<OrderTable> groupedTables = new ArrayList<>();
        groupedTables.add(createOrderTable(1L, createTableGroup(), createCompletedOrders(), 10, true));
        groupedTables.add(createOrderTable(2L, createTableGroup(), createCompletedOrders(), 10, true));
        given(orderTableRepository.findAllByTableGroupId(any())).willReturn(groupedTables);

        assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroup.getId()));
        tableGroup.getOrderTables()
            .forEach(orderTable -> assertThat(orderTable.getTableGroup()).isNull());
    }

    @Test
    void 해제_시_주문_테이블들의_주문_상태가_모두_완료되지_않았으면_예외를_반환한다() {
        List<OrderTable> groupedTables = new ArrayList<>();
        groupedTables.add(createOrderTable(1L, createTableGroup(), createMealOrders(), 10, true));
        groupedTables.add(createOrderTable(2L, createTableGroup(), createMealOrders(), 10, true));
        given(orderTableRepository.findAllByTableGroupId(any())).willReturn(tableGroup.getOrderTables());

        assertThrows(IllegalStateException.class, () -> tableGroupService.ungroup(tableGroup.getId()));
    }
}
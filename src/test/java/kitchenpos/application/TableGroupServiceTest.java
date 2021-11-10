package kitchenpos.application;

import kitchenpos.annotation.IntegrationTest;
import kitchenpos.domain.*;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@IntegrationTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    private final List<OrderTable> validOrderTables = new ArrayList<>();

    @BeforeEach
    void setUp() {
        OrderTable orderTable1 = new OrderTable(1L);
        OrderTable orderTable2 = new OrderTable(2L);

        validOrderTables.add(orderTable1);
        validOrderTables.add(orderTable2);
    }

    @ParameterizedTest
    @DisplayName("올바르지 않은 orderTables가 주어질 경우 TableGroup을 등록할 수 없다.")
    @MethodSource("invalidOrderTables")
    public void invalidOrderTablesException(List<OrderTable> orderTables) {
        //given & when
        TableGroup tableGroup = new TableGroup(orderTables);

        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> invalidOrderTables() {
        OrderTable orderTable = new OrderTable(1L);

        OrderTable unRegisteredOrderTable = new OrderTable(10L);

        return Stream.of(
                Arguments.of(Collections.emptyList()),  //주어지는 order_table 은 비어있어서는 안된다.
                Arguments.of(Collections.singletonList(orderTable)), //2개 미만의 order_table 요청이어서는 안된다.
                Arguments.of(Arrays.asList(orderTable, orderTable)) //중복된 order_table 요청이어서는 안된다.
        );
    }

    @Test
    @DisplayName("empty 상태가 order_table 이어서는 안된다.")
    public void emptyStatusOrderTableException() {
        //given
        TableGroup tableGroup = new TableGroup(validOrderTables);

        //when
        OrderTable emptyOrderTable = new OrderTable(false);
        tableService.changeEmpty(tableGroup.getOrderTables().get(0).getId(), emptyOrderTable);

        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("다른 table_group에 속한 order_table이 있어서는 안된다.")
    public void includeAlreadyContainedOtherTableGroupOrderTableException() {
        //given
        TableGroup tableGroup1 = new TableGroup(validOrderTables);

        TableGroup tableGroup2 = new TableGroup(validOrderTables);

        //when
        tableGroupService.create(tableGroup1);

        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("TableGroup을 등록할 수 있다.")
    public void enrollTableGroup() {
        //given & when
        TableGroup tableGroup = new TableGroup(validOrderTables);

        //then
        assertDoesNotThrow(() -> tableGroupService.create(tableGroup));
    }

    @Test
    @DisplayName("TableGroup 생성 후에 해당 group에 속한 order_table들의 table_group_id에 생성된 TableGroup의 id가 할당되어야 하고," +
            " empty 필드가 false로 변경되어야 한다.")
    public void allocatedTableGroupIdAndChangeEmptyStatus() {
        //given
        TableGroup tableGroup = new TableGroup(validOrderTables);

        //when
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        //then
        for (OrderTable orderTable : savedTableGroup.getOrderTables()) {
            assertThat(orderTable.getTableGroupId()).isNotNull();
            assertThat(orderTable.isEmpty()).isFalse();
        }
    }

    @Test
    @DisplayName("TableGroup을 해제할 수 있다.")
    public void unGroup() {
        //given
        TableGroup tableGroup = new TableGroup(validOrderTables);

        //when
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        //then
        assertDoesNotThrow(() -> tableGroupService.ungroup(savedTableGroup.getId()));
    }

    @Test
    @DisplayName("TableGroup에 속한 order_table이 현재 이용 중이면(COMPLETION이 아니면) 해제할 수 없다.")
    public void cannotUnGrouptWhenTableActivatedException() {
        //given
        TableGroup tableGroup = new TableGroup(validOrderTables);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        //when
        Order savedOrder = enrollOrder();

        //then
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("TableGroup에 속한 order_table이 COMPLETION 이면 해제할 수 있다.")
    public void unGrouptWhenOrderStatusCOMPLETION() {
        //given
        TableGroup tableGroup = new TableGroup(validOrderTables);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        //when
        Order savedOrder = enrollOrder();
        Order completedOrder = new Order(OrderStatus.COMPLETION.name());
        orderService.changeOrderStatus(savedOrder.getId(), completedOrder);

        //then
        assertDoesNotThrow(() -> tableGroupService.ungroup(savedTableGroup.getId()));
    }

    private Order enrollOrder() {
        Menu findMenu = menuRepository.findById(1L).get();
        OrderTable findOrderTable = orderTableRepository.findById(1L).get();
        OrderLineItem orderLineItem = new OrderLineItem(findMenu, 1L);
        Order order = new Order(findOrderTable, Collections.singletonList(orderLineItem));

        return orderService.create(order);
    }
}
package kitchenpos.application;

import kitchenpos.Order.domain.Order;
import kitchenpos.Order.domain.OrderLineItem;
import kitchenpos.Order.domain.OrderStatus;
import kitchenpos.OrderTable.application.TableGroupService;
import kitchenpos.OrderTable.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @ParameterizedTest
    @DisplayName("올바르지 않은 orderTables가 주어질 경우 TableGroup을 등록할 수 없다.")
    @MethodSource("invalidOrderTables")
    public void invalidOrderTablesException(List<Long> orderTableIds) {
        assertThatThrownBy(() -> tableGroupService.create(orderTableIds))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> invalidOrderTables() {
        return Stream.of(
                Arguments.of(Collections.emptyList()),  //주어지는 order_table 은 비어있어서는 안된다.
                Arguments.of(Collections.singletonList(1L)), //2개 미만의 order_table 요청이어서는 안된다.
                Arguments.of(Arrays.asList(1L, 1L)) //중복된 order_table 요청이어서는 안된다.
        );
    }

    @Test
    @DisplayName("empty 상태가 아닌 orderTable 이어서는 안된다.")
    public void emptyStatusOrderTableException() {
        assertThatThrownBy(() -> tableGroupService.create(emptyFalseOrderTableIds()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("다른 table_group에 속한 order_table이 있어서는 안된다.")
    public void includeAlreadyContainedOtherTableGroupOrderTableException() {
        //given&when
        tableGroupService.create(emptyTrueOrderTableIds());

        //then
        assertThatThrownBy(() -> tableGroupService.create(emptyTrueOrderTableIds()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("TableGroup을 등록할 수 있다.")
    public void enrollTableGroup() {
        assertDoesNotThrow(() -> tableGroupService.create(emptyTrueOrderTableIds()));
    }

    @Test
    @DisplayName("TableGroup을 해제할 수 있다.")
    public void unGroup() {
        //given&when
        TableGroup savedTableGroup = tableGroupService.create(emptyTrueOrderTableIds());

        //then
        assertDoesNotThrow(() -> tableGroupService.ungroup(savedTableGroup.getId()));
    }

    @Test
    @DisplayName("TableGroup에 속한 order_table의 Order 상태가 현재 이용 중이면(COMPLETION이 아니면) 해제할 수 없다.")
    public void cannotUnGrouptWhenTableActivatedException() {
        //given&when
        List<Long> orderTableIds = emptyTrueOrderTableIds();
        TableGroup savedTableGroup = tableGroupService.create(orderTableIds);
        OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 1L);
        orderService.create(
                new Order(orderTableIds.get(0), Collections.singletonList(orderLineItem))
        );

        //then
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("TableGroup에 속한 order_table의 Order 상태가 COMPLETION 이면 해제할 수 있다.")
    public void unGrouptWhenOrderStatusCOMPLETION() {
        //given
        List<Long> orderTableIds = emptyTrueOrderTableIds();
        TableGroup savedTableGroup = tableGroupService.create(orderTableIds);
        OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 1L);
        Order savedOrder = orderService.create(
                new Order(orderTableIds.get(0), Collections.singletonList(orderLineItem))
        );

        //when
        orderService.changeOrderStatus(savedOrder.getId(), OrderStatus.COMPLETION.name());

        //then
        assertDoesNotThrow(() -> tableGroupService.ungroup(savedTableGroup.getId()));
    }
}
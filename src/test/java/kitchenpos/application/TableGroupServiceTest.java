package kitchenpos.application;

import kitchenpos.application.exceptions.OrderStatusNotCompletionException;
import kitchenpos.config.IsolatedTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.exceptions.InvalidOrderTableSizesException;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.ui.dto.tablegroup.TableGroupRequest;
import kitchenpos.ui.dto.tablegroup.TableGroupResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableGroupServiceTest extends IsolatedTest {

    @Autowired
    private TableGroupService service;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("단체 지정 실패 - 주문 테이블이 없을 경우")
    @Test
    public void createFailZeroTable() {
        TableGroupRequest request = new TableGroupRequest(Lists.newArrayList(1L, 3L, 4L));

        assertThatThrownBy(() -> service.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 실패 - 주문 테이블 갯수가 1개일 경우")
    @Test
    public void createFailSingleTable() {
        orderTableRepository.save(new OrderTable(null, 4, true));
        TableGroupRequest request = new TableGroupRequest(Lists.newArrayList(1L));

        assertThatThrownBy(() -> service.create(request))
            .isInstanceOf(InvalidOrderTableSizesException.class);
    }

    @DisplayName("단체 지정 실패 - 지정하고자 하는 테이블 갯수와 저장가능한 테이블 갯수가 다를 경우")
    @Test
    public void createFailNotMatchTableCount() {
        orderTableRepository.save(new OrderTable(null, 4, true));
        TableGroupRequest request = new TableGroupRequest(Lists.newArrayList(1L, 2L));

        assertThatThrownBy(() -> service.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정")
    @Test
    public void createTableGroup() {
        orderTableRepository.save(new OrderTable(null, 4, true));
        orderTableRepository.save(new OrderTable(null, 5, true));
        TableGroupRequest request = new TableGroupRequest(Lists.newArrayList(1L, 2L));

        final TableGroupResponse response = service.create(request);

        assertThat(response.getOrderTables()).hasSize(2);
        assertThat(response.getOrderTables().get(0).isEmpty()).isFalse();
        assertThat(response.getOrderTables().get(1).isEmpty()).isFalse();
    }

    @DisplayName("단체 해제 실패 - 주문 상태가 조리 중일 경우")
    @Test
    public void unGroupFailOrderStatus_Cooking() {
        orderTableRepository.save(new OrderTable(null, 4, true));
        orderTableRepository.save(new OrderTable(null, 5, true));
        TableGroupRequest request = new TableGroupRequest(Lists.newArrayList(1L, 2L));
        createOrderWithOrderStatusOf(OrderStatus.COOKING);

        final TableGroupResponse response = service.create(request);

        assertThatThrownBy(() -> service.ungroup(response.getId()))
            .isInstanceOf(OrderStatusNotCompletionException.class);
    }

    @DisplayName("단체 해제 실패 - 주문 상태가 식사 중일 경우")
    @Test
    public void unGroupFailOrderStatus_Meal() {
        orderTableRepository.save(new OrderTable(null, 4, true));
        orderTableRepository.save(new OrderTable(null, 5, true));
        TableGroupRequest request = new TableGroupRequest(Lists.newArrayList(1L, 2L));
        createOrderWithOrderStatusOf(OrderStatus.MEAL);

        final TableGroupResponse response = service.create(request);

        assertThatThrownBy(() -> service.ungroup(response.getId()))
                .isInstanceOf(OrderStatusNotCompletionException.class);
    }

    @DisplayName("단체 해제")
    @Test
    public void unGroup() {
        orderTableRepository.save(new OrderTable(null, 4, true));
        orderTableRepository.save(new OrderTable(null, 5, true));
        TableGroupRequest request = new TableGroupRequest(Lists.newArrayList(1L, 2L));
        createOrderWithOrderStatusOf(OrderStatus.COMPLETION);
        createOrderWithOrderStatusOf(OrderStatus.COMPLETION);

        final TableGroupResponse response = service.create(request);
        service.ungroup(response.getId());

        assertThat(response.getOrderTables().get(0).isEmpty()).isFalse();
        assertThat(response.getOrderTables().get(1).isEmpty()).isFalse();
    }

    private void createOrderWithOrderStatusOf(OrderStatus orderStatus) {
        OrderTable orderTable = new OrderTable(1L, null, 4, false);
        Order order = new Order(orderTable, orderStatus, LocalDateTime.now());
        orderRepository.save(order);
    }
}

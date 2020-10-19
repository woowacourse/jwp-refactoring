package kitchenpos.application;

import kitchenpos.config.IsolatedTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
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
    private OrderDao orderDao;

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
            .isInstanceOf(IllegalArgumentException.class);
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
        createOrderWithOrderStatus("COOKING", 1L);

        final TableGroupResponse response = service.create(request);

        assertThatThrownBy(() -> service.ungroup(response.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 해제 실패 - 주문 상태가 식사 중일 경우")
    @Test
    public void unGroupFailOrderStatus_Meal() {
        orderTableRepository.save(new OrderTable(null, 4, true));
        orderTableRepository.save(new OrderTable(null, 5, true));
        TableGroupRequest request = new TableGroupRequest(Lists.newArrayList(1L, 2L));
        createOrderWithOrderStatus("MEAL", 1L);

        final TableGroupResponse response = service.create(request);

        assertThatThrownBy(() -> service.ungroup(response.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 해제")
    @Test
    public void unGroup() {
        orderTableRepository.save(new OrderTable(null, 4, true));
        orderTableRepository.save(new OrderTable(null, 5, true));
        TableGroupRequest request = new TableGroupRequest(Lists.newArrayList(1L, 2L));
        createOrderWithOrderStatus("COMPLETION", 1L);
        createOrderWithOrderStatus("COMPLETION", 2L);

        final TableGroupResponse response = service.create(request);
        service.ungroup(response.getId());

        assertThat(response.getOrderTables().get(0).isEmpty()).isFalse();
        assertThat(response.getOrderTables().get(1).isEmpty()).isFalse();
    }

    private void createOrderWithOrderStatus(String orderStatus, Long orderTableId) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderLineItems(Lists.newArrayList());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);
    }
}

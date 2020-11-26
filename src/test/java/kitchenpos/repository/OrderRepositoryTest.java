package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.Table;
import kitchenpos.domain.tablegroup.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderRepositoryTest extends KitchenPosRepositoryTest {

    @DisplayName("Order 저장 - 성공")
    @Test
    void save_Success() {
        Table table = getCreatedTableWithTableGroup();
        Order order = Order.entityOf(table, OrderStatus.COOKING, null);

        Order savedOrder = orderRepository.save(order);

        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getTable()).isEqualTo(table);
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(savedOrder.getOrderedTime()).isNotNull();
        assertThat(savedOrder.getOrderLineItems()).isNull();
    }

    @DisplayName("Order ID로 Order 조회 - 조회됨, ID가 존재하는 경우")
    @Test
    void findById_ExistsId_ReturnOrder() {
        Table table = getCreatedTableWithTableGroup();
        Order order = Order.entityOf(table, OrderStatus.COOKING, null);
        Order savedOrder = orderRepository.save(order);

        Order foundOrder = orderRepository.findById(savedOrder.getId())
            .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 order가 없습니다."));

        assertThat(foundOrder.getId()).isEqualTo(savedOrder.getId());
        assertThat(foundOrder.getTable()).isEqualTo(savedOrder.getTable());
        assertThat(foundOrder.getOrderStatus()).isEqualTo(savedOrder.getOrderStatus());
        assertThat(foundOrder.getOrderedTime()).isEqualTo(savedOrder.getOrderedTime());
        assertThat(foundOrder.getOrderLineItems()).isEqualTo(savedOrder.getOrderLineItems());
    }

    @DisplayName("Order ID로 Order 조회 - 조회되지 않음, ID가 존재하지 않는 경우")
    @Test
    void findById_NotExistsId_ReturnEmpty() {
        Order order = Order.entityOf(getCreatedTableWithTableGroup(), OrderStatus.COOKING, null);
        Order savedOrder = orderRepository.save(order);

        Optional<Order> foundOrder = orderRepository.findById(savedOrder.getId() + 1);

        assertThat(foundOrder.isPresent()).isFalse();
    }

    @DisplayName("전체 Order 조회 - 성공")
    @Test
    void findAll_Success() {
        Order order = Order.entityOf(getCreatedTableWithTableGroup(), OrderStatus.COOKING, null);
        Order savedOrder = orderRepository.save(order);

        List<Order> orders = orderRepository.findAll();

        assertThat(orders).isNotNull();
        assertThat(orders).isNotEmpty();
        assertThat(orders).contains(savedOrder);
    }

    @DisplayName("OrderTableId와 OrderStatus들과 매치되는 Order가 있는지 확인 - True, 모두 매치")
    @Test
    void existsByTable_IdAndOrderStatusIn_MatchedOrderTableIdAndOrderStatus_ReturnTrue() {
        Table table = getCreatedTableWithTableGroup();
        List<OrderStatus> orderStatuses = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);

        for (OrderStatus orderStatus : orderStatuses) {
            Order order = Order.entityOf(table, orderStatus, null);
            orderRepository.save(order);
        }

        boolean existsOrder = orderRepository
            .existsByTable_IdAndOrderStatusIn(table.getId(), orderStatuses);

        assertThat(existsOrder).isTrue();
    }

    @DisplayName("OrderTableId와 OrderStatus들과 매치되는 Order가 있는지 확인 - False, OrderStatus만 매치")
    @Test
    void existsByTable_IdAndOrderStatusIn_MatchedOnlyOrderStatus_ReturnFalse() {
        Table table = getCreatedTableWithTableGroup();
        Table otherTable = getCreatedTableWithTableGroup();
        List<OrderStatus> orderStatuses
            = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);

        for (OrderStatus orderStatus : orderStatuses) {
            Order order = Order.entityOf(table, orderStatus, null);
            orderRepository.save(order);
        }

        boolean existsOrder = orderRepository
            .existsByTable_IdAndOrderStatusIn(otherTable.getId(), orderStatuses);

        assertThat(existsOrder).isFalse();
    }

    @DisplayName("OrderTableId와 OrderStatus들과 매치되는 Order가 있는지 확인 - False, OrderTableId만 매치")
    @Test
    void existsByTable_IdAndOrderStatusIn_MatchedOnlyOrderTableId_ReturnFalse() {
        Table table = getCreatedTableWithTableGroup();
        List<OrderStatus> orderStatuses = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);

        Order order = Order.entityOf(table, OrderStatus.COMPLETION, null);
        orderRepository.save(order);

        boolean existsOrder = orderRepository
            .existsByTable_IdAndOrderStatusIn(table.getId(), orderStatuses);

        assertThat(existsOrder).isFalse();
    }

    @DisplayName("OrderTableId와 OrderStatus들과 매치되는 Order가 있는지 확인 - False, 매치되는 것 없음")
    @Test
    void existsByTable_IdAndOrderStatusIn_MatchedNothing_ReturnFalse() {
        Table table = getCreatedTableWithTableGroup();
        Table orderTable = getCreatedTableWithTableGroup();
        List<OrderStatus> orderStatuses = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);

        Order order = Order.entityOf(table, OrderStatus.COMPLETION, null);
        orderRepository.save(order);

        boolean existsOrder = orderRepository
            .existsByTable_IdAndOrderStatusIn(orderTable.getId(), orderStatuses);

        assertThat(existsOrder).isFalse();
    }

    @DisplayName("OrderTableId들과 OrderStatus들과 매치되는 Order가 있는지 확인 - True, 모두 매치")
    @Test
    void existsByTableTableGroupAndOrderStatusIn_MatchedOrderTableIdsAndOrderStatus_ReturnTrue() {
        List<Table> tables = Arrays.asList(getCreatedTable(), getCreatedTable());
        TableGroup createdTableGroup = getCreatedTableGroup(tables);
        List<OrderStatus> orderStatuses = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);

        Order order = Order.entityOf(tables.get(0), orderStatuses.get(0), null);
        orderRepository.save(order);

        boolean existsOrder = orderRepository
            .existsByTableTableGroupAndOrderStatusIn(createdTableGroup, orderStatuses);

        assertThat(existsOrder).isTrue();
    }

    @DisplayName("OrderTableId들과 OrderStatus들과 매치되는 Order가 있는지 확인 - False, OrderTableIds만 매치")
    @Test
    void existsByTableTableGroupAndOrderStatusIn_MatchedOrderTableIds_ReturnFalse() {
        List<Table> tables = Arrays.asList(getCreatedTable(), getCreatedTable());
        TableGroup createdTableGroup = getCreatedTableGroup(tables);
        List<OrderStatus> orderStatuses = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);

        Order order = Order.entityOf(tables.get(0), OrderStatus.COMPLETION, null);
        orderRepository.save(order);

        boolean existsOrder = orderRepository
            .existsByTableTableGroupAndOrderStatusIn(createdTableGroup, orderStatuses);

        assertThat(existsOrder).isFalse();
    }

    @DisplayName("OrderTableId들과 OrderStatus들과 매치되는 Order가 있는지 확인 - False, OrderStatuses만 매치")
    @Test
    void existsByTableTableGroupAndOrderStatusIn_MatchedOrderStatuses_ReturnFalse() {
        List<Table> tables = Arrays.asList(getCreatedTable(), getCreatedTable());
        TableGroup createdTableGroup = getCreatedTableGroup(tables);
        List<OrderStatus> orderStatuses = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);

        Order order = Order.entityOf(getCreatedTableWithTableGroup(), orderStatuses.get(0), null);
        orderRepository.save(order);

        boolean existsOrder = orderRepository
            .existsByTableTableGroupAndOrderStatusIn(createdTableGroup, orderStatuses);

        assertThat(existsOrder).isFalse();
    }

    @DisplayName("OrderTableId들과 OrderStatus들과 매치되는 Order가 있는지 확인 - False, 매치되는 것 없음")
    @Test
    void existsByTableTableGroupAndOrderStatusIn_MatchedNothing_ReturnFalse() {
        List<Table> tables = Arrays.asList(getCreatedTable(), getCreatedTable());
        TableGroup createdTableGroup = getCreatedTableGroup(tables);
        List<OrderStatus> orderStatuses = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);

        Order order = Order.entityOf(getCreatedTableWithTableGroup(), OrderStatus.COMPLETION, null);
        orderRepository.save(order);

        boolean existsOrder = orderRepository
            .existsByTableTableGroupAndOrderStatusIn(createdTableGroup, orderStatuses);

        assertThat(existsOrder).isFalse();
    }
}

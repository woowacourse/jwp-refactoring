package kitchenpos.dao;

import static kitchenpos.constants.Constants.TEST_ORDER_ORDERED_TIME;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderRepositoryTest extends KitchenPosDaoTest {

    @DisplayName("Order 저장 - 성공")
    @Test
    void save_Success() {
        Long orderTableId = getCreatedOrderTableId();

        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(TEST_ORDER_ORDERED_TIME);

        Order savedOrder = orderRepository.save(order);

        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderTableId()).isEqualTo(orderTableId);
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(savedOrder.getOrderedTime()).isEqualTo(TEST_ORDER_ORDERED_TIME);
        assertThat(savedOrder.getOrderLineItems()).isNull();
    }

    @DisplayName("Order ID로 Order 조회 - 조회됨, ID가 존재하는 경우")
    @Test
    void findById_ExistsId_ReturnOrder() {
        Order order = new Order();
        order.setOrderTableId(getCreatedOrderTableId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(TEST_ORDER_ORDERED_TIME);
        Order savedOrder = orderRepository.save(order);

        Order foundOrder = orderRepository.findById(savedOrder.getId())
            .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 order가 없습니다."));

        assertThat(foundOrder.getId()).isEqualTo(savedOrder.getId());
        assertThat(foundOrder.getOrderTableId()).isEqualTo(savedOrder.getOrderTableId());
        assertThat(foundOrder.getOrderStatus()).isEqualTo(savedOrder.getOrderStatus());
        assertThat(foundOrder.getOrderedTime()).isEqualTo(savedOrder.getOrderedTime());
        assertThat(foundOrder.getOrderLineItems()).isEqualTo(savedOrder.getOrderLineItems());
    }

    @DisplayName("Order ID로 Order 조회 - 조회되지 않음, ID가 존재하지 않는 경우")
    @Test
    void findById_NotExistsId_ReturnEmpty() {
        Order order = new Order();
        order.setOrderTableId(getCreatedOrderTableId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(TEST_ORDER_ORDERED_TIME);
        Order savedOrder = orderRepository.save(order);

        Optional<Order> foundOrder = orderRepository.findById(savedOrder.getId() + 1);

        assertThat(foundOrder.isPresent()).isFalse();
    }

    @DisplayName("전체 Order 조회 - 성공")
    @Test
    void findAll_Success() {
        Order order = new Order();
        order.setOrderTableId(getCreatedOrderTableId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(TEST_ORDER_ORDERED_TIME);
        Order savedOrder = orderRepository.save(order);

        List<Order> orders = orderRepository.findAll();

        assertThat(orders).isNotNull();
        assertThat(orders).isNotEmpty();

        List<Long> orderIds = orders.stream()
            .map(Order::getId)
            .collect(Collectors.toList());

        assertThat(orderIds).contains(savedOrder.getId());
    }

    @DisplayName("OrderTableId와 OrderStatus들과 매치되는 Order가 있는지 확인 - True, 모두 매치")
    @Test
    void existsByOrderTableIdAndOrderStatusIn_MatchedOrderTableIdAndOrderStatus_ReturnTrue() {
        Long orderTableId = getCreatedOrderTableId();
        List<String> orderStatuses
            = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());

        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatuses.get(0));
        order.setOrderedTime(TEST_ORDER_ORDERED_TIME);
        orderRepository.save(order);

        boolean existsOrder
            = orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, orderStatuses);

        assertThat(existsOrder).isTrue();
    }

    @DisplayName("OrderTableId와 OrderStatus들과 매치되는 Order가 있는지 확인 - False, OrderStatus만 매치")
    @Test
    void existsByOrderTableIdAndOrderStatusIn_MatchedOnlyOrderStatus_ReturnFalse() {
        Long orderTableId = getCreatedOrderTableId();
        List<String> orderStatuses
            = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());

        Order order = new Order();
        order.setOrderTableId(getCreatedOrderTableId());
        order.setOrderStatus(orderStatuses.get(0));
        order.setOrderedTime(TEST_ORDER_ORDERED_TIME);
        orderRepository.save(order);

        boolean existsOrder
            = orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, orderStatuses);

        assertThat(existsOrder).isFalse();
    }

    @DisplayName("OrderTableId와 OrderStatus들과 매치되는 Order가 있는지 확인 - False, OrderTableId만 매치")
    @Test
    void existsByOrderTableIdAndOrderStatusIn_MatchedOnlyOrderTableId_ReturnFalse() {
        Long orderTableId = getCreatedOrderTableId();
        List<String> orderStatuses
            = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());

        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        order.setOrderedTime(TEST_ORDER_ORDERED_TIME);
        orderRepository.save(order);

        boolean existsOrder
            = orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, orderStatuses);

        assertThat(existsOrder).isFalse();
    }

    @DisplayName("OrderTableId와 OrderStatus들과 매치되는 Order가 있는지 확인 - False, 매치되는 것 없음")
    @Test
    void existsByOrderTableIdAndOrderStatusIn_MatchedNothing_ReturnFalse() {
        Long orderTableId = getCreatedOrderTableId();
        List<String> orderStatuses
            = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());

        Order order = new Order();
        order.setOrderTableId(getCreatedOrderTableId());
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        order.setOrderedTime(TEST_ORDER_ORDERED_TIME);
        orderRepository.save(order);

        boolean existsOrder
            = orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, orderStatuses);

        assertThat(existsOrder).isFalse();
    }

    @DisplayName("OrderTableId들과 OrderStatus들과 매치되는 Order가 있는지 확인 - True, 모두 매치")
    @Test
    void existsByOrderTableIdInAndOrderStatusIn_MatchedOrderTableIdsAndOrderStatus_ReturnTrue() {
        List<Long> orderTableIds
            = Arrays.asList(getCreatedOrderTableId(), getCreatedOrderTableId());
        List<String> orderStatuses
            = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());

        Order order = new Order();
        order.setOrderTableId(orderTableIds.get(0));
        order.setOrderStatus(orderStatuses.get(0));
        order.setOrderedTime(TEST_ORDER_ORDERED_TIME);
        orderRepository.save(order);

        boolean existsOrder = orderRepository
            .existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatuses);

        assertThat(existsOrder).isTrue();
    }

    @DisplayName("OrderTableId들과 OrderStatus들과 매치되는 Order가 있는지 확인 - False, OrderTableIds만 매치")
    @Test
    void existsByOrderTableIdInAndOrderStatusIn_MatchedOrderTableIds_ReturnFalse() {
        List<Long> orderTableIds
            = Arrays.asList(getCreatedOrderTableId(), getCreatedOrderTableId());
        List<String> orderStatuses
            = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());

        Order order = new Order();
        order.setOrderTableId(orderTableIds.get(0));
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        order.setOrderedTime(TEST_ORDER_ORDERED_TIME);
        orderRepository.save(order);

        boolean existsOrder = orderRepository
            .existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatuses);

        assertThat(existsOrder).isFalse();
    }

    @DisplayName("OrderTableId들과 OrderStatus들과 매치되는 Order가 있는지 확인 - False, OrderStatuses만 매치")
    @Test
    void existsByOrderTableIdInAndOrderStatusIn_MatchedOrderStatuses_ReturnFalse() {
        List<Long> orderTableIds
            = Arrays.asList(getCreatedOrderTableId(), getCreatedOrderTableId());
        List<String> orderStatuses
            = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());

        Order order = new Order();
        order.setOrderTableId(getCreatedOrderTableId());
        order.setOrderStatus(orderStatuses.get(0));
        order.setOrderedTime(TEST_ORDER_ORDERED_TIME);
        orderRepository.save(order);

        boolean existsOrder = orderRepository
            .existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatuses);

        assertThat(existsOrder).isFalse();
    }

    @DisplayName("OrderTableId들과 OrderStatus들과 매치되는 Order가 있는지 확인 - False, 매치되는 것 없음")
    @Test
    void existsByOrderTableIdInAndOrderStatusIn_MatchedNothing_ReturnFalse() {
        List<Long> orderTableIds
            = Arrays.asList(getCreatedOrderTableId(), getCreatedOrderTableId());
        List<String> orderStatuses
            = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());

        Order order = new Order();
        order.setOrderTableId(getCreatedOrderTableId());
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        order.setOrderedTime(TEST_ORDER_ORDERED_TIME);
        orderRepository.save(order);

        boolean existsOrder = orderRepository
            .existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatuses);

        assertThat(existsOrder).isFalse();
    }
}

package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import support.IntegrationTest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 서비스 테스트")
@IntegrationTest
class OrderServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderDao orderDao; // todo: DAO를 살릴 것인가 말것인가...?

    @Autowired
    private OrderService orderService;

    private OrderTable orderTable;
    private OrderLineItem validOrderLineItem;

    @BeforeEach
    void setUp() {
        orderTable = registerOrderTable(false);

        validOrderLineItem = new OrderLineItem();
        validOrderLineItem.setMenuId(1L);
        validOrderLineItem.setQuantity(1);
    }

    @Nested
    @DisplayName("[주문 추가]")
    class CreateOrder {

        @DisplayName("성공")
        @Test
        void create() {
            //when
            Order actual = registerOrder();

            //then
            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
            assertThat(actual.getOrderTableId()).isEqualTo(orderTable.getId());
            assertThat(actual.getOrderLineItems()).hasSize(1);
        }

        @DisplayName("실패 - 주문 항목이 비어있는 경우")
        @Test
        void createWhenOrderLineItemsIsEmpty() {
            //given
            List<OrderLineItem> emptyOrderLineItems = Collections.emptyList();

            //when //then
            assertThatThrownBy(() -> registerOrder(orderTable.getId(), emptyOrderLineItems))
                    .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("실패 - 주문 항목들 중에 존재하지 않는 주문 항목이 있는 경우")
        @Test
        void createWhenContainsNotExistOrderLineItem() {
            //given
            OrderLineItem invalidOrderLineItem = new OrderLineItem();
            invalidOrderLineItem.setMenuId(100L);
            invalidOrderLineItem.setQuantity(1);
            List<OrderLineItem> invalidOrderLineItems = Collections.singletonList(invalidOrderLineItem);

            //when //then
            assertThatThrownBy(() -> registerOrder(orderTable.getId(), invalidOrderLineItems))
                    .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("실패 - 존재하지 않는 주문 테이블인 경우")
        @Test
        void createWhenNotExistOrderTable() {
            //given
            OrderTable notRegisteredOrderTable = new OrderTable();
            notRegisteredOrderTable.setId(100L);

            Long invalidOrderTableId = notRegisteredOrderTable.getId();

            //when //then
            assertThatThrownBy(() -> registerOrder(invalidOrderTableId))
                    .isExactlyInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("[주문 상태 변경]")
    class ChangeOrderStatus {

        @DisplayName("성공")
        @Test
        void changeOrderStatus() {
            //given
            Order savedOrder = registerOrder();

            Order changedOrder = new Order();
            changedOrder.setOrderStatus(OrderStatus.MEAL.name());

            //when
            Order actual = orderService.changeOrderStatus(savedOrder.getId(), changedOrder);

            //then
            assertThat(actual.getId()).isEqualTo(savedOrder.getId());
            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
            assertThat(actual.getOrderTableId()).isEqualTo(orderTable.getId());
            assertThat(actual.getOrderLineItems()).hasSize(1);
        }

        @DisplayName("실패 - 이미 '계산 완료'된 주문인 경우")
        @Test
        void changeOrderStatusWhenAlreadyOrderStatusIsCOMPLETION() {
            //given
            Order completionOrder = registerOrder();
            completionOrder.setOrderStatus(OrderStatus.COMPLETION.name());
            orderDao.save(completionOrder);

            Order mealOrder = new Order();
            mealOrder.setOrderStatus(OrderStatus.MEAL.name());

            //when //then
            assertThatThrownBy(() -> orderService.changeOrderStatus(completionOrder.getId(), mealOrder))
                    .isExactlyInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("[주문 조회] 성공")
    @Test
    void list() {
        //given
        Order order = registerOrder();

        //when
        List<Order> actual = orderService.list();

        //then
        assertThat(actual).hasSize(1);
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrder(order);
    }

    private Order registerOrder(Long orderTableId, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);

        return orderService.create(order);
    }

    private Order registerOrder(Long invalidOrderTableId) {
        return registerOrder(invalidOrderTableId, Collections.singletonList(validOrderLineItem));
    }

    private Order registerOrder() {
        return registerOrder(orderTable.getId());
    }

    public OrderTable registerOrderTable(Long id, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setEmpty(empty);
        orderTable.setNumberOfGuests(0);

        return tableService.create(orderTable);
    }

    private OrderTable registerOrderTable(boolean empty) {
        return registerOrderTable(null, empty);
    }
}

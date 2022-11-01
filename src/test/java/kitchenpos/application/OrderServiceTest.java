package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderCreateRequest;
import kitchenpos.application.dto.OrderLineItemCreateRequest;
import kitchenpos.application.dto.OrderUpdateRequest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("OrderService의")
class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private MenuDao menuDao;
    @MockBean
    private OrderTableDao orderTableDao;

    @Nested
    @DisplayName("create 메서드는")
    class Create {
        private static final long ORDER_ID = 1L;
        private static final long MENU_A_ID = 1L;
        private static final long MENU_B_ID = 2L;
        private static final long ORDER_TABLE_ID = 1L;

        private OrderLineItemCreateRequest orderLineItemA;
        private OrderLineItemCreateRequest orderLineItemB;
        private OrderTable orderTable;
        private OrderCreateRequest request;

        @BeforeEach
        void setUp() {
            orderLineItemA = new OrderLineItemCreateRequest(MENU_A_ID, 10L);
            orderLineItemB = new OrderLineItemCreateRequest(MENU_B_ID, 20L);

            orderTable = new OrderTable(ORDER_TABLE_ID, null, 2, false);

            request = new OrderCreateRequest(ORDER_TABLE_ID, Arrays.asList(orderLineItemA, orderLineItemB));

            given(menuDao.countByIdIn(any()))
                    .willReturn((long) request.getOrderLineItems().size());
            given(orderTableDao.findById(request.getOrderTableId()))
                    .willReturn(Optional.of(orderTable));
        }

        @Test
        @DisplayName("등록할 수 있는 주문을 받으면, 저장하고 내용을 반환한다.")
        void success() {
            //when
            Order actual = orderService.create(request);

            //then
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getOrderLineItems()).hasSize(2),
                    () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING)
            );
        }

        @Test
        @DisplayName("주문 아이템이 비어있으면, 예외를 던진다.")
        void fail_noOrderLineItem() {
            //given
            request = new OrderCreateRequest(ORDER_TABLE_ID, Collections.emptyList());

            //when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("동일한 주문 아이템을 입력하면, 예외를 던진다.")
        void fail_sameOrderLineItem() {
            //given
            request = new OrderCreateRequest(ORDER_TABLE_ID, Arrays.asList(orderLineItemA, orderLineItemA));
            Set<Long> distinctMenus = request.getOrderLineItems().stream()
                    .map(OrderLineItem::getMenuId)
                    .collect(Collectors.toSet());

            given(menuDao.countByIdIn(any()))
                    .willReturn((long) distinctMenus.size());

            //when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문한 테이블의 상태가 Empty이면, 예외를 던진다.")
        void fail_orderTableIsEmpty() {
            //given
            orderTable.updateEmpty(true);

            //when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("changeOrderStatus 메서드는")
    class changeOrderStatus {

        private static final long ORDER_ID = 1L;
        private static final long MENU_A_ID = 1L;
        private static final long MENU_B_ID = 2L;
        private static final long ORDER_TABLE_ID = 1L;

        private OrderLineItemCreateRequest orderLineItemA;
        private OrderLineItemCreateRequest orderLineItemB;
        private OrderTable orderTable;
        private Order savedOrder;
        private OrderCreateRequest createRequest;
        private OrderUpdateRequest request;

        @BeforeEach
        void setUp() {
            orderLineItemA = new OrderLineItemCreateRequest(MENU_A_ID, 10L);
            orderLineItemB = new OrderLineItemCreateRequest(MENU_B_ID, 20L);

            orderTable = new OrderTable(ORDER_TABLE_ID, null, 2, false);

            createRequest = new OrderCreateRequest(ORDER_TABLE_ID, Arrays.asList(orderLineItemA, orderLineItemB));
            given(orderTableDao.findById(any()))
                    .willReturn(Optional.of(orderTable));
            given(menuDao.countByIdIn(any()))
                    .willReturn(2L);

            savedOrder = orderService.create(createRequest);
            request = new OrderUpdateRequest(OrderStatus.MEAL.name());
        }

        @Test
        @DisplayName("주문의 상태를 변경할 수 있다.")
        void success() {
            //when
            Order actual = orderService.changeOrderStatus(savedOrder.getId(), request);

            //then
            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
        }

        @Test
        @DisplayName("주문이 존재하지 않으면, 예외를 던진다.")
        void fail_noExistOrder() {
            //given

            //when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(-1L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문이 완료상태이면, 예외를 던진다.")
        void fail_completedOrder() {
            //given
            orderService.changeOrderStatus(savedOrder.getId(), new OrderUpdateRequest(OrderStatus.COMPLETION.name()));

            //when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}

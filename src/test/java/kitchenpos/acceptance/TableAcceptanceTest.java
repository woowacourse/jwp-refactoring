package kitchenpos.acceptance;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.ui.request.OrderTableEmptyRequest;
import kitchenpos.table.ui.request.OrderTableNumberOfGuestRequest;
import kitchenpos.table.ui.request.OrderTableRequest;
import kitchenpos.table.ui.response.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블 관련 기능")
class TableAcceptanceTest extends AcceptanceTest {

    private OrderTable 테이블_그룹_없음_주문_테이블1;
    private OrderTable 테이블_그룹_없음_주문_테이블2;

    private TableGroup 테이블_그룹1;
    private OrderTable 테이블_그룹1_소속_주문_테이블3;
    private OrderTable 테이블_그룹1_소속_주문_테이블4;

    private Order 주문_테이블1_요리중인_주문;
    private Order 주문_테이블2_식사중인_주문;

    private OrderLineItem 주문_테이블1_한마리메뉴_중_후라이트치킨;
    private OrderLineItem 주문_테이블2_한마리메뉴_중_후라이트치킨;

    @BeforeEach
    void setUp() {
        super.setUp();

        테이블_그룹_없음_주문_테이블1 = new OrderTable.Builder()
                .numberOfGuests(4)
                .empty(false)
                .build();

        테이블_그룹_없음_주문_테이블2 = new OrderTable.Builder()
                .numberOfGuests(2)
                .empty(false)
                .build();

        테이블_그룹1_소속_주문_테이블3 = new OrderTable.Builder()
                .numberOfGuests(3)
                .empty(true)
                .build();

        테이블_그룹1_소속_주문_테이블4 = new OrderTable.Builder()
                .numberOfGuests(5)
                .empty(true)
                .build();

        OrderTables 주문_테이블들 = new OrderTables(Arrays.asList(테이블_그룹1_소속_주문_테이블3, 테이블_그룹1_소속_주문_테이블4));

        테이블_그룹1 = new TableGroup.Builder()
                .createdDate(LocalDateTime.now())
                .build();

        주문_테이블들.registerTableGroup(테이블_그룹1);

        tableGroupRepository.save(테이블_그룹1);
        orderTableRepository.save(테이블_그룹_없음_주문_테이블1);
        orderTableRepository.save(테이블_그룹_없음_주문_테이블2);
        orderTableRepository.saveAll(주문_테이블들.getOrderTables());

        주문_테이블1_한마리메뉴_중_후라이트치킨  = new OrderLineItem.Builder()
                .menu(한마리메뉴_중_후라이드치킨)
                .order(주문_테이블1_요리중인_주문)
                .quantity(1L)
                .build();

        주문_테이블1_요리중인_주문 = new Order.Builder()
                .orderTable(테이블_그룹_없음_주문_테이블1)
                .orderStatus(OrderStatus.COOKING)
                .orderedTime(LocalDateTime.now())
                .orderLineItems(Arrays.asList(주문_테이블1_한마리메뉴_중_후라이트치킨))
                .build();
        orderRepository.save(주문_테이블1_요리중인_주문);
        orderLineItemRepository.save(주문_테이블1_한마리메뉴_중_후라이트치킨);

        주문_테이블2_한마리메뉴_중_후라이트치킨  = new OrderLineItem.Builder()
                .menu(한마리메뉴_중_후라이드치킨)
                .order(주문_테이블2_식사중인_주문)
                .quantity(1L)
                .build();

        주문_테이블2_식사중인_주문 = new Order.Builder()
                .orderTable(테이블_그룹_없음_주문_테이블2)
                .orderStatus(OrderStatus.COOKING)
                .orderedTime(LocalDateTime.now())
                .orderLineItems(Arrays.asList(주문_테이블2_한마리메뉴_중_후라이트치킨))
                .build();
        주문_테이블2_식사중인_주문.changeOrderStatus(OrderStatus.MEAL);
        orderRepository.save(주문_테이블2_식사중인_주문);
        orderLineItemRepository.save(주문_테이블2_한마리메뉴_중_후라이트치킨);
    }

    @DisplayName("매장에서 주문이 발생하는 테이블들에 대한 정보를 반환한다")
    @Test
    void getOrders() {
        // when
        ResponseEntity<OrderTableResponse[]> response = testRestTemplate.getForEntity("/api/tables", OrderTableResponse[].class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(4);
    }

    @DisplayName("매장에서 주문이 발생하는 테이블에 대한 정보를 추가한다")
    @Test
    void createOrder() {
        // given
        OrderTableRequest 주문_테이블3_요청 = new OrderTableRequest();
        주문_테이블3_요청.setNumberOfGuests(0);
        주문_테이블3_요청.setEmpty(true);

        // when
        ResponseEntity<OrderTableResponse> response = testRestTemplate.postForEntity("/api/tables", 주문_테이블3_요청, OrderTableResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        OrderTableResponse 응답된_주문_테이블 = response.getBody();
        assertThat(응답된_주문_테이블.getNumberOfGuests()).isZero();
        assertThat(응답된_주문_테이블.getEmpty()).isTrue();
    }

    @DisplayName("매장에서 주문이 발생하는 테이블 중 tableId에 해당하는 테이블의 empty 여부를 변경한다")
    @Test
    void changeEmptyStatus() {
        // given
        OrderTableEmptyRequest 변경할_주문_테이블_EMPTY_요청 = new OrderTableEmptyRequest();
        변경할_주문_테이블_EMPTY_요청.setEmpty(true);
        Long 주문_테이블1_ID = 테이블_그룹_없음_주문_테이블1.getId();
        주문_테이블1_요리중인_주문.changeOrderStatus(OrderStatus.COMPLETION);
        orderRepository.save(주문_테이블1_요리중인_주문);

        // when
        testRestTemplate.put("/api/tables/" + 주문_테이블1_ID + "/empty", 변경할_주문_테이블_EMPTY_요청);

        // then
        OrderTable 변경된_주문_테이블1 = orderTableRepository.findById(주문_테이블1_ID).get();
        assertThat(변경된_주문_테이블1.getId()).isEqualTo(주문_테이블1_ID);
        assertThat(변경된_주문_테이블1.isEmpty()).isTrue();
    }

    @DisplayName("매장에서 주문이 발생하는 테이블 중 tableId에 해당하는 테이블의 empty 여부를 변경시, 해당 테이블의 주문 상태가 모두 COMPLETION 아니라면 변경할 수 없다")
    @Test
    void cannotChangeEmptyStatusWhenOrderStatusNotCompletion() {
        // given
        OrderTableEmptyRequest 변경할_주문_테이블_EMPTY_요청 = new OrderTableEmptyRequest();
        변경할_주문_테이블_EMPTY_요청.setEmpty(true);
        Long 주문_테이블2_ID = 테이블_그룹_없음_주문_테이블2.getId();

        // when
        ResponseEntity<Void> response = testRestTemplate.exchange("/api/tables/" + 주문_테이블2_ID + "/empty",
                HttpMethod.PUT, new HttpEntity<>(변경할_주문_테이블_EMPTY_요청), Void.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("매장에서 주문이 발생하는 테이블 중 tableId에 해당하는 테이블의 empty 여부를 변경시, 해당 테이블이 TableGroup이 있다면 변경할 수 없다.")
    @Test
    void cannotChangeEmptyStatusWhenTableGroupIsNotNull() {
        // given
        OrderTableEmptyRequest 변경할_주문_테이블_EMPTY_요청 = new OrderTableEmptyRequest();
        변경할_주문_테이블_EMPTY_요청.setEmpty(true);
        Long 주문_테이블3_ID = 테이블_그룹1_소속_주문_테이블3.getId();

        // when
        ResponseEntity<Void> response = testRestTemplate.exchange("/api/tables/" + 주문_테이블3_ID + "/empty",
                HttpMethod.PUT, new HttpEntity<>(변경할_주문_테이블_EMPTY_요청), Void.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("매장에서 주문이 발생하는 테이블 중 tableId에 해당하는 테이블의 number-of-guest를 변경한다")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTableNumberOfGuestRequest 변경할_주문_테이블_손님_요청 = new OrderTableNumberOfGuestRequest();
        변경할_주문_테이블_손님_요청.setNumberOfGuests(100);
        Long 주문_테이블1_ID = 테이블_그룹_없음_주문_테이블1.getId();

        // when
        testRestTemplate.put("/api/tables/" + 주문_테이블1_ID + "/number-of-guests", 변경할_주문_테이블_손님_요청);

        // then
        OrderTable 변경된_주문_테이블1 = orderTableRepository.findById(주문_테이블1_ID).get();
        assertThat(변경된_주문_테이블1.getId()).isEqualTo(주문_테이블1_ID);
        assertThat(변경된_주문_테이블1.getNumberOfGuests()).isEqualTo(100);
    }

    @DisplayName("매장에서 주문이 발생하는 테이블 중 tableId에 해당하는 테이블의 number-of-guest를 변경시, 음수이면 안된다")
    @Test
    void cannotChangeNumberOfGuestsToNegative() {
        // given
        OrderTableNumberOfGuestRequest 변경할_주문_테이블_손님_요청 = new OrderTableNumberOfGuestRequest();
        변경할_주문_테이블_손님_요청.setNumberOfGuests(-1000);
        Long 주문_테이블1_ID = 테이블_그룹_없음_주문_테이블1.getId();

        // when
        ResponseEntity<Void> response = testRestTemplate.exchange("/api/tables/" + 주문_테이블1_ID + "/number-of-guests",
                HttpMethod.PUT, new HttpEntity<>(변경할_주문_테이블_손님_요청), Void.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("매장에서 주문이 발생하는 테이블 중 tableId에 해당하는 테이블의 number-of-guest를 변경시, 이미 비워진 테이블이라면 안된다")
    @Test
    void cannotChangeNumberOfGuestsIfEmpty() {
        // given
        OrderTable 비워진_주문_테이블 = new OrderTable.Builder()
                .numberOfGuests(4)
                .empty(true)
                .build();
        비워진_주문_테이블 = orderTableRepository.save(비워진_주문_테이블);

        OrderTableNumberOfGuestRequest 변경할_주문_테이블_손님_요청 = new OrderTableNumberOfGuestRequest();
        변경할_주문_테이블_손님_요청.setNumberOfGuests(100);
        Long 비워진_주문_테이블_ID = 비워진_주문_테이블.getId();

        // when
        ResponseEntity<Void> response = testRestTemplate.exchange("/api/tables/" + 비워진_주문_테이블_ID + "/number-of-guests",
                HttpMethod.PUT, new HttpEntity<>(변경할_주문_테이블_손님_요청), Void.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

package kitchenpos.acceptance;

import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuDto;
import kitchenpos.application.dto.OrderDto;
import kitchenpos.application.dto.OrderLineItemDto;
import kitchenpos.application.dto.TableDto;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class OrdersAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("신규 주문을 생성할 수 있다")
    void createOrder() {
        final Order order = Order.create(손님있는_테이블.getId(),
                List.of(new OrderLineItem(파스타한상.getName(), 파스타한상.getPrice(), 파스타한상.getId(), 1L)));

        final ExtractableResponse<Response> response = 메뉴_주문_요청(order);
        final OrderDto responseBody = response.body().as(OrderDto.class);
        final List<OrderLineItemDto> orderLineItems = responseBody.getOrderLineItems();

        assertAll(
                () -> 응답_코드_일치_검증(response, HttpStatus.CREATED),
                () -> 단일_데이터_검증(responseBody.getOrderTableId(), 손님있는_테이블.getId()),
                () -> 단일_데이터_검증(responseBody.getOrderStatus(), OrderStatus.COOKING.name()),
                () -> 리스트_데이터_검증(orderLineItems, "menuId", 파스타한상.getId()),
                () -> 리스트_데이터_검증(orderLineItems, "orderId", responseBody.getId()),
                () -> 리스트_데이터_검증(orderLineItems, "quantity", 1L)
        );
    }

    @Test
    @DisplayName("전체 주문을 조회할 수 있다")
    void getOrders() {
        final MenuDto newMenu = 메뉴_등록("파스타세트", 16000L, 세트.getId(), 토마토파스타.getId(), 탄산음료.getId());
        final TableDto table1 = 손님_채운_테이블_생성(2);
        final TableDto table2 = 손님_채운_테이블_생성(2);
        final OrderDto order1 = 메뉴_주문(table1.getId(), 파스타한상.getId());
        final OrderDto order2 = 메뉴_주문(table2.getId(), newMenu.getId());

        final ExtractableResponse<Response> response = 모든_주문_조회_요청();
        final List<OrderDto> responseBody = response.body()
                .jsonPath()
                .getList(".", OrderDto.class);

        final List<OrderLineItemDto> orderLineItems = responseBody.stream()
                .flatMap(order -> order.getOrderLineItems().stream())
                .collect(Collectors.toList());

        assertAll(
                () -> 응답_코드_일치_검증(response, HttpStatus.OK),
                () -> 리스트_데이터_검증(responseBody, "id", order1.getId(), order2.getId()),
                () -> 리스트_데이터_검증(responseBody, "orderTableId", order1.getOrderTableId(), order2.getOrderTableId()),
                () -> 리스트_데이터_검증(orderLineItems, "menuId", 파스타한상.getId(), newMenu.getId())
        );
    }

    @Test
    @DisplayName("주문의 상태를 변경할 수 있다")
    void changeOrderStatus() {
        final OrderDto order = 메뉴_주문(손님있는_테이블.getId(), 파스타한상.getId());

        // when
        final ExtractableResponse<Response> response = 주문_상태_변경_요청(order.getId(), OrderStatus.MEAL.name());
        final OrderDto responseBody = response.as(OrderDto.class);

        // then
        assertAll(
                () -> 응답_코드_일치_검증(response, HttpStatus.OK),
                () -> 단일_데이터_검증(responseBody.getOrderStatus(), OrderStatus.MEAL.name())
        );
    }
}

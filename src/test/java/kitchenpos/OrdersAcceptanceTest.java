package kitchenpos;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@DisplayName("주문 기능")
@Sql({"classpath:tableInit.sql", "classpath:dataInsert.sql"})
public class OrdersAcceptanceTest extends ApplicationTest {

    private final Order 기본_주문 = new Order();
    private final OrderLineItem 기본_주문_메뉴 = new OrderLineItem();

    @BeforeEach
    public void setUp() {
        super.setUp();
        기본_주문_메뉴.setMenuId(1L);
        기본_주문_메뉴.setQuantity(1L);

        기본_주문.setOrderTableId(1L);
        기본_주문.setOrderLineItems(Collections.singletonList(기본_주문_메뉴));
    }

    @DisplayName("주문 등록에 성공하면 201 응답을 받는다.")
    @Test
    void createOrder() {
        //given
        주문_테이블_채우기();

        //when
        ExtractableResponse<Response> response = 주문_추가(기본_주문);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 주문_테이블_채우기() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        TableAcceptanceTest.주문_테이블_비우거나_채우기(orderTable, 1L);
    }

    @DisplayName("잘못된")
    @Nested
    class FailTest {

        @DisplayName("메뉴id로 주문을 등록하려고하면 500 응답을 받는다.")
        @Test
        void fail1() {
            //given
            기본_주문_메뉴.setMenuId(1000L);

            주문_테이블_채우기();

            //when
            ExtractableResponse<Response> response = 주문_추가(기본_주문);

            //then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        @DisplayName("주문 테이블id로 주문을 등록하려고하면 500 응답을 받는다.")
        @Test
        void fail2() {
            //given
            기본_주문.setOrderTableId(1000L);

            주문_테이블_채우기();

            //when
            ExtractableResponse<Response> response = 주문_추가(기본_주문);

            //then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        @DisplayName("주문아이디로 주문 상태를 변경하려고하면 500응답을 받는다.")
        @Test
        void changeOrderStatus(){
            주문_테이블_채우기();
            주문_추가(기본_주문);

            Order order = new Order();
            order.setOrderStatus("MEAL");

            ExtractableResponse<Response> response = 주문_상태_변경(1000L, order);

            //then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @DisplayName("빈 테이블에 주문을 등록하려고하면 500 응답을 받는다.")
    @Test
    void fail3() {
        //when
        ExtractableResponse<Response> response = 주문_추가(기본_주문);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


    @DisplayName("전체 주문을 불러오는데 성공하면, 200 응답을 받는다.")
    @Test
    void getOrders() {

        //given
        주문_테이블_채우기();
        주문_추가(기본_주문);

        //when
        ExtractableResponse<Response> response = 전체_주문_조회();
        List<Order> orders = response.jsonPath().getList(".", Order.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(orders).hasSize(1);
    }

    @DisplayName("주문 상태를 변경하는데 성공하면, 200 응답을 받는다.")
    @Test
    void changeOrderStatus(){
        주문_테이블_채우기();
        주문_추가(기본_주문);

        Order order = new Order();
        order.setOrderStatus("MEAL");

        ExtractableResponse<Response> response = 주문_상태_변경(1L, order);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("COMPLETION 주문상태를 변경하려고하면, 500 응답을 받는다.")
    @Test
    void completionChangeOrderStatus(){
        주문_테이블_채우기();
        주문_추가(기본_주문);

        Order order = new Order();
        order.setOrderStatus("COMPLETION");
        주문_상태_변경(1L, order);

        order = new Order();
        order.setOrderStatus("MEAL");
        ExtractableResponse<Response> response = 주문_상태_변경(1L, order);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private ExtractableResponse<Response> 주문_상태_변경(final Long orderId, final Order order) {
        return RestAssured
            .given().log().all()
            .body(order)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .put(String.format("/api/orders/%s/order-status", orderId))
            .then().log().all()
            .extract();
    }


    private ExtractableResponse<Response> 주문_추가(final Order order) {
        return RestAssured
            .given().log().all()
            .body(order)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .post("/api/orders")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 전체_주문_조회() {
        return RestAssured
            .given().log().all()
            .when().get("/api/orders")
            .then().log().all()
            .extract();
    }
}

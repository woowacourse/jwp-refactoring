package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.request.OrderStatusChangeRequest;
import kitchenpos.application.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.domain.OrderStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/truncate_insert_data.sql")
class OrderRestControllerTest {

    @Autowired
    OrderRestController orderRestController;

    @DisplayName("POST {{host}}/api/orders fail")
    @Test
    void order_create_fail() {
        //given
        final OrderCreateRequest request = RequestFixture.orderCreateRequest();

        //when
        //then
        assertThatThrownBy(() -> orderRestController.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블에는 주문을 넣을 수 없습니다.");
    }

    @DisplayName("POST {{host}}/api/orders")
    @Test
    void order_create(@Autowired TableRestController tableRestController) {
        //given

        //when
        final ResponseEntity<OrderResponse> response = orderCreate(tableRestController);

        //then
        SoftAssertions.assertSoftly(
                soft -> {
                    soft.assertThat(response.getBody().getId()).isNotNull();
                    soft.assertThat(response.getBody().getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
                    soft.assertThat(response.getBody().getOrderLineItems().get(0).getSeq()).isNotNull();
                }
        );
    }

    private void changeTableEmptyFalse(final long orderTableId, final TableRestController tableRestController) {
        final OrderTableChangeEmptyRequest changeEmptyRequestFalse = RequestFixture.orderTableChangeEmptyRequest_false();
        tableRestController.changeEmpty(orderTableId, changeEmptyRequestFalse);
    }

    private ResponseEntity<OrderResponse> orderCreate(
            final TableRestController tableRestController) {
        //given
        final OrderCreateRequest request = RequestFixture.orderCreateRequest();
        changeTableEmptyFalse(request.getOrderTableId(), tableRestController);
        //when
        return orderRestController.create(request);
    }

    @DisplayName("GET {{host}}/api/orders")
    @Test
    void order_list() {
        //given

        //when
        final ResponseEntity<List<OrderResponse>> response = orderRestController.list();
        //then
        SoftAssertions.assertSoftly(
                soft -> {
                    soft.assertThat(response.getBody().size()).isEqualTo(0);
                }
        );
    }

    @DisplayName("PUT {{host}}/api/orders/1/order-status fail")
    @Test
    void order_changeOrderStatus_fail() {
        //given
        final OrderStatusChangeRequest request = RequestFixture.orderStatusChangeRequest_MEAL();

        //when
        //then
        assertThatThrownBy(() -> orderRestController.changeOrderStatus(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문입니다.");
    }

    @DisplayName("PUT {{host}}/api/orders/1/order-status MEAL")
    @Test
    void order_changeOrderStatus_MEAL(@Autowired TableRestController tableRestController) {
        //given
        final OrderResponse orderResponse = orderCreate(tableRestController).getBody();
        final OrderStatusChangeRequest request = RequestFixture.orderStatusChangeRequest_MEAL();

        //when
        final ResponseEntity<OrderResponse> response = orderRestController.changeOrderStatus(orderResponse.getId(),
                request);

        //then
        assertThat(response.getBody().getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("PUT {{host}}/api/orders/1/order-status COMPLETION")
    @Test
    void order_changeOrderStatus_COMPLETION(@Autowired TableRestController tableRestController) {
        //given
        final OrderResponse orderResponse = orderCreate(tableRestController).getBody();
        final OrderStatusChangeRequest request = RequestFixture.orderStatusChangeRequest_COMPLETION();

        //when
        final ResponseEntity<OrderResponse> response = orderRestController.changeOrderStatus(orderResponse.getId(),
                request);

        //then
        assertThat(response.getBody().getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }
}

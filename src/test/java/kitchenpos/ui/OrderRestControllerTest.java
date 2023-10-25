package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

import java.util.List;
import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.application.support.RequestFixture;
import kitchenpos.domain.OrderStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
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
        final OrderTableChangeEmptyRequest changeEmptyRequestFalse = RequestFixture.orderTableChangeEmptyRequest_false();
        final OrderCreateRequest request = RequestFixture.orderCreateRequest();

        tableRestController.changeEmpty(request.getOrderTableId(), changeEmptyRequestFalse);
        //when
        final ResponseEntity<OrderResponse> response = orderRestController.create(request);

        //then
        SoftAssertions.assertSoftly(
                soft -> {
                    soft.assertThat(response.getBody().getId()).isNotNull();
                    soft.assertThat(response.getBody().getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
                }
        );
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
}

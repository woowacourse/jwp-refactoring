package kitchenpos.acceptance;

import static kitchenpos.adapter.presentation.web.OrderRestController.*;
import static kitchenpos.fixture.RequestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import kitchenpos.application.dto.OrderResponse;

public class OrderAcceptanceTest extends AcceptanceTest {
    /**
     * 주문을 관리한다.
     * <p>
     * When 주문 생성 요청.
     * Then 주문이 생성 된다.
     * <p>
     * Given 주문이 생성 되어 있다.
     * When 주문 전체 조회 요청.
     * Then 전체 주문을 반환한다.
     * <p>
     * When 주문 상태 변경 요청.
     * Then 변경된 주문 정보를 반환한다.
     */
    @DisplayName("주문 관리")
    @TestFactory
    Stream<DynamicTest> manageOrder() throws Exception {
        // 주문 생성
        Long orderId = createOrder();
        assertThat(orderId).isNotNull();

        return Stream.of(
                dynamicTest("주문 전체 조회", () -> {
                    List<OrderResponse> orders = getAll(OrderResponse.class, API_ORDERS);
                    OrderResponse lastOrder = getLastItem(orders);

                    assertThat(lastOrder.getId()).isEqualTo(orderId);
                }),
                dynamicTest("주문 상태 변경", () -> {
                    OrderResponse response = changeOrderStatus(orderId);
                    assertAll(
                            () -> assertThat(response.getId()).isEqualTo(orderId),
                            () -> assertThat(response.getOrderStatus()).isEqualTo(
                                    ORDER_STATUS_CHANGE_REQUEST1.getOrderStatus())
                    );
                })
        );
    }

    private Long createOrder() throws Exception {
        changeOrderTableEmpty(false, ORDER_CREATE_REQUEST.getOrderTableId());

        String request = objectMapper.writeValueAsString(ORDER_CREATE_REQUEST);
        return post(request, API_ORDERS);
    }

    private OrderResponse changeOrderStatus(Long orderId) throws Exception {
        String request = objectMapper.writeValueAsString(ORDER_STATUS_CHANGE_REQUEST1);
        return put(OrderResponse.class, request, API_ORDERS + "/" + orderId + "/order-status");
    }
}

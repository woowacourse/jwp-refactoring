package acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("주문 인수테스트에서")
public class OrderAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("주문을 조회한다.")
    void getOrder() {
        주문을_생성한다(테이블(), 주문항목());
        주문을_생성한다(테이블(), 주문항목());
        주문을_생성한다(테이블(), 주문항목());

        List<OrderResponse> responses = 주문_목목을_조회한다();

        assertThat(responses).hasSize(3);
    }

    @ParameterizedTest(name = "{0}으로 변경한다.")
    @DisplayName("주문 상태를 변경한다.")
    @ValueSource(strings = {"MEAL", "COMPLETION"})
    void changeStatus(String orderStatus) {
        long 주문 = 주문을_생성한다(테이블(), 주문항목());

        OrderResponse response = 주문_상태를_변경한다(주문, orderStatus);

        assertThat(response.getOrderStatus()).isEqualTo(orderStatus);
    }

    private long 테이블() {
        return 테이블을_생성한다(2, false);
    }

    private List<OrderLineItem> 주문항목() {
        long 후라이드 = 상품을_생성한다("후라이드", 16000);
        long 양념치킨 = 상품을_생성한다("양념치킨", 18000);

        long 두마리_메뉴 = 메뉴_그룹을_생성한다("두마리 메뉴");

        long 후라이드_두마리 = 메뉴를_생성한다("후라이드+후라이드", 30000, 두마리_메뉴, List.of(후라이드), 2);
        long 양념치킨_두마리 = 메뉴를_생성한다("양념치킨+양념치킨", 34000, 두마리_메뉴, List.of(양념치킨), 2);

        OrderLineItem orderLineItem1 = createOrderLineItem(후라이드_두마리);
        OrderLineItem orderLineItem2 = createOrderLineItem(양념치킨_두마리);

        return List.of(orderLineItem1, orderLineItem2);
    }

    private static OrderLineItem createOrderLineItem(long orderLineItemId) {
        OrderLineItem orderLineItem1 = new OrderLineItem();
        return orderLineItem1;
    }
}

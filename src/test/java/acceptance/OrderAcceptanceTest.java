package acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("주문을 조회한다.")
    void getOrder() {
        // given
        long 테이블 = 테이블(0, false);
        List<OrderLineItem> 주문항목 = 주문항목();
        long 주문 = 주문_생성(테이블, 주문항목);

        // when
        List<Order> orders = 주문_목목조회();

        // then
        assertThat(orders)
                .extracting(Order::getId, Order::getOrderTableId, Order::getOrderStatus)
                .containsExactlyInAnyOrder(
                        tuple(주문, 테이블, "COOKING")
                );
    }

    private long 테이블(int numberOfGuests, boolean empty) {
        return 테이블_생성(numberOfGuests, empty);
    }

    private List<OrderLineItem> 주문항목() {
        long 후라이드 = 상품_생성("후라이드", 16000);
        long 양념치킨 = 상품_생성("양념치킨", 18000);

        long 두마리_메뉴 = 메뉴_그룹_생성("두마리 메뉴");

        long 후라이드_두마리_메뉴 = 메뉴_생성("후라이드+후라이드", 30000, 두마리_메뉴, List.of(후라이드), 2);
        long 양념치킨_두마리_메뉴 = 메뉴_생성("양념치킨+양념치킨", 34000, 두마리_메뉴, List.of(양념치킨), 2);

        OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setMenuId(후라이드_두마리_메뉴);
        orderLineItem1.setQuantity(1);

        OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setMenuId(양념치킨_두마리_메뉴);
        orderLineItem2.setQuantity(1);

        return List.of(orderLineItem1, orderLineItem2);
    }
}

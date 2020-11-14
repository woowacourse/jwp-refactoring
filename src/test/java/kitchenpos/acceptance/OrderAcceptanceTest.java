package kitchenpos.acceptance;

import static kitchenpos.ui.OrderRestController.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.util.Lists.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

@DisplayName("주문 인수 테스트")
class OrderAcceptanceTest extends AcceptanceTest {
    /*
     * Feature: 주문 관리
     *
     * Scenario: 주문을 관리한다.
     *
     * Given: 메뉴 그룹이 등록되어 있다.
     *        상품이 등록되어 있다.
     *        메뉴가 등록되어 있다.
     *        주문 테이블이 등록되어 있다.
     *
     * When: 주문을 등록한다.
     * Then: 주문이 등록된다.
     *
     * Given: 주문이 등록되어 있다.
     * When: 주문의 목록을 조회한다.
     * Then: 주문의 목록이 조회된다.
     *
     * Given: 주문이 등록되어 있다.
     * When: 주문의 상태를 변경한다.
     * Then: 주문의 상태가 변경된다.
     */
    @DisplayName("주문 관리")
    @TestFactory
    Stream<DynamicTest> manageOrder() throws JsonProcessingException {
        // Given
        final Map<Product, Long> products = new HashMap<>();
        products.put(createProduct("마늘치킨", "18000"), 1L);
        products.put(createProduct("파닭치킨", "18000"), 1L);
        final MenuGroup menuGroup = createMenuGroup("세마리 메뉴");
        final Menu menu = createMenu("마늘파닭치킨", "35000", menuGroup.getId(), products);
        final OrderTable orderTable = createOrderTable(4, false);

        return Stream.of(
                dynamicTest(
                        "주문을 등록한다",
                        () -> {
                            // When
                            final OrderLineItem orderLineItem = new OrderLineItem();
                            orderLineItem.setMenuId(menu.getId());
                            orderLineItem.setQuantity(2L);

                            final Order order = new Order();
                            order.setOrderTableId(orderTable.getId());
                            order.setOrderLineItems(newArrayList(orderLineItem));

                            final Order createdOrder = create(ORDER_REST_API_URI, order,
                                    Order.class);

                            // Then
                            assertThat(createdOrder)
                                    .extracting(Order::getId)
                                    .isNotNull()
                            ;
                        }
                )
        );
    }
}

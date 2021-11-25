package kitchenpos.application;

import java.util.ArrayList;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import org.assertj.core.api.ThrowableAssert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class OrderServiceTest extends ServiceTest {

    private final OrderRequest cookingOrder;

    public OrderServiceTest() {
        this.cookingOrder = Fixtures.makeOrder();
    }

    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("주문 생성")
    void createTest() {

        // when
        final Order savedOrder = orderService.create(cookingOrder);

        // then
        assertThat(orderService.list()).contains(savedOrder);
    }

    @Test
    @DisplayName("주문 생성 실패 - 주문 항목이 비어있음")
    void createFailIfRequestIsEmptyTest() {

        // given
        final OrderRequest orderRequest = new OrderRequest(1L, new ArrayList<>());

        // when
        ThrowableAssert.ThrowingCallable callable = () -> orderService.create(orderRequest);

        // then
        assertThatIllegalArgumentException().isThrownBy(callable)
                                            .withMessage("주문 항목이 비어있습니다.");
    }

    @Test
    @DisplayName("주문 생성 실패 - 저장되어 있는 메뉴보다 더 많은 메뉴가 입력")
    void createFailInputIsMoreThanSaveTest() {

        // given
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(10L, 1L);
        final OrderRequest orderRequest = new OrderRequest(1L, Collections.singletonList(orderLineItemRequest));

        // when
        ThrowableAssert.ThrowingCallable callable = () -> orderService.create(orderRequest);

        // then
        assertThatIllegalArgumentException().isThrownBy(callable)
                                            .withMessage("저장되어 있는 메뉴보다 더 많은 메뉴가 입력되었습니다.");
    }

    @Test
    @DisplayName("주문 생성 실패 - 빈 주문 테이블")
    void createFailIfOrderTableIsEmptyTest() {

        // given
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L);
        final OrderRequest orderRequest = new OrderRequest(2L, Collections.singletonList(orderLineItemRequest));

        // when
        ThrowableAssert.ThrowingCallable callable = () -> orderService.create(orderRequest);

        // then
        assertThatIllegalArgumentException().isThrownBy(callable)
                                            .withMessage("주문 테이블이 비어있습니다.");
    }

    @Test
    @DisplayName("주문 상태 변경")
    void changeOrderStatusTest() {

        // given
        final Order savedCookingOrder = orderService.create(cookingOrder);

        // when
        final Order changedOrder = orderService.changeOrderStatus(savedCookingOrder.getId(), OrderStatus.COMPLETION);

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }
}

package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.dto.request.order.ChangeOrderRequest;
import kitchenpos.dto.request.order.CreateOrderRequest;
import kitchenpos.dto.request.order.OrderLineItemsDto;
import kitchenpos.dto.response.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @DisplayName("주문을 생성한다")
    @Test
    void create()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // given
        final int newOrderId = orderService.list().size() + 1;
        final List<OrderLineItemsDto> dto = List.of(
                getRequest(OrderLineItemsDto.class, 1L, 1L, 1L, 1L),
                getRequest(OrderLineItemsDto.class, 2L, 2L, 2L, 2L)
        );
        final CreateOrderRequest request = getRequest(CreateOrderRequest.class, 5L, dto);
        // when
        final OrderResponse actual = orderService.create(request);

        // then
        assertThat(actual.getId()).isEqualTo(newOrderId);
    }

    @DisplayName("주문 생성에 실패한다")
    @ParameterizedTest(name = "{0} 주문 생성 시 실패한다")
    @MethodSource("orderTableProvider")
    void create_Fail(
            final String name,
            final Long id,
            final List<Long> products
    ) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // given
        final List<OrderLineItemsDto> dto = new ArrayList<>();
        for (Long productId : products) {
            dto.add(getRequest(OrderLineItemsDto.class, productId, productId, productId, productId));
        }
        final CreateOrderRequest request = getRequest(CreateOrderRequest.class, id, dto);

        // when
        assertThatThrownBy(() -> orderService.create(request));
    }

    private static Stream<Arguments> orderTableProvider() {
        return Stream.of(
                Arguments.of("상품이 없는", 5L, List.of()),
                Arguments.of("메뉴에 없는 상품", 5L, List.of(-1L)),
                Arguments.of("빈 테이블", 1L, List.of(1L, 2L)),
                Arguments.of("존재하지 않는", -1L, List.of(1L, 2L))
        );
    }

    @DisplayName("주문 목록을 조회한다")
    @Test
    void list() {
        // then
        final List<OrderResponse> actual = orderService.list();

        // then
        assertThat(actual).hasSize(2);
    }

    @DisplayName("주문 상태를 변경한다")
    @Test
    void changeOrderStatus()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // given
        final ChangeOrderRequest request = getRequest(ChangeOrderRequest.class, "COMPLETION");

        // when
        final OrderResponse actual = orderService.changeOrderStatus(1L, request);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(request.getOrderStatus());
    }
}

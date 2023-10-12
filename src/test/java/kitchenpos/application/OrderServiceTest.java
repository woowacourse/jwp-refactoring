package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Stream;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class OrderServiceTest extends ServiceTest {

    @DisplayName("주문을 생성한다")
    @Test
    void create() {
        // when
        final Order actual = createOrder(createOrderTable(null, 1, false), List.of(1L, 2L));

        // then
        assertThat(actual.getOrderLineItems()).hasSize(2);
    }

    @DisplayName("주문 생성에 실패한다")
    @ParameterizedTest(name = "{0} 주문 생성 시 실패한다")
    @MethodSource("orderTableProvider")
    void create_Fail(
            final String name,
            final boolean empty,
            final List<Long> products
    ) {
        // when
        assertThatThrownBy(() -> createOrder(createOrderTable(null, 1, empty), products));
    }

    private static Stream<Arguments> orderTableProvider() {
        return Stream.of(
                Arguments.of("상품이 없는", false, List.of()),
                Arguments.of("메뉴에 없는 상품", false, List.of(-1L)),
                Arguments.of("빈 테이블", true, List.of(1L))
        );
    }

    @DisplayName("존재하지 않는 테이블 주문 시 실패한다")
    @Test
    void create_FailNoExistTable() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(-1L);
        orderTable.setEmpty(false);

        // when & then
        assertThatThrownBy(() -> createOrder(orderTable, List.of(1L)));
    }

    @DisplayName("주문 목록을 조회한다")
    @Test
    void list() {
        // given
        orderService.create(createOrder(createOrderTable(null, 1, false), List.of(1L, 2L)));
        orderService.create(createOrder(createOrderTable(null, 1, false), List.of(3L, 4L)));

        // then
        final List<Order> actual = orderService.list();

        // then
        assertThat(actual).isNotNull();
    }

    @DisplayName("주문 상태를 변경한다")
    @Test
    void changeOrderStatus() {
        // given
        final Order order = orderService.create(createOrder(createOrderTable(null, 1, false), List.of(1L, 2L)));
        order.setOrderStatus("COMPLETION");

        // when
        final Order actual = orderService.changeOrderStatus(order.getId(), order);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(order.getOrderStatus());
    }
}

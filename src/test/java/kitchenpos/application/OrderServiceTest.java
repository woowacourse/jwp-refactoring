package kitchenpos.application;

import static kitchenpos.fixture.OrderFixture.createRequestOrderStatus;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class OrderServiceTest extends ServiceTest {

    @DisplayName("주문을 추가한다.")
    @Test
    void create() {
        // given
        final MenuGroup menuGroup = saveAndGetMenuGroup();
        final Menu menu = saveAndGetMenu(menuGroup.getId());

        final OrderTable orderTable = saveAndGetOrderTable(false);
        final OrderLineItemCreateRequest orderLineItem = createOrderLineItemCreateRequest(
            menu.getId(), 1);

        final OrderCreateRequest request = createOrderCreateRequest(orderTable.getId(),
            List.of(orderLineItem));

        // when
        final Order actual = orderService.create(request);

        // then
        assertThat(actual.getId()).isNotNull();
    }

    @DisplayName("create 메서드는 orderLineItems가 비어있으면 예외를 발생시킨다.")
    @Test
    void create_empty_orderLineItems_throwException() {
        // given
        final OrderTable orderTable = saveAndGetOrderTable(false);

        final OrderCreateRequest request = createOrderCreateRequest(orderTable.getId(), List.of());

        // when & then
        assertThatThrownBy(() -> orderService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create 메서드는 menuId가 가진 OrderLineItem과 OrderLineItems이 다르면 예외를 발생시킨다.")
    @Test
    void create_invalid_menuId_or_orderLineItems_throwException() {
        // given
        final MenuGroup menuGroup = saveAndGetMenuGroup();
        final Menu menu = saveAndGetMenu(menuGroup.getId());

        final OrderTable orderTable = saveAndGetOrderTable(false);
        final OrderLineItemCreateRequest orderLineItem1 = createOrderLineItemCreateRequest(
            menu.getId(), 1);
        final OrderLineItemCreateRequest orderLineItem2 = createOrderLineItemCreateRequest(
            menu.getId() + 10, 1);

        final OrderCreateRequest request = createOrderCreateRequest(orderTable.getId(),
            List.of(orderLineItem1, orderLineItem2));

        // when & then
        assertThatThrownBy(() -> orderService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create 메서드는 같은 menuId가 중복되면 예외를 발생시킨다..")
    @Test
    void create_duplicate_menuId_throwException() {
        // given
        final MenuGroup menuGroup = saveAndGetMenuGroup();
        final Menu menu = saveAndGetMenu(menuGroup.getId());

        final OrderTable orderTable = saveAndGetOrderTable(false);
        final OrderLineItemCreateRequest orderLineItem1 = createOrderLineItemCreateRequest(
            menu.getId(), 1);
        final OrderLineItemCreateRequest orderLineItem2 = createOrderLineItemCreateRequest(
            menu.getId(), 1);

        final OrderCreateRequest request = createOrderCreateRequest(orderTable.getId(),
            List.of(orderLineItem1, orderLineItem2));

        // when & then
        assertThatThrownBy(() -> orderService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        // given
        saveAndGetOrder();

        // when
        final List<Order> actual = orderService.list();

        // then
        assertThat(actual).hasSize(1);
    }

    @DisplayName("주문의 상태를 변경한다.")
    @ParameterizedTest
    @CsvSource(value = {"COOKING", "MEAL", "COMPLETION"})
    void changeOrderStatus(final OrderStatus status) {
        // given
        final Order order = saveAndGetOrder();
        final Order request = createRequestOrderStatus(status);

        // when
        final Order actual = orderService.changeOrderStatus(order.getId(), request);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(status.name());
    }

    @DisplayName("changeOrderStatus메서드는 주문의 상태가 COMPLETION인 경우 예외를 발생시킨다.")
    @Test
    void changeOrderStatus_status_completion_throwException() {
        // given
        final Order order = saveAndGetOrder(OrderStatus.COMPLETION.name());

        final Order request = createRequestOrderStatus(OrderStatus.COMPLETION);

        // when & when
        assertThatThrownBy(
            () -> orderService.changeOrderStatus(order.getId(), request))
            .isInstanceOf(IllegalArgumentException.class);
    }
}

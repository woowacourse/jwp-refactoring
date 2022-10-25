package kitchenpos.application;

import static kitchenpos.fixture.DomainCreator.createOrder;
import static kitchenpos.fixture.DomainCreator.createOrderLineItem;
import static kitchenpos.fixture.OrderFixture.createRequestOrderStatus;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class OrderServiceTest extends ServiceTest {

    @DisplayName("주문을 추가한다.")
    @Test
    void create() {
        // given
        MenuGroup menuGroup = saveAndGetMenuGroup();
        Menu menu = saveAndGetMenu(menuGroup.getId());

        OrderTable orderTable = saveAndGetOrderTable(false);
        OrderLineItem orderLineItem = createOrderLineItem(null, null, menu.getId(), 1);

        Order request = createOrder(null, orderTable.getId(), null, null, List.of(orderLineItem));

        // when
        Order actual = orderService.create(request);

        // then
        assertThat(actual.getId()).isNotNull();
    }

    @DisplayName("create 메서드는 orderLineItems가 비어있으면 예외를 발생시킨다.")
    @Test
    void create_empty_orderLineItems_throwException() {
        // given
        OrderTable orderTable = saveAndGetOrderTable(false);

        Order request = createOrder(null, orderTable.getId(), null, null, new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> orderService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create 메서드는 menuId가 가진 OrderLineItem과 OrderLineItems이 다르면 예외를 발생시킨다.")
    @Test
    void create_invalid_menuId_or_orderLineItems_throwException() {
        // given
        MenuGroup menuGroup = saveAndGetMenuGroup();
        Menu menu = saveAndGetMenu(menuGroup.getId());

        OrderTable orderTable = saveAndGetOrderTable(false);
        OrderLineItem orderLineItem1 = createOrderLineItem(null, null, menu.getId(), 1);
        OrderLineItem orderLineItem2 = createOrderLineItem(null, null, menu.getId() + 10, 1);

        Order request = createOrder(null, orderTable.getId(), null, null, List.of(orderLineItem1, orderLineItem2));

        // when & then
        assertThatThrownBy(() -> orderService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        // given
        saveAndGetOrder();

        // when
        List<Order> actual = orderService.list();

        // then
        assertThat(actual).hasSize(1);
    }

    @DisplayName("주문의 상태를 변경한다.")
    @ParameterizedTest
    @CsvSource(value = {"COOKING", "MEAL", "COMPLETION"})
    void changeOrderStatus(OrderStatus status) {
        // given
        Order order = saveAndGetOrder();
        Order request = createRequestOrderStatus(status);

        // when
        Order actual = orderService.changeOrderStatus(order.getId(), request);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(status.name());
    }

    @DisplayName("changeOrderStatus메서드는 주문의 상태가 COMPLETION인 경우 예외를 발생시킨다.")
    @Test
    void changeOrderStatus_status_completion_throwException() {
        // given
        Order order = saveAndGetOrder(OrderStatus.COMPLETION.name());

        Order request = createRequestOrderStatus(OrderStatus.COMPLETION);

        // when & when
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), request)).isInstanceOf(
                IllegalArgumentException.class);
    }
}

package kitchenpos.application;

import static kitchenpos.fixture.DomainCreator.createOrder;
import static kitchenpos.fixture.DomainCreator.createOrderLineItem;
import static kitchenpos.fixture.OrderFixture.createRequestOrderStatus;
import static org.assertj.core.api.Assertions.assertThat;

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
}

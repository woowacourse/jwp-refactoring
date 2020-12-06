package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @DisplayName("주문을 그룹 해제할수 있는지 확인한다.")
    @ParameterizedTest
    @CsvSource(value = {"COMPLETION,true", "MEAL,false", "COOKING,false"})
    void isUngroupable(OrderStatus orderStatus, boolean expect) {
        //given
        Order order = new Order(orderStatus);

        //when
        boolean result = order.isUngroupable();

        //then
        assertThat(result).isEqualTo(expect);
    }

    @DisplayName("주문의 주문상태는 완료가 아니면 변경할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"MEAL,false", "COOKING,false"})
    void changeStatus(OrderStatus orderStatus, boolean expect) {
        //given
        Order order = new Order(orderStatus);

        //when
        order.changeStatus("COMPLETION");

        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.valueOf("COMPLETION"));
    }

    @DisplayName("주문의 주문상태가 완료라면 변경할 수 없다.")
    @Test
    void changeStatusException() {
        Order order = new Order(OrderStatus.COMPLETION);

        assertThatThrownBy(() -> order.changeStatus("COMPLETION"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상태가 계산 완료인 경우 변경할 수 없습니다.");
    }

    @DisplayName("주문의 수량과 메뉴를 이용해서 OrderLineItem을 생성한다.")
    @Test
    void createOrderLineItem() {
        //given
        Menu menu = new Menu("메뉴이름", 1000L, new MenuGroup("메뉴그룹"));
        Long quantity = 1L;

        //when

        OrderLineItem orderLineItem = new OrderLineItem(quantity, menu);

        //then
        assertThat(orderLineItem.getMenu()).isEqualTo(menu);
        assertThat(orderLineItem.getQuantity()).isEqualTo(quantity);
    }
}
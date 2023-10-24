package kitchenpos.domain;

import java.lang.reflect.Field;
import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;

import static kitchenpos.domain.order.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.platform.commons.util.ReflectionUtils.HierarchyTraversalMode.TOP_DOWN;

class OrderTest {

    public static final Order 주문_fixture = new Order(2L,
            List.of(new OrderLineItem(1L, null, 1L, 1L), new OrderLineItem(2L, null, 2L, 1L))
    );

    @Test
    void id_가_같으면_동등하다() throws IllegalAccessException {
        //given
        Field id = ReflectionUtils.findFields(Order.class, field -> field.getName().equals("id"), TOP_DOWN).get(0);
        Order 주문_객체 = 주문_fixture;
        Order 다른_주문_객체 = new Order(3L,
                List.of(new OrderLineItem(1L, null, 1L, 1L), new OrderLineItem(2L, null, 2L, 1L)));

        id.setAccessible(true);
        id.set(주문_객체, 1L);
        id.set(다른_주문_객체, 1L);

        //when
        boolean actual = 주문_객체.equals(다른_주문_객체);

        //then
        assertThat(actual).isTrue();
    }

    @Test
    void 같은_상태로_변경하면_예외가_발생한다() {
        //expect
        assertThatThrownBy(() -> 주문_fixture.changeOrderStatus(COOKING))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("같은 상태로 변경할 수 없습니다.");
    }

}

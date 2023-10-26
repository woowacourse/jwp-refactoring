package kitchenpos.domain;

import java.lang.reflect.Field;
import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.commons.util.ReflectionUtils;

import static kitchenpos.domain.order.OrderLineItemFixture.id_없는_주문항목;
import static kitchenpos.domain.order.OrderLineItemFixture.주문항목;
import static kitchenpos.domain.order.OrderStatus.COOKING;
import static kitchenpos.domain.order.OrderStatus.NOT_STARTED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.platform.commons.util.ReflectionUtils.HierarchyTraversalMode.TOP_DOWN;

class OrderTest {

    public static final Order 주문_fixture = new Order(2L, List.of(id_없는_주문항목(), 주문항목(2L)));

    @Test
    void id_가_같으면_동등하다() throws IllegalAccessException {
        //given
        Field id = ReflectionUtils.findFields(Order.class, field -> field.getName().equals("id"), TOP_DOWN).get(0);
        Order 주문_객체 = 주문_fixture;
        Order 다른_주문_객체 = new Order(3L, List.of(id_없는_주문항목(), 주문항목(2L)));

        id.setAccessible(true);
        id.set(주문_객체, 1L);
        id.set(다른_주문_객체, 1L);

        //when
        boolean actual = 주문_객체.equals(다른_주문_객체);

        //then
        assertThat(actual).isTrue();
    }

    @ParameterizedTest
    @EnumSource(mode = EnumSource.Mode.EXCLUDE, names = {"NOT_STARTED"})
    void 올바르지_않은_상태에서_조리중으로_변경하면_예외가_발생한다(OrderStatus orderStatus) {
        //given
        Order order = new Order(null, 1L, orderStatus, null, null);

        //expect
        assertThatThrownBy(() -> order.startCooking())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 진행 중인 주문입니다.");
    }

    @ParameterizedTest
    @EnumSource(mode = EnumSource.Mode.EXCLUDE, names = {"COOKING"})
    void 올바르지_않은_상태에서_식사중으로_변경하면_예외가_발생한다(OrderStatus orderStatus) {
        //given
        Order order = new Order(null, 1L, orderStatus, null, null);

        //expect
        assertThatThrownBy(() -> order.startMeal())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("조리 중인 주문이 아닙니다.");
    }

    @ParameterizedTest
    @EnumSource(mode = EnumSource.Mode.EXCLUDE, names = {"MEAL"})
    void 올바르지_않은_상태에서_식사완료_로_변경하면_예외가_발생한다(OrderStatus orderStatus) {
        //given
        Order order = new Order(null, 1L, orderStatus, null, null);

        //expect
        assertThatThrownBy(() -> order.completeOrder())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("식사 중인 주문이 아닙니다.");
    }

}

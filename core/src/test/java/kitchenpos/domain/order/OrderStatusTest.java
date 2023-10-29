package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderStatusTest {

    @Test
    void matches_메서드는_자기_자신과_같은_OrderStatus를_전달하면_true를_반환한다() {
        // when
        final boolean actual = OrderStatus.MEAL.matches(OrderStatus.MEAL);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void matches_메서드는_자기_자신과_다른_OrderStatus를_전달하면_false를_반환한다() {
        // when
        final boolean actual = OrderStatus.MEAL.matches(OrderStatus.COOKING);

        // then
        assertThat(actual).isFalse();
    }
}

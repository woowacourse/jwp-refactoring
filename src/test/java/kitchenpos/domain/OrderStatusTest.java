package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OrderStatusTest {

    @Test
    void changeStatus() {
        assertAll(
                () -> assertThat(OrderStatus.COOKING.next()).isEqualTo(OrderStatus.MEAL),
                () -> assertThat(OrderStatus.MEAL.next()).isEqualTo(OrderStatus.COMPLETION),
                () -> assertThatThrownBy(OrderStatus.COMPLETION.next()).isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("이미 완료되었습니다.")
        );
    }
}

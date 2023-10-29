package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import domain.OrderStatus;
import exception.OrderStatusException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class OrderStatusTest {

    @Test
    void 해당하는_주문_상태가_없으면_예외를_발생시킨다() {
        Assertions.assertThatThrownBy(() -> OrderStatus.checkIfHas("ㅋㅋ"))
                .isInstanceOf(OrderStatusException.class)
                .hasMessage("해당하는 주문 상태가 없습니다.");
    }

    @CsvSource(value = {"COMPLETION:true", "MEAL:false"}, delimiter = ':')
    @ParameterizedTest
    void 해당하는_주문_상태가_주문_완료인지_확인한다(OrderStatus orderStatus, boolean expected) {
        Assertions.assertThat(OrderStatus.checkWhetherCompletion(orderStatus)).isEqualTo(expected);
    }

    @CsvSource(value = {"COOKING:true", "MEAL:false"}, delimiter = ':')
    @ParameterizedTest
    void 해당하는_주문_상태가_요리중인지_확인한다(OrderStatus orderStatus, boolean expected) {
        Assertions.assertThat(OrderStatus.checkWhetherCooking(orderStatus)).isEqualTo(expected);
    }

    @CsvSource(value = {"MEAL:true", "COOKING:false"}, delimiter = ':')
    @ParameterizedTest
    void 해당하는_주문_상태가_식사중인지_확인한다(OrderStatus orderStatus, boolean expected) {
        Assertions.assertThat(OrderStatus.checkWhetherMeal(orderStatus)).isEqualTo(expected);
    }
}

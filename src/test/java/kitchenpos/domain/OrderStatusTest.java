package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.CustomErrorCode;
import kitchenpos.exception.DomainLogicException;
import org.junit.jupiter.api.Test;

class OrderStatusTest {

    @Test
    void 주문_상태를_식사로_변경한다() {
        // given
        final var status = OrderStatus.COOKING;

        // when
        final var changed = status.meal();

        // then
        assertThat(changed).isEqualTo(OrderStatus.MEAL);
    }

    @Test
    void 주문_상태를_계산_완료로_변경한다() {
        // given
        final var status = OrderStatus.MEAL;

        // when
        final var changed = status.complete();

        // then
        assertThat(changed).isEqualTo(OrderStatus.COMPLETION);
    }

    @Test
    void 계산_완료인데_상태를_변경하려하면_예외를_던진다() {
        // given
        final var status = OrderStatus.COMPLETION;

        // when & then
        assertThatThrownBy(status::complete)
                .isInstanceOf(DomainLogicException.class)
                .extracting("errorCode")
                .isEqualTo(CustomErrorCode.ORDER_STATUS_ALREADY_COMPLETED_ERROR);
    }

    @Test
    void 같은_상태로_변경하려면_예외를_던진다() {
        // given
        final var status = OrderStatus.MEAL;

        // when & then
        assertThatThrownBy(status::meal)
                .isInstanceOf(DomainLogicException.class)
                .extracting("errorCode")
                .isEqualTo(CustomErrorCode.ORDER_STATUS_CHANGE_SAME_ERROR);
    }
}

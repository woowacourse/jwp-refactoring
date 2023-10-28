package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import kitchenpos.domain.Order;
import kitchenpos.domain.vo.OrderStatus;
import kitchenpos.test.fixture.OrderFixture;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("NonAsciiCharacters")
class OrderTest {

    @Nested
    class 주문_진행_확인_시 {

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        void 요리_중이거나_식사_중이라면_주문이_진행중이다(OrderStatus orderStatus) {
            //given
            OrderTable orderTable = new OrderTable(2, false);
            Order order = OrderFixture.주문(orderTable.getId(), orderStatus, LocalDateTime.now());

            //when
            boolean progress = order.isProgress();

            //then
            assertThat(progress).isTrue();
        }

        @Test
        void 종료된_상태라면_주문이_진행중이_아니다() {
            //given
            OrderTable orderTable = new OrderTable(2, false);
            Order order = OrderFixture.주문(orderTable.getId(), OrderStatus.COMPLETION, LocalDateTime.now());

            //when
            boolean progress = order.isProgress();

            //then
            assertThat(progress).isFalse();
        }
    }
}

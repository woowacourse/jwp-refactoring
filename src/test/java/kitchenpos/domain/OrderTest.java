package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import kitchenpos.exception.KitchenPosException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderTest {

    @Nested
    class 생성 {

        @Test
        void 주문_테이블이_비어있는_상태이면_예외() {
            // given
            OrderTable orderTable = new OrderTable(1L, true, 0);
            LocalDateTime orderedTime = LocalDateTime.parse("2023-10-15T22:40:00");

            // when & then
            assertThatThrownBy(() -> new Order(1L, OrderStatus.COOKING, orderedTime, orderTable))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("주문 테이블은 비어있는 상태일 수 없습니다.");
        }

        @Test
        void 성공() {
            // given
            OrderTable orderTable = new OrderTable(1L, false, 0);
            LocalDateTime orderedTime = LocalDateTime.parse("2023-10-15T22:40:00");

            // when
            Order order = new Order(1L, OrderStatus.COOKING, orderedTime, orderTable);

            // then
            assertThat(order.getId()).isEqualTo(1L);
        }
    }

    @Nested
    class changeStatus {

        @Test
        void 상태가_계산_완료이면_예외() {
            // given
            OrderTable orderTable = new OrderTable(1L, false, 0);
            LocalDateTime orderedTime = LocalDateTime.parse("2023-10-15T22:40:00");
            Order order = new Order(1L, OrderStatus.COMPLETION, orderedTime, orderTable);

            // when & then
            assertThatThrownBy(() -> order.changeStatus(OrderStatus.COMPLETION))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("계산이 완료된 주문은 상태를 변경할 수 없습니다.");
        }

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, mode = Mode.EXCLUDE, names = {"COMPLETION"})
        void 상태가_계산_완료가_아니면_성공(OrderStatus orderStatus) {
            // given
            OrderTable orderTable = new OrderTable(1L, false, 0);
            LocalDateTime orderedTime = LocalDateTime.parse("2023-10-15T22:40:00");
            Order order = new Order(1L, orderStatus, orderedTime, orderTable);

            // when
            order.changeStatus(OrderStatus.COMPLETION);

            // then
            assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
        }
    }
}

package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.order.domain.model.Order;
import kitchenpos.order.domain.model.OrderStatus;
import kitchenpos.order.supports.OrderFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderTest {

    @Nested
    class 상태_변경 {

        @Test
        void 이미_계산완료된_주문이면_예외() {
            // given
            Order order = OrderFixture.fixture().orderStatus(OrderStatus.COMPLETION).build();

            // when & then
            assertThatThrownBy(() -> order.changeStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 계산 완료된 주문입니다.");
        }

        @Test
        void 성공() {
            // given
            Order order = OrderFixture.fixture().orderStatus(OrderStatus.COOKING).build();

            // when
            order.changeStatus(OrderStatus.MEAL);

            // then
            assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
        }
    }

    @Nested
    class 그룹해제_가능_판단 {

        @Test
        void 계산_완료면_참() {
            // given
            Order order = OrderFixture.fixture().orderStatus(OrderStatus.COMPLETION).build();

            // when
            boolean result = order.isAbleToUngroup();

            // then
            assertThat(result).isTrue();
        }

        @ValueSource(strings = {"COOKING", "MEAL"})
        @ParameterizedTest
        void 계산_완료가_아니면_거짓(OrderStatus orderStatus) {
            // given
            Order order = OrderFixture.fixture().orderStatus(orderStatus).build();

            // when
            boolean result = order.isAbleToUngroup();

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    class 빈테이블_변경_가능_판단 {

        @Test
        void 계산_완료면_참() {
            // given
            Order order = OrderFixture.fixture().orderStatus(OrderStatus.COMPLETION).build();

            // when
            boolean result = order.isAbleToChangeEmpty();

            // then
            assertThat(result).isTrue();
        }

        @ValueSource(strings = {"COOKING", "MEAL"})
        @ParameterizedTest
        void 계산_완료가_아니면_거짓(OrderStatus orderStatus) {
            // given
            Order order = OrderFixture.fixture().orderStatus(orderStatus).build();

            // when
            boolean result = order.isAbleToChangeEmpty();

            // then
            assertThat(result).isFalse();
        }
    }
}

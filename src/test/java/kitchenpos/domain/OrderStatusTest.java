package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;

@DisplayName("OrderStatus의")
class OrderStatusTest {

    @Nested
    @DisplayName("collectInProgress 메서드는")
    class CollectInProgress {
        @Test
        @DisplayName("주문 상태 중 진행중인 상태 목록을 반환한다.")
        void success() {
            List<String> progressStatuses = OrderStatus.collectInProgress();
            assertThat(progressStatuses).contains(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
        }
    }

    @Nested
    @DisplayName("isCompleted 메서드는")
    class IsCompleted {

        @Test
        @DisplayName("완료 상태를 받으면, 참을 반환한다.")
        void success() {
            OrderStatus status = OrderStatus.COMPLETION;
            assertThat(OrderStatus.isCompleted(status)).isTrue();
        }

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COMPLETION"}, mode = Mode.EXCLUDE)
        @DisplayName("완료가 아닌 상태를 받으면, 거짓을 반환한다.")
        void fail_notCompleted() {
            OrderStatus status = OrderStatus.COMPLETION;
            assertThat(OrderStatus.isCompleted(status)).isTrue();
        }
    }
}

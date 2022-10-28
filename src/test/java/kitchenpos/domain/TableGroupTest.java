package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Nested
    class 생성자는 {

        @Test
        void 그룹화하려는_테이블이_비어_있지_않다면_예외가_발생한다() {
            // given
            OrderTable orderTable = new OrderTable(null, 7, false);

            // when & then
            Assertions.assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), List.of(orderTable)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 그룹화하려는_테이블이_이미_다른_그룹에_속해있다면_예외가_발생한다() {
            // given
            OrderTable orderTable = new OrderTable(1L, 0, true);

            // when & then
            Assertions.assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), List.of(orderTable)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}

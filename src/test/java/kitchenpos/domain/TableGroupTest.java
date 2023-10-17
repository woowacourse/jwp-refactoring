package kitchenpos.domain;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class TableGroupTest {

    @Nested
    class 테이블_그룹_생성 {
        @Test
        void 테이블이_2개_이하인_경우_예외가_발생한다() {
            //given
            List<OrderTable> 테이블_목록 = List.of(new OrderTable());

            //expect
            assertThatThrownBy(() -> new TableGroup(null, null, 테이블_목록))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("테이블 그룹은 2개 이상의 테이블로 구성되어야 합니다.");
        }

        @Test
        void 비어있지_않은_테이블이_포함되면_예외가_발생한다() {
            //given
            List<OrderTable> 테이블_목록 = List.of(new OrderTable(2, false), new OrderTable(0, true));

            //expect
            assertThatThrownBy(() -> new TableGroup(null, null, 테이블_목록))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("빈 테이블만 테이블 그룹으로 만들 수 있습니다.");
        }
    }



}

package kitchenpos.domain;

import static kitchenpos.common.fixture.OrderTableFixture.단체_지정_없는_빈_주문_테이블;
import static kitchenpos.common.fixture.OrderTableFixture.주문_테이블;
import static kitchenpos.common.fixture.TableGroupFixture.단체_지정;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.common.fixture.OrderTableFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TableGroupTest {

    @Nested
    class 단체_지정을_생성할_때 {

        @Test
        void 주문_테이블_ID_목록과_저장된_주문_테이블_목록의_사이즈가_다를_경우_예외를_던진다() {
            // given
            List<Long> invalidOrderTableIds = List.of(Long.MIN_VALUE, Long.MAX_VALUE);
            List<OrderTable> savedOrderTables = List.of(단체_지정_없는_빈_주문_테이블());

            // expect
            assertThatThrownBy(() -> TableGroup.of(invalidOrderTableIds, savedOrderTables))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 주문 테이블이 있거나 주문 테이블이 중복되었습니다.");
        }

        @Test
        void 주문_테이블_목록이_비었으면_예외를_던진다() {
            // given
            List<Long> orderTableIds = List.of();
            List<OrderTable> invalidOrderTables = List.of();

            // expect
            assertThatThrownBy(() -> TableGroup.of(orderTableIds, invalidOrderTables))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("단체 지정의 주문 테이블 개수는 최소 2 이상이어야 합니다.");
        }

        @Test
        void 주문_테이블의_개수가_2개_미만이면_예외를_던진다() {
            // given
            List<Long> orderTableIds = List.of(1L);
            List<OrderTable> invalidOrderTables = List.of(단체_지정_없는_빈_주문_테이블());

            // expect
            assertThatThrownBy(() -> TableGroup.of(orderTableIds, invalidOrderTables))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("단체 지정의 주문 테이블 개수는 최소 2 이상이어야 합니다.");
        }

        @Test
        void 주문_테이블_중_채워진_테이블이_있는_경우_예외를_던진다() {
            // given
            List<Long> orderTableIds = List.of(1L, 2L);
            List<OrderTable> invalidOrderTables = List.of(단체_지정_없는_빈_주문_테이블(),
                    OrderTableFixture.단체_지정_없는_주문_테이블());

            // expect
            assertThatThrownBy(() -> TableGroup.of(orderTableIds, invalidOrderTables))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("단체 지정의 주문 테이블이 차 있거나 이미 단체 지정되었습니다.");
        }

        @Test
        void 주문_테이블_중_이미_단체_지정된_테이블이_있는_경우_예외를_던진다() {
            // given
            TableGroup tableGroup = 단체_지정();
            List<Long> orderTableIds = List.of(1L, 2L);
            List<OrderTable> invalidOrderTables = List.of(단체_지정_없는_빈_주문_테이블(), 주문_테이블(tableGroup));

            // expect
            assertThatThrownBy(() -> TableGroup.of(orderTableIds, invalidOrderTables))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("단체 지정의 주문 테이블이 차 있거나 이미 단체 지정되었습니다.");
        }
    }

    @Nested
    class 단체_지정을_해제할_때 {

        @Test
        void 조리_혹은_식사_주문이_존재하는_경우_예외를_던진다() {
            // given
            TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(주문_테이블(단체_지정())));
            boolean hasCookingOrMealOrder = true;

            // expect
            assertThatThrownBy(() -> tableGroup.ungroup(hasCookingOrMealOrder))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("조리 혹은 식사 주문이 존재하는 단체 지정은 단체 지정을 취소할 수 없습니다.");
        }

        @Test
        void 정상적으로_해제한다() {
            // given
            TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(주문_테이블(단체_지정())));
            boolean hasCookingOrMealOrder = false;

            // expect
            assertThatNoException().isThrownBy(() -> tableGroup.ungroup(hasCookingOrMealOrder));
        }
    }
}

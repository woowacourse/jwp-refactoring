package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class OrderTableTest {

    @Nested
    class Constructor_성공_테스트 {
    }

    @Nested
    class Constructor_실패_테스트 {

        @Test
        void 손님의_수가_0보다_작으면_에러를_반환한다() {
            // given & when & then
            assertThatThrownBy(() -> new OrderTable(new TableGroup(), -3, true))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 손님의 수가 음수입니다.");
        }
    }

    @Nested
    class changeEmpty_성공_테스트 {

        @Test
        void 주문_테이블을_빈테이블로_변경할_수_있다() {
            // given
            final var orderTable = new OrderTable(new TableGroup(), 3, false);

            // when
            orderTable.changeEmpty(true);

            // then
            assertThat(orderTable.isEmpty()).isTrue();
        }

        @Test
        void 주문_테이블을_비어있지_않은_테이블로_변경할_수_있다() {
            // given
            final var orderTable = new OrderTable(new TableGroup(), 3, true);

            // when
            orderTable.changeEmpty(false);

            // then
            assertThat(orderTable.isEmpty()).isFalse();
        }
    }

    @Nested
    class changeEmpty_실패_테스트 {
    }

    @Nested
    class updateNumberOfGuests_성공_테스트 {

        @ParameterizedTest
        @ValueSource(ints = {0, 5})
        void 손님의_수는_0명_이상으로_변경할_수_있다(final int numberOfGuests) {
            // given
            final var orderTable = new OrderTable(new TableGroup(), 3, false);

            // when
            orderTable.updateNumberOfGuests(numberOfGuests);

            // then
            assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
        }
    }

    @Nested
    class updateNumberOfGuests_실패_테스트 {

        @Test
        void 손님의_수가_0보다_작으면_에러를_반환한다() {
            // given
            final var orderTable = new OrderTable(new TableGroup(), 3, false);

            // when & then
            assertThatThrownBy(() -> orderTable.updateNumberOfGuests(-3))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 손님의 수가 음수입니다.");
        }

        @Test
        void 주문_테이블이_비어_있으면_에러를_반환한다() {
            // given
            final var orderTable = new OrderTable(new TableGroup(), 3, true);

            // when & then
            assertThatThrownBy(() -> orderTable.updateNumberOfGuests(4))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 주문 테이블이 비어있습니다.");
        }
    }

    @Nested
    class group_성공_테스트 {

        @Test
        void 주문_테이블_그룹화를_할_수_있다() {
            // given
            final var orderTable = new OrderTable(null, 3, true);
            final var tableGroup = new TableGroup();

            // when
            orderTable.group(tableGroup);

            // then
            assertThat(orderTable.getTableGroup()).isEqualTo(tableGroup);
        }
    }

    @Nested
    class group_실패_테스트 {
    }

    @Nested
    class ungroup_성공_테스트 {

        @Test
        void 주문_테이블의_그룹화를_해제할_수_있다() {
            // given
            final var orderTable = new OrderTable(new TableGroup(), 3, true);

            // when
            orderTable.ungroup();

            // then
            assertSoftly(soft -> {
                soft.assertThat(orderTable.getTableGroup()).isNull();
                soft.assertThat(orderTable.isEmpty()).isFalse();
            });
        }
    }

    @Nested
    class ungroup_실패_테스트 {
    }

    @Nested
    class findTableGroupId_성공_테스트 {

        @Test
        void 단체_테이블이_존재하면_해당_ID를_반환한다() {
            // given
            final var orderTable = new OrderTable(new TableGroup(1L), 3, true);

            // when
            final var actual = orderTable.findTableGroupId();

            // then
            assertThat(actual).isEqualTo(1L);
        }

        @Test
        void 단쳬_테이블이_존재하지_않으면_NULL을_반환한다() {
            // given
            final var orderTable = new OrderTable(null, 3, true);

            // when
            final var actual = orderTable.findTableGroupId();

            // then
            assertThat(actual).isNull();
        }
    }

    @Nested
    class findTableGroupId_실패_테스트 {

    }
}

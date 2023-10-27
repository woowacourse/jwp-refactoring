package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import kitchenpos.common.exception.KitchenPosException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MoneyTest {

    @Nested
    class 생성 {

        @Test
        void null이면_예외() {
            // when & then
            assertThatThrownBy(() -> new Money(null))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("금액은 null이 될 수 없습니다.");
        }

        @ParameterizedTest
        @ValueSource(longs = {-1, 0, 1})
        void 금액에_관계_없이_성공(long value) {
            // when
            Money money = Money.from(value);

            // then
            assertThat(money.getAmount()).isEqualTo(BigDecimal.valueOf(value));
        }
    }

    @Nested
    class plus {

        @Test
        void 금액을_더할_수_있다() {
            // given
            Money thousand = Money.from(1000);
            Money hundred = Money.from(100);

            // when
            Money actual = thousand.plus(hundred);

            // then
            assertThat(actual).isEqualTo(Money.from(1100));
        }

        @Test
        void 금액을_더하면_새로운_인스턴스가_반환() {
            // given
            Money thousand = Money.from(1000);
            Money hundred = Money.from(100);
            Money expect = Money.from(1100);

            // when
            Money actual = thousand.plus(hundred);

            // then
            assertThat(actual).isNotSameAs(expect);
        }

        @Test
        void zero를_더하면_자신을_그대로_반환() {
            // given
            Money thousand = Money.from(1000);

            // when
            Money actual = thousand.plus(Money.ZERO);

            // then
            assertThat(actual).isSameAs(thousand);
        }

        @Test
        void 자신에_zero를_더하면_그대로_반환() {
            // given
            Money thousand = Money.from(1000);

            // when
            Money actual = Money.ZERO.plus(thousand);

            // then
            assertThat(actual).isSameAs(thousand);
        }
    }

    @Nested
    class isNegative {

        @Test
        void 음수이면_true() {
            // given
            Money money = Money.from(-1);

            // when
            boolean actual = money.isNegative();

            // then
            assertThat(actual).isTrue();
        }

        @ParameterizedTest
        @ValueSource(longs = {0, 1, 2})
        void zero_또는_양수이면_false(long value) {
            // given
            Money money = Money.from(value);

            // when
            boolean actual = money.isNegative();

            // then
            assertThat(actual).isFalse();
        }
    }

    @Nested
    class isLessThan {

        @Test
        void 넘어온_인수가_자신보다_작으면_false() {
            // given
            Money big = Money.from(1000);
            Money small = Money.from(999);

            // when
            boolean actual = big.isLessThan(small);

            // then
            assertThat(actual).isFalse();
        }

        @Test
        void 넘어온_인수가_자신보다_크면_true() {
            // given
            Money big = Money.from(1000);
            Money small = Money.from(999);

            // when
            boolean actual = small.isLessThan(big);

            // then
            assertThat(actual).isTrue();
        }

        @Test
        void 넘어온_인수가_같으면_false() {
            // given
            Money same = Money.from(1000);
            Money sameAs = Money.from(1000);

            // when
            boolean actual = same.isLessThan(sameAs);

            // then
            assertThat(actual).isFalse();
        }
    }

    @Nested
    class isGreaterThan {

        @Test
        void 넘어온_인수가_자신보다_작으면_true() {
            // given
            Money big = Money.from(1000);
            Money small = Money.from(999);

            // when
            boolean actual = big.isGreaterThan(small);

            // then
            assertThat(actual).isTrue();
        }

        @Test
        void 넘어온_인수가_자신보다_크면_false() {
            // given
            Money big = Money.from(1000);
            Money small = Money.from(999);

            // when
            boolean actual = small.isGreaterThan(big);

            // then
            assertThat(actual).isFalse();
        }

        @Test
        void 넘어온_인수가_같으면_false() {
            // given
            Money same = Money.from(1000);
            Money sameAs = Money.from(1000);

            // when
            boolean actual = same.isGreaterThan(sameAs);

            // then
            assertThat(actual).isFalse();
        }
    }
}

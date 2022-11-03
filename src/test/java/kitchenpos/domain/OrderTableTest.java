package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@IgnoreDisplayNameUnderscores
class OrderTableTest {

    @Test
    void 생성자는_게스트_수가_음수면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new OrderTable(null, -1, false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void validateNotInTableGroup_메소드는_테이블이_그룹_ID를_가지고_있으면_예외가_발생한다() {
        // given
        OrderTable orderTable = new OrderTable(1L, 5, false);

        // when & then
        assertThatThrownBy(orderTable::validateNotInTableGroup)
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    void validateEmpty_메소드는_테이블이_비어_있지_않으면_예외가_발생한다() {
        // given
        OrderTable orderTable = new OrderTable(null, 2, false);

        // when & then
        assertThatThrownBy(orderTable::validateEmpty)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void validateNotEmpty_메소드는_테이블이_비어_있으면_예외가_발생한다() {
        // given
        OrderTable orderTable = new OrderTable(null, 0, true);

        // when & then
        assertThatThrownBy(orderTable::validateNotEmpty)
                .isInstanceOf(IllegalArgumentException.class);
    }
}

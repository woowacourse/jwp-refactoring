package kitchenpos.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.common.exception.InvalidNameException;
import kitchenpos.product.domain.ProductName;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
class NameTest {

    @ParameterizedTest
    @NullAndEmptySource
    void 이름이_null_이거나_공백이라면_예외가_발생한다(String invalidName) {
        // given
        // when
        final ThrowingCallable throwingCallable = () -> new ProductName(invalidName);

        // then
        assertThatThrownBy(throwingCallable)
                .isInstanceOf(InvalidNameException.class)
                .hasMessage("상품 이름은 공백일 수 없습니다.");
    }

    @Test
    void 이름이_255자를_초과하면_예외가_발생한다() {
        // given
        final var invalidName = "a".repeat(256);

        // when
        final ThrowingCallable throwingCallable = () -> new ProductName(invalidName);

        // then
        assertThatThrownBy(throwingCallable)
                .isInstanceOf(InvalidNameException.class)
                .hasMessage("상품 이름은 255자를 초과할 수 없습니다.");
    }

    @Test
    void 이름_규칙에_맞을_경우_정상적으로_생성한다() {
        // given
        final var validName = "테스트 상품";

        // when
        final var name = new ProductName(validName);

        // then
        assertThat(name.getValue()).isEqualTo(validName);
    }
}

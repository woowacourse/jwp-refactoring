package kitchenpos.domain.common;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.domain.exception.InvalidNameException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class NameTest {

    @Test
    void 생성자는_유효한_이름을_전달하면_Name을_초기화한다() {
        // when & then
        assertThatCode(() -> new Name("hello")).doesNotThrowAnyException();
    }

    @ParameterizedTest(name = "이름이 {0}이면 예외가 발생한다.")
    @NullAndEmptySource
    void 생성자는_유효하지_않은_이름을_전달하면_예외가_발생한다(final String invalidName) {
        // when & then
        assertThatThrownBy(() -> new Name(invalidName))
                .isInstanceOf(InvalidNameException.class);
    }
}

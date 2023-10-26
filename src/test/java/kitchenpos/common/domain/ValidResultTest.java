package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ValidResultTest {

    @Test
    void success_메서드로_생성된_인스턴스는_공유된다() {
        // given
        ValidResult success1 = ValidResult.success();
        ValidResult success2 = ValidResult.success();

        // when & then
        assertThat(success1).isSameAs(success2);
    }

    @Test
    void failure_메서드로_생성된_인스턴스는_예외를_발생시킨다() {
        // given
        ValidResult validResult = ValidResult.failure("예외가 발생했습니다.");

        // when & then
        assertThatThrownBy(() -> validResult.throwIfFailure(IllegalArgumentException::new))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("예외가 발생했습니다.");
    }
}

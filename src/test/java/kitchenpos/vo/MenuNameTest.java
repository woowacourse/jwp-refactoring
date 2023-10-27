package kitchenpos.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.dto.vo.MenuName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuNameTest {

    @ParameterizedTest
    @ValueSource(strings = {"chicken", "coffee"})
    void 메뉴_이름_객체를_생성한다(String value) {
        MenuName menuName = new MenuName(value);

        assertThat(menuName.getValue()).isEqualTo(value);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void 메뉴_이름이_NULL_또는_빈_값_또는_공백이면_예외가_발생한다(String value) {
        assertThatThrownBy(() -> new MenuName(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 이름은 반드시 포함되어야 합니다.");
    }
}

package kitchenpos.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.menugroup.application.dto.vo.MenuGroupName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuGroupNameTest {

    @ParameterizedTest
    @ValueSource(strings = {"korean", "setMenu"})
    void 메뉴_그룹_이름_객체를_생성한다(String value) {
        MenuGroupName menuGroupName = new MenuGroupName(value);

        assertThat(menuGroupName.getValue()).isEqualTo(value);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void 메뉴_그룹_이름이_NULL_또는_빈_값_또는_공백이면_예외가_발생한다(String value) {
        assertThatThrownBy(() -> new MenuGroupName(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹 이름은 반드시 포함되어야 합니다.");
    }
}

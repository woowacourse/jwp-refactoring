package kitchenpos.domain.menugroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class MenuGroupNameTest {

    @DisplayName("생성자 테스트 - IAE 발생, Name이 Null이거나 공란인 경우")
    @ParameterizedTest
    @NullAndEmptySource
    void constructor_NullOrEmptyName_ThrownIllegalArgumentException(String name) {
        assertThatThrownBy(() -> new MenuGroupName(name))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("생성자 테스트 - 성공, 올바른 이름")
    @ParameterizedTest
    @ValueSource(strings = {"name", "1234", "이름", "!@#$"})
    void constructor_CorrectName_Success(String name) {
        MenuGroupName menuGroupName = new MenuGroupName(name);

        assertThat(menuGroupName.getName()).isEqualTo(name);
    }

}

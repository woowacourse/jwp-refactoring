package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MenuNameTest {

    @DisplayName("생성자 테스트 - NPE 발생, Name이 Null인 경우")
    @Test
    void constructor_NullName_ThrownNullPointerException() {
        assertThatThrownBy(() -> new MenuName(null))
            .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("생성자 테스트 - IAE 발생, Name이 공란인 경우")
    @Test
    void constructor_EmptyName_ThrownIllegalArgumentException() {
        String emptyName = "";

        assertThatThrownBy(() -> new MenuName(emptyName))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("생성자 테스트 - 성공, 올바른 이름")
    @ParameterizedTest
    @ValueSource(strings = {"name", "1234", "이름", "!@#$"})
    void constructor_CorrectName_Success(String name) {
        MenuName menuName = new MenuName(name);

        assertThat(menuName.getName()).isEqualTo(name);
    }
}

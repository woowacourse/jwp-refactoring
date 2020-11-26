package kitchenpos.menugroup.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuGroupTest {
    @DisplayName("메뉴그룹의 이름은 비어있을 수 없다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createNameException(String wrongName) {
        assertThatThrownBy(() -> new MenuGroup(wrongName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("%s : 올바르지 않은 이름입니다.", wrongName);
    }

}
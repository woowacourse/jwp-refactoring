package kitchenpos.domain;

import kitchenpos.exception.FieldNotValidException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuGroupTest {
    @DisplayName("메뉴 그룹을 생성한다. - 실패, 이름이 ")
    @ParameterizedTest(name = "{displayName} {0}인 경우")
    @NullAndEmptySource
    void create(String name) {
        assertThatThrownBy(() -> new MenuGroup(name))
                .isInstanceOf(FieldNotValidException.class);
    }
}

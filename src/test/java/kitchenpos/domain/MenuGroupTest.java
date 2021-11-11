package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.InvalidMenuGroupException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MenuGroup 단위 테스트")
public class MenuGroupTest {

    @DisplayName("MenuGroup을 생성할 때 name이 Null인 경우 예외가 발생한다.")
    @Test
    void nameNullException() {
        // when, then
        assertThatThrownBy(() -> new MenuGroup(null))
            .isExactlyInstanceOf(InvalidMenuGroupException.class);
    }
}

package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class MenuGroupTest {

    @DisplayName("[SUCCESS] 생성한다.")
    @Test
    void success_create() {
        assertThatCode(() -> new MenuGroup("테스트용 메뉴명"))
                .doesNotThrowAnyException();
    }
}

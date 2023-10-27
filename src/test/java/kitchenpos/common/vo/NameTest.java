package kitchenpos.common.vo;

import kitchenpos.common.vo.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class NameTest {

    @DisplayName("[SUCCESS] 생성한다.")
    @Test
    void success_create() {
        assertThatCode(() -> new Name("테스트용 이름"))
                .doesNotThrowAnyException();
    }
}

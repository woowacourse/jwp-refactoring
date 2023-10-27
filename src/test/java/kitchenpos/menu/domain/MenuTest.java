package kitchenpos.menu.domain;

import static kitchenpos.support.TestFixtureFactory.새로운_메뉴;
import static kitchenpos.support.TestFixtureFactory.새로운_메뉴_그룹;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.menu.exception.MenuException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuTest {

    @Test
    void 메뉴의_이름은_255자_이하여야_한다() {
        assertThatThrownBy(() -> 새로운_메뉴("짱".repeat(256), BigDecimal.ONE, 새로운_메뉴_그룹(null, "메뉴 그룹")))
                .isInstanceOf(MenuException.class)
                .hasMessage("메뉴의 이름이 유효하지 않습니다.");

    }
}

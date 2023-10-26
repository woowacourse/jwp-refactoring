package kitchenpos.domain.menu;

import kitchenpos.exception.KitchenposException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static kitchenpos.exception.ExceptionInformation.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("메뉴 이름 테스트")
class NameTest {

    @ParameterizedTest
    @ValueSource(strings = {"가", "가나다라마바사아자차카타파하까나따라마빠"})
    void 이름_정상_생성(String name) {
        assertDoesNotThrow(() -> Name.create(name));
    }

    @Test
    void 메뉴_이름은_비어있을수_없다() {
        assertThatThrownBy(() -> Name.create(null))
                .isExactlyInstanceOf(KitchenposException.class)
                .hasMessage(MENU_NAME_IS_NULL.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void 메뉴_이름은_빈칸일_수_없다(String name) {
        assertThatThrownBy(() -> Name.create(name))
                .isExactlyInstanceOf(KitchenposException.class)
                .hasMessage(MENU_NAME_LENGTH_OUT_OF_BOUNCE.getMessage());
    }

    @Test
    void 메뉴_이름은_20글자_이하여야한다() {
        assertThatThrownBy(() -> Name.create("가나다라마바사아자차카타파하까나따라마빠하"))
                .isExactlyInstanceOf(KitchenposException.class)
                .hasMessage(MENU_NAME_LENGTH_OUT_OF_BOUNCE.getMessage());
    }
}

package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import kitchenpos.exception.InvalidNameException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
class MenuNameTest {

    @ParameterizedTest
    @NullAndEmptySource
    void 메뉴_이름이_비어있다면_에러가_발생한다(String name) {
        //given
        //when
        ThrowingCallable action = () -> new MenuName(name);

        //then
        assertThatThrownBy(action).isInstanceOf(InvalidNameException.class)
                                  .hasMessage("메뉴 이름은 공백일 수 없습니다.");
    }

    @Test
    void 메뉴_이름이_255자_이상이라면_에러가_발생한다() {
        //given
        final var invalidName = "메".repeat(256);

        //when
        ThrowingCallable action = () -> new MenuName(invalidName);

        //then
        assertThatThrownBy(action).isInstanceOf(InvalidNameException.class)
                                  .hasMessage("메뉴 이름은 255자를 초과할 수 없습니다.");
    }

    @Test
    void 메뉴_이름이_255자_이하라면_에러가_발생하지_않는다() {
        //given
        final var validName = "메뉴 이름";

        //when
        final ThrowingSupplier<MenuName> action = () -> new MenuName(validName);

        //then
        assertDoesNotThrow(action);
    }
}

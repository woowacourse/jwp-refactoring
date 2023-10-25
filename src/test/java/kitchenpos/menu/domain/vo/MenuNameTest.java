package kitchenpos.menu.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.menu.domain.vo.MenuName;
import kitchenpos.test.RandomStringUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
class MenuNameTest {

    @Nested
    class 메뉴명_생성_시 {

        @Test
        void 정상적인_메뉴명이라면_생성에_성공한다() {
            //given
            String name = "간장치킨";

            //when
            MenuName menuName = new MenuName(name);

            //then
            assertThat(menuName.getName()).isEqualTo(name);
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 메뉴명이_존재하지_않거나_공백이라면_예외를_던진다(String name) {
            //given, when, then
            assertThatThrownBy(() -> new MenuName(name))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴명이 존재하지 않거나 공백입니다.");
        }

        @Test
        void 메뉴명_길이가_유효하지_않으면_예외를_던진다() {
            //given
            String name = RandomStringUtils.random(256);

            //when, then
            assertThatThrownBy(() -> new MenuName(name))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴명 길이가 유효하지 않습니다.");
        }
    }
}

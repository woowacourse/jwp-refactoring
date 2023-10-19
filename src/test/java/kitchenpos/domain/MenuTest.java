package kitchenpos.domain;

import kitchenpos.fixture.MenuFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class MenuTest {

    @ParameterizedTest(name = "메뉴의 가격이 {0}이면 예외")
    @ValueSource(longs = {-1, -100})
    void 메뉴의_가격이_잘못되면_예외(Long price) {
        // when & then
        Assertions.assertThatThrownBy(() -> Menu.builder()
                        .price(BigDecimal.valueOf(price))
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullSource
    void 메뉴의_가격이_Null이면_예외(BigDecimal price) {
        Assertions.assertThatThrownBy(() -> Menu.builder()
                        .price(price)
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "메뉴가 {0}원이고 상품이 {1}원이면 예외")
    @CsvSource(value = {"17000,16999", "17000,10000", "17000,1000", "17000,1", "17000,0"})
    void 메뉴_가격이_상품들의_가격_합보다_크면_예외(Long menuPrice, BigDecimal productPrice) {
        // given
        Menu menu = MenuFixture.MENU.후라이드_치킨_N원_1마리(menuPrice);

        // when & then
        Assertions.assertThatThrownBy(() -> menu.validPrice(productPrice))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

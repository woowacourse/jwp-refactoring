package kitchenpos.domain;

import kitchenpos.domain.menu.Menu;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static kitchenpos.fixture.MenuFixture.후라이드_두마리;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuTest {

    @Test
    @Disabled
    void 메뉴_가격은_메뉴_상품들의_가격_총합을_넘을_수_없다() {
        assertThatThrownBy(() -> new Menu(
                후라이드_두마리().getName(),
                후라이드_두마리().getPrice().add(new Price(1)),
                후라이드_두마리().getMenuGroupId(),
                후라이드_두마리().getMenuProducts()
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 가격은 메뉴 상품 합계를 넘을 수 없습니다");
    }

    @Test
    void 메뉴_가격은_메뉴_상품들_가격_총합과_같을_수_있다() {
        assertThatNoException().isThrownBy(() -> new Menu(
                후라이드_두마리().getName(),
                후라이드_두마리().getPrice(),
                후라이드_두마리().getMenuGroupId(),
                후라이드_두마리().getMenuProducts()
        ));
    }
}

package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
public class MenuTest {

    @Test
    void 메뉴이름_가격_메뉴그룹ID_메뉴상품리스트를_받아서_메뉴_정보를_등록할_수_있다() {
        //given
        Long id = 1L;
        String name = "후라이드";
        BigDecimal price = BigDecimal.valueOf(19000);
        MenuGroup 추천메뉴 = MenuGroupFixture.추천메뉴();
        List<MenuProduct> 후라이드_강정치킨 = List.of(MenuProductFixture.후라이드_강정치킨());

        //when, then
        assertThatNoException().isThrownBy(() -> new Menu(id, name, price, 추천메뉴, 후라이드_강정치킨));
    }

    @Test
    void 메뉴_가격이_입력되지_않으면_예외처리한다() {
        //given
        Long id = 1L;
        String name = "후라이드";
        MenuGroup 추천메뉴 = MenuGroupFixture.추천메뉴();
        List<MenuProduct> 후라이드_강정치킨 = List.of(MenuProductFixture.후라이드_강정치킨());

        //when, then
        assertThatThrownBy(() -> new Menu(id, name, null, 추천메뉴, 후라이드_강정치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_가격이_0원_미만으로_입력되면_예외처리한다() {
        //given
        Long id = 1L;
        String name = "후라이드";
        BigDecimal price = BigDecimal.valueOf(-1000);
        MenuGroup 추천메뉴 = MenuGroupFixture.추천메뉴();
        List<MenuProduct> 후라이드_강정치킨 = List.of(MenuProductFixture.후라이드_강정치킨());

        //when, then
        assertThatThrownBy(() -> new Menu(id, name, price, 추천메뉴, 후라이드_강정치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

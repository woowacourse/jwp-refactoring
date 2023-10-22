package kitchenpos.domain.menu;

import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.fixture.MenusFixture.메뉴_가격;
import static kitchenpos.fixture.MenusFixture.메뉴_상품;
import static kitchenpos.fixture.MenusFixture.메뉴_상품들;
import static kitchenpos.fixture.MenusFixture.메뉴_이름;
import static kitchenpos.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.domain.menu_group.MenuGroup;
import kitchenpos.domain.vo.Price;
import kitchenpos.exception.MenuException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MenuTest {

    private MenuName menuName;
    private MenuGroup menuGroup;
    private Price menuPrice;
    private MenuProducts menuProducts;

    @BeforeEach
    void setUp() {
        this.menuName = 메뉴_이름("신메뉴");
        this.menuGroup = 메뉴_그룹("메뉴 그룹");
        this.menuPrice = 메뉴_가격(3000L);
        this.menuProducts = 메뉴_상품들(
                메뉴_상품(상품("후라이드", BigDecimal.valueOf(1000L)), 1L),
                메뉴_상품(상품("양념", BigDecimal.valueOf(1000L)), 2L)
        );
    }

    @Test
    void 메뉴는_메뉴_그룹이_없다면_예외가_발생한다() {
        // given
        final MenuGroup givenGroup = null;

        // when
        assertThatThrownBy(() -> new Menu(menuName, menuPrice, menuProducts, givenGroup))
                .isInstanceOf(MenuException.NoMenuGroupException.class);
    }

    @Test
    void 메뉴는_이름이_없다면_예외가_발생한다() {
        // given
        final MenuName givenName = null;

        // when
        assertThatThrownBy(() -> new Menu(givenName, menuPrice, menuProducts, menuGroup))
                .isInstanceOf(MenuException.NoMenuNameException.class);
    }

    @Test
    void 메뉴는_메뉴_상품이_없다면_예외가_발생한다() {
        // given
        final MenuProducts givenMenuProducts = null;

        // when
        assertThatThrownBy(() -> new Menu(menuName, menuPrice, givenMenuProducts, menuGroup))
                .isInstanceOf(MenuException.NoMenuProductsException.class);
    }

    @Test
    void 메뉴는_가격이_없다면_예외가_발생한다() {
        // given
        final Price givenPrice = null;

        // when
        assertThatThrownBy(() -> new Menu(menuName, givenPrice, menuProducts, menuGroup))
                .isInstanceOf(MenuException.NoPriceException.class);
    }

    @Test
    void 메뉴는_메뉴_상품의_총합보다_가격이_높다면_예외가_발생한다() {
        // given
        final Price givenPrice = new Price(BigDecimal.valueOf(Long.MAX_VALUE));

        // when
        assertThatThrownBy(() -> new Menu(menuName, givenPrice, menuProducts, menuGroup))
                .isInstanceOf(MenuException.OverPriceException.class);
    }

}

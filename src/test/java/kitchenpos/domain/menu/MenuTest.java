package kitchenpos.domain.menu;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Name;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.global.exception.KitchenposException;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.global.exception.ExceptionInformation.MENU_PRICE_OVER_MENU_PRODUCT_PRICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("메뉴에 테스트")
class MenuTest {
    private static final MenuProduct 가격_45_상품 = MenuProduct.create(new Product(1L, Name.create("name"), Price.create(new BigDecimal(15))), 3);
    private static final MenuProduct 가격_0_상품 = MenuProduct.create(new Product(1L, Name.create("name"), Price.create(new BigDecimal(15))), 0);
    private static final MenuProduct 가격_70_상품 = MenuProduct.create(new Product(1L, Name.create("name"), Price.create(new BigDecimal(10))), 7);
    private static final MenuProducts 총가격_115인_상품들 = MenuProducts.create(List.of(가격_45_상품, 가격_0_상품, 가격_70_상품));
    private static final MenuGroup 메뉴그룹 = MenuGroup.create("그룹이름");


    @Test
    void 메뉴의_가격이_메뉴에_속한_상품들의_가격_총합과_같으면_메뉴를_생성한다() {

        Assertions.assertDoesNotThrow(() -> Menu.create("메뉴이름", new BigDecimal(115), 메뉴그룹, 총가격_115인_상품들));
    }

    @Test
    void 메뉴의_가격이_메뉴에_속한_상품들의_가격_총합보다_작으면_메뉴를_생성한다() {
        Assertions.assertDoesNotThrow(() -> Menu.create("메뉴이름", new BigDecimal(114), 메뉴그룹, 총가격_115인_상품들));
    }

    @Test
    void 메뉴의_가격이_메뉴에_속한_상품들의_가격_총합보다_크면_예외가_발생한다() {
        assertThatThrownBy(() -> Menu.create("메뉴이름", new BigDecimal(116), 메뉴그룹, 총가격_115인_상품들))
                .isExactlyInstanceOf(KitchenposException.class)
                .hasMessage(MENU_PRICE_OVER_MENU_PRODUCT_PRICE.getMessage());
    }
}

package kitchenpos.menu.domain;

import kitchenpos.BaseTest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductName;
import kitchenpos.product.domain.ProductPrice;
import kitchenpos.menu.exception.MenuException;
import kitchenpos.menu.domain.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.List;

class MenuTest extends BaseTest {

    @ParameterizedTest
    @CsvSource(value = {"100, 10, 200, 5, 1800", "10, 1, 10, 5, 50", "3, 3, 2, 2, 13", "0, 0, 0, 0, 0"})
    void 메뉴가격은_모든_메뉴_상품의_가격의_총합보다_같거나_낮아야한다(
            BigDecimal firstProductPrice, Long firstProductQuantity,
            BigDecimal secondProductPrice, Long secondProductQuantity,
            BigDecimal menuPrice) {
        // given
        Product firstProduct = new Product(
                new ProductName("chicken"),
                new ProductPrice(firstProductPrice));
        Product secondProduct = new Product(
                new ProductName("chicken"),
                new ProductPrice(secondProductPrice));

        MenuProductQuantity firstQuantity = new MenuProductQuantity(firstProductQuantity);
        MenuProductQuantity secondQuantity = new MenuProductQuantity(secondProductQuantity);

        MenuProduct firstMenuProduct = new MenuProduct(firstProduct, firstQuantity);
        MenuProduct secondMenuProduct = new MenuProduct(secondProduct, secondQuantity);
        List<MenuProduct> menuProducts = List.of(firstMenuProduct, secondMenuProduct);

        Menu menu = new Menu(
                new MenuName("테스트메뉴"),
                new MenuPrice(menuPrice),
                new MenuGroup(1L, new MenuGroupName("테스트메뉴그룹")));

        // when, then
        Assertions.assertThatNoException()
                .isThrownBy(() -> menu.addMenuProducts(menuProducts));
    }

    @ParameterizedTest
    @CsvSource(value = {"100, 10, 200, 5, 2100", "10, 1, 10, 5, 1000", "3, 3, 2, 2, 14", "0, 0, 0, 0, 1"})
    void 메뉴가격은_모든_메뉴_상품의_가격의_총합보다_높으면_예외를_던진다(
            BigDecimal firstProductPrice, Long firstProductQuantity,
            BigDecimal secondProductPrice, Long secondProductQuantity,
            BigDecimal menuPrice) {

        // given
        Product firstProduct = new Product(
                new ProductName("chicken"),
                new ProductPrice(firstProductPrice));
        Product secondProduct = new Product(
                new ProductName("chicken"),
                new ProductPrice(secondProductPrice));

        MenuProductQuantity firstQuantity = new MenuProductQuantity(firstProductQuantity);
        MenuProductQuantity secondQuantity = new MenuProductQuantity(secondProductQuantity);

        MenuProduct firstMenuProduct = new MenuProduct(firstProduct, firstQuantity);
        MenuProduct secondMenuProduct = new MenuProduct(secondProduct, secondQuantity);
        List<MenuProduct> menuProducts = List.of(firstMenuProduct, secondMenuProduct);

        Menu menu = new Menu(
                new MenuName("테스트메뉴"),
                new MenuPrice(menuPrice),
                new MenuGroup(1L, new MenuGroupName("테스트메뉴그룹")));

        // when, then
        Assertions.assertThatThrownBy(() -> menu.addMenuProducts(menuProducts))
                .isInstanceOf(MenuException.class);
    }
}

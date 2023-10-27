package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MenuTest {

    @CsvSource(value = {"10000,1000,1", "10000,1000,9", "1000,100,8"})
    @ParameterizedTest(name = "메뉴의 가격이 {0} 이고, 상품의 가격이 {1} 원, 상품의 개수가 {2} 개 일때 예외")
    void 메뉴의_가격은_메뉴_상품들의_총_가격보다_클_수_없다(final long menuPrice, final long productPrice, final long quantity) {
        // given
        final Menu menu = new Menu("name",
                                   Price.from(menuPrice),
                                   new MenuGroup("name").getId(),
                                   null);

        // when
        final Product product = new Product("product", productPrice);
        final MenuProduct menuProduct = new MenuProduct(quantity, menu, product);

        // then
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                                                        () -> menu.addMenuProducts(List.of(menuProduct)));
        assertThat(e).hasMessage("메뉴의 가격은 메뉴 상품들의 총 가격보다 클 수 없습니다.");
    }
}

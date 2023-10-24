package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("NonAsciiCharacters")
public class MenuFixture {

    private static final long DEFAULT_QUANTITY = 1L;

    private static Menu 메뉴_생성(final MenuGroup menuGroup, final List<Product> products, @Nullable final Integer number) {
        String 메뉴_이름 = "메뉴";
        if (number != null) {
            메뉴_이름 += number.toString();
        }

        final Menu 메뉴 = new Menu(메뉴_이름, 메뉴_가격_계산(products), menuGroup);
        final List<MenuProduct> 메뉴_상품들 = 메뉴_상품들_생성(products, 메뉴);
        메뉴.updateMenuProducts(메뉴_상품들);

        return 메뉴;
    }

    public static Menu 메뉴_생성(final MenuGroup menuGroup, final List<Product> products) {
        return 메뉴_생성(menuGroup, products, null);
    }

    public static List<Menu> 메뉴들_생성(final int count, final MenuGroup menuGroup, final List<Product> products) {
        final List<Menu> 메뉴들 = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            메뉴들.add(메뉴_생성(menuGroup, products, i));
        }

        return 메뉴들;
    }

    private static BigDecimal 메뉴_가격_계산(final List<Product> products) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final Product product : products) {
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(DEFAULT_QUANTITY)));
        }

        return sum;
    }

    private static List<MenuProduct> 메뉴_상품들_생성(final List<Product> products, final Menu menu) {
        return products.stream()
                       .map(product -> 메뉴_상품_생성(product, menu))
                       .collect(Collectors.toList());
    }

    private static MenuProduct 메뉴_상품_생성(final Product product, final Menu menu) {
        final MenuProduct menuProduct = new MenuProduct(product, DEFAULT_QUANTITY);
        menuProduct.updateMenu(menu);

        return menuProduct;
    }
}

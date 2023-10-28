package kitchenpos.fixture;

import kitchenpos.common.vo.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.ui.dto.MenuProductDto;
import kitchenpos.menu.ui.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("NonAsciiCharacters")
public class MenuFixture {

    public static final String 메뉴명 = "메뉴";
    private static final long DEFAULT_QUANTITY = 1L;

    private static Menu 메뉴_엔티티_생성(final MenuGroup menuGroup, final List<Product> products, @Nullable final Integer number) {
        String 메뉴_이름 = 메뉴명;
        if (number != null) {
            메뉴_이름 += number.toString();
        }

        final MenuProducts 메뉴_상품들 = 메뉴_상품_엔티티들_생성(products);
        final Menu 메뉴 = Menu.of(메뉴_이름, new Price(메뉴_가격_계산(products)), menuGroup.getId(), 메뉴_상품들);

        return 메뉴;
    }

    public static Menu 메뉴_엔티티_생성(final MenuGroup menuGroup, final List<Product> products) {
        return 메뉴_엔티티_생성(menuGroup, products, null);
    }

    public static List<Menu> 메뉴_엔티티들_생성(final int count, final MenuGroup menuGroup, final List<Product> products) {
        final List<Menu> 메뉴들 = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            메뉴들.add(메뉴_엔티티_생성(menuGroup, products, i));
        }

        return 메뉴들;
    }

    private static BigDecimal 메뉴_가격_계산(final List<Product> products) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final Product product : products) {
            sum = sum.add(product.getPriceValue().multiply(BigDecimal.valueOf(DEFAULT_QUANTITY)));
        }

        return sum;
    }

    private static MenuProducts 메뉴_상품_엔티티들_생성(final List<Product> products) {
        final List<MenuProduct> menuProducts = products.stream()
                                                       .map(product -> 메뉴_상품_엔티티_생성(product))
                                                       .collect(Collectors.toList());
        return new MenuProducts(menuProducts);
    }

    private static MenuProduct 메뉴_상품_엔티티_생성(final Product product) {
        final MenuProduct menuProduct = new MenuProduct(product.getId(), DEFAULT_QUANTITY);

        return menuProduct;
    }

    private static MenuRequest 메뉴_요청_dto_생성(final MenuGroup menuGroup, final List<Product> products, @Nullable final Integer number) {
        String 메뉴_이름 = 메뉴명;
        if (number != null) {
            메뉴_이름 += number.toString();
        }

        final List<MenuProductDto> 메뉴_상품_요청_dto들 = 메뉴_상품_요청_dto들로_변환(products);
        final MenuRequest 메뉴_요청 = new MenuRequest(메뉴_이름, 메뉴_가격_계산(products), menuGroup.getId(), 메뉴_상품_요청_dto들);

        return 메뉴_요청;
    }

    public static MenuRequest 메뉴_요청_dto_생성(final MenuGroup menuGroup, final List<Product> products) {
        return 메뉴_요청_dto_생성(menuGroup, products, null);
    }

    private static List<MenuProductDto> 메뉴_상품_요청_dto들로_변환(final List<Product> products) {
        return products.stream()
                       .map(product -> new MenuProductDto(product.getId(), DEFAULT_QUANTITY))
                       .collect(Collectors.toList());
    }
}

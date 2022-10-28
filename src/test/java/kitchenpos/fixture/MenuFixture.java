package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;

@SuppressWarnings("NonAsciiCharacters")
public enum MenuFixture {

    후라이드_양념치킨_두마리세트("후라이드, 양념치킨 두마리 세트", new BigDecimal(30_000));

    private final String name;
    private final BigDecimal price;

    MenuFixture(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public MenuRequest toRequest(Long menuGroupId, Long... productIds) {
        List<MenuProductRequest> menuProductRequests = Arrays.stream(productIds)
                .map(productId -> new MenuProductRequest(productId, 1L))
                .collect(Collectors.toList());

        return new MenuRequest(name, price, menuGroupId, menuProductRequests);
    }

    public Menu toMenu(Long menuGroupId, Long... productIds) {
        Menu menu = new Menu(null, name, price, menuGroupId);
        addMenuProducts(menu, productIds);
        return menu;
    }

    private static void addMenuProducts(Menu menu, Long[] productIds) {
        for (Long productId : productIds) {
            menu.addMenuProduct(new MenuProduct(productId, 1));
        }
    }
}

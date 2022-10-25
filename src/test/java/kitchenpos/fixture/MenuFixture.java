package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

@SuppressWarnings("NonAsciiCharacters")
public enum MenuFixture {

    후라이드_양념치킨_두마리세트("후라이드, 양념치킨 두마리 세트", new BigDecimal(30_000));

    private final String name;
    private final BigDecimal price;

    MenuFixture(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Menu toMenu(Long menuGroupId, Long... productIds) {
        return new Menu(name, price, menuGroupId, createMenuProducts(productIds));
    }

    private List<MenuProduct> createMenuProducts(Long... productIds) {
        return Arrays.stream(productIds)
                .map(id -> new MenuProduct(id, 1))
                .collect(Collectors.toList());
    }
}

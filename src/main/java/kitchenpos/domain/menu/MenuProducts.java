package kitchenpos.domain.menu;

import static javax.persistence.CascadeType.PERSIST;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.domain.vo.Price;

@Embeddable
public class MenuProducts {

    private static final int MENU_PRODUCT_SIZE_MINIMUM = 1;

    @OneToMany(mappedBy = "menu", cascade = PERSIST)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> menuProducts) {
        validateMenuProducts(menuProducts);
        this.menuProducts = new ArrayList<>(menuProducts);
    }

    public void setMenu(final Menu menu) {
        menuProducts.forEach(menuProduct -> menuProduct.register(menu));
    }

    private void validateMenuProducts(final List<MenuProduct> menuProducts) {
        if (menuProducts.size() < MENU_PRODUCT_SIZE_MINIMUM) {
            throw new IllegalArgumentException("메뉴 상품의 최소 개수는 " + MENU_PRODUCT_SIZE_MINIMUM + "개입니다.");
        }
    }

    public Price totalPrice() {
        final List<Price> prices = menuProducts.stream()
                .map(MenuProduct::totalPrice)
                .collect(Collectors.toList());
        return Price.sum(prices);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public Map<Product, Long> getQuantityByProduct() {
        return menuProducts.stream()
                .collect(Collectors.toMap(MenuProduct::getProduct, MenuProduct::totalPriceToLong));
    }
}

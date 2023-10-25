package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    private MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public static MenuProducts of(List<MenuProduct> menuProducts, Price price) {
        validate(menuProducts, price);
        return new MenuProducts(menuProducts);
    }

    private static void validate(final List<MenuProduct> menuProducts, Price price) {
        if (price.compareTo(sum(menuProducts)) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 메뉴 상품들의 가격보다 저렴해야 합니다.");
        }
    }

    private static Price sum(final List<MenuProduct> menuProducts) {
        Price sum = Price.from(BigDecimal.ZERO);

        for (MenuProduct menuProduct : menuProducts) {
            final Product product = menuProduct.getProduct();
            final long quantity = menuProduct.getQuantity();
            sum = sum.add(product.getPrice().multiply(quantity));
        }

        return sum;
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }
}

package kitchenpos.domain.menu;

import kitchenpos.domain.common.Money;
import kitchenpos.domain.product.Product;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Embeddable
public class MenuProducts {
    @OneToMany
    @JoinColumn(name = "menu_id")
    private List<MenuProduct> menuProducts;

    protected MenuProducts() {
    }

    private MenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public static MenuProducts from(List<Product> products, List<Long> quantities) {
        if (products.size() != quantities.size()) {
            throw new IllegalArgumentException("상품 개수와 상품 수량의 개수가 일치하지 않습니다.");
        }
        return new MenuProducts(IntStream.range(0, products.size())
                .mapToObj(i -> MenuProduct.of(products.get(i), quantities.get(i)))
                .collect(Collectors.toList()));
    }

    public Money calculateSum() {
        return menuProducts.stream()
                .map(MenuProduct::getProduct)
                .map(Product::getPrice)
                .reduce(Money.ZERO, Money::add);
    }

    public List<MenuProduct> getMenuProducts() {
        return new ArrayList<>(menuProducts);
    }
}

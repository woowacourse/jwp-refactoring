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
    private List<MenuProduct> items;

    protected MenuProducts() {
    }

    private MenuProducts(final List<MenuProduct> items) {
        this.items = items;
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
        return items.stream()
                .map(MenuProduct::getProduct)
                .map(Product::getPrice)
                .reduce(Money.ZERO, Money::add);
    }

    public List<MenuProduct> getItems() {
        return new ArrayList<>(items);
    }
}

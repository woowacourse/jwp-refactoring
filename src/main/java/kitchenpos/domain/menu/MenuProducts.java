package kitchenpos.domain.menu;

import kitchenpos.domain.common.Money;
import kitchenpos.domain.product.Product;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Embeddable
public class MenuProducts {
    public static final String PRODUCTS_SIZE_AND_QUANTITIES_SIZE_ARE_DIFFERENT_ERROR_MESSAGE = "상품 개수와 상품 수량의 개수가 일치하지 않습니다.";

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id")
    private List<MenuProduct> items;

    protected MenuProducts() {
    }

    private MenuProducts(final List<MenuProduct> items) {
        this.items = items;
    }

    public static MenuProducts from(List<Product> products, List<Long> quantities) {
        if (products.size() != quantities.size()) {
            throw new IllegalArgumentException(PRODUCTS_SIZE_AND_QUANTITIES_SIZE_ARE_DIFFERENT_ERROR_MESSAGE);
        }
        return new MenuProducts(IntStream.range(0, products.size())
                .mapToObj(i -> MenuProduct.of(products.get(i), quantities.get(i)))
                .collect(Collectors.toList()));
    }

    public static MenuProducts empty() {
        return new MenuProducts(new ArrayList<>());
    }

    public void setMenu(final Menu menu) {
        items.forEach(menuProduct -> menuProduct.setMenu(menu));
    }

    public Money calculateSum() {
        return items.stream()
                .map(MenuProduct::getProduct)
                .map(Product::getPrice)
                .reduce(Money.ZERO, Money::add);
    }

    public int size() {
        return items.size();
    }

    public List<MenuProduct> getItems() {
        return new ArrayList<>(items);
    }
}

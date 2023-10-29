package kitchenpos.menu.domain;

import kitchenpos.common.domain.Money;
import kitchenpos.common.domain.Quantity;
import kitchenpos.product.domain.Product;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Embeddable
public class MenuProducts {
    public static final String PRODUCTS_SIZE_AND_QUANTITIES_SIZE_ARE_DIFFERENT_ERROR_MESSAGE = "상품 개수와 상품 수량의 개수가 일치하지 않습니다.";

    @NotNull
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
                .mapToObj(i -> MenuProduct.of(products.get(i), Quantity.of(quantities.get(i))))
                .collect(Collectors.toList()));
    }

    public Money calculateSum() {
        return items.stream()
                .map(this::multiplyPriceByQuantity)
                .reduce(Money.ZERO, Money::add);
    }

    private Money multiplyPriceByQuantity(final MenuProduct menuProduct) {
        final Money price = menuProduct.getProduct().getPrice();
        final long quantity = menuProduct.getQuantity().getQuantity();
        return price.multiply(quantity);
    }

    public int size() {
        return items.size();
    }

    public List<MenuProduct> getItems() {
        return new ArrayList<>(items);
    }
}

package kitchenpos.menu.domain;

import kitchenpos.domain.Price;
import kitchenpos.product.domain.Product;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "menu_id", nullable = false, updatable = false)
    private List<MenuProduct> items;

    protected MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> items) {
        this.items = items;
    }

    public boolean checkMenuProductsPriceIsMoreThan(final Price price) {
        Price sum = new Price(BigDecimal.ZERO);
        for (final MenuProduct menuProduct : items) {
            final Product product = menuProduct.getProduct();
            sum = sum.add(product.getPrice().multiply(menuProduct.getQuantity()));
        }

        return price.isMoreThan(sum);
    }

    public List<MenuProduct> getItems() {
        return items;
    }
}

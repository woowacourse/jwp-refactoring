package kitchenpos.menuproduct.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void validateTotalPrice(final BigDecimal price) {
        System.out.println(calculatedTotalPrice());
        if (!Objects.equals(calculatedTotalPrice(), price)) {
            throw new IllegalArgumentException();
        }
    }

    private BigDecimal calculatedTotalPrice() {
        return menuProducts.stream()
                .map(MenuProduct::calculatedPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}

package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "menu")
    private List<MenuProduct> values = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> values) {
        this.values = values;
    }

    public BigDecimal calculateMenuPrice() {
        BigDecimal price = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : values) {
            price = price.add(menuProduct.calculateTotalPrice());
        }
        return price;
    }

    public List<MenuProduct> getValues() {
        return values;
    }
}

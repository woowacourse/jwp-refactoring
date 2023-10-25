package kitchenpos.domain;

import kitchenpos.domain.vo.Price;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;

public class MenuProducts {

    @OneToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true, mappedBy = "menu")
    @Column(name = "menuProducts")
    private final List<MenuProduct> values;

    public MenuProducts(final List<MenuProduct> values) {
        this.values = values;
    }

    public Price calculateTotalPrice() {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct value : values) {
            final BigDecimal price = value.getProductPrice()
                    .multiply(BigDecimal.valueOf(value.getQuantity()));
            sum = sum.add(price);
        }
        return Price.of(sum);
    }

    public List<MenuProduct> getValues() {
        return values;
    }
}

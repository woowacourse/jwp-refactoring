package kitchenpos.domain.menu;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", updatable = false)
    private List<MenuProduct> values;

    public MenuProducts(final List<MenuProduct> menuProducts) {
        this.values = menuProducts;
    }

    public MenuProducts() {
        this(Collections.emptyList());
    }

    public List<MenuProduct> getValues() {
        return Collections.unmodifiableList(values);
    }

    public BigDecimal calculateTotalPrice() {
        return values.stream()
                .map(MenuProduct::calculatePriceMultiplyQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

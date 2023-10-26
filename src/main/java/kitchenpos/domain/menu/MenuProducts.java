package kitchenpos.domain.menu;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
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

    public void updateMenu(final Menu menu) {
        values.forEach(menuProduct -> menuProduct.setMenu(menu));
    }
}

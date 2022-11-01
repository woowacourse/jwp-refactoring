package kitchenpos.domain.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<MenuProduct> values;

    public MenuProducts(final List<MenuProduct> values) {
        this.values = new ArrayList<>(values);
    }

    public Price calculateTotalPrice() {
        final List<Price> prices = values.stream()
                .map(MenuProduct::calculatePrice)
                .collect(Collectors.toList());
        return Price.sum(prices);
    }

    public List<MenuProduct> getValues() {
        return Collections.unmodifiableList(values);
    }

    protected MenuProducts() {
    }
}

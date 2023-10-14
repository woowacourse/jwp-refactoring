package kitchenpos.domain;

import static javax.persistence.CascadeType.PERSIST;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(fetch = FetchType.EAGER, cascade = {PERSIST})
    @JoinColumn(name = "menu_id", nullable = false, updatable = false)
    private List<MenuProduct> items = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> items) {
        this.items = items;
    }

    public BigDecimal calculateAmount() {
        return items.stream()
                .map(MenuProduct::calculateAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<MenuProduct> getItems() {
        return new ArrayList<>(items);
    }
}

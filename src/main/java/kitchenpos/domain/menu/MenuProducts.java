package kitchenpos.domain.menu;

import static javax.persistence.CascadeType.PERSIST;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.support.money.Money;

@Embeddable
public class MenuProducts {

    @OneToMany(fetch = FetchType.EAGER, cascade = PERSIST)
    @JoinColumn(name = "menu_id", nullable = false, updatable = false)
    private List<MenuProduct> items = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> items) {
        this.items = items;
    }

    public Money calculateAmount() {
        return Money.sum(items, MenuProduct::calculateAmount);
    }

    public List<MenuProduct> getItems() {
        return new ArrayList<>(items);
    }
}

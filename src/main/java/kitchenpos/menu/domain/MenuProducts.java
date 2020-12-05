package kitchenpos.menu.domain;

import kitchenpos.generic.Price;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "MENU_ID")
    private final List<MenuProduct> menuProducts = new ArrayList<>();

    public MenuProducts() {
    }

    public Price calculateSum() {
        return menuProducts.stream()
                .map(MenuProduct::calculateSum)
                .reduce(Price.of(0L), Price::add);
    }

    public void addAll(List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }
}

package kitchenpos.domain;

import kitchenpos.domain.vo.Price;
import org.hibernate.annotations.BatchSize;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    @BatchSize(size = 100)
    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true, mappedBy = "menu")
    private List<MenuProduct> menuProductItems = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> menuProductItems) {
        this.menuProductItems = menuProductItems;
    }

    public static MenuProducts empty() {
        return new MenuProducts(new ArrayList<>());
    }

    public Price getTotalPrice() {
        return menuProductItems.stream()
                .map(MenuProduct::getTotalPrice)
                .reduce(Price::sum)
                .orElse(Price.ZERO);
    }

    public void addAll(final MenuProducts requestMenuProducts) {
        menuProductItems.addAll(requestMenuProducts.getMenuProductItems());
    }

    public List<MenuProduct> getMenuProductItems() {
        return menuProductItems;
    }
}

package kitchenpos.menu.domain;

import kitchenpos.menu.domain.vo.Price;
import org.hibernate.annotations.BatchSize;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    @BatchSize(size = 100)
    @JoinColumn(name = "menu_id", nullable = false, foreignKey = @ForeignKey(name = "fk_menu_product_to_menu"))
    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
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

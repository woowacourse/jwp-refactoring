package kitchenpos.domain;

import kitchenpos.domain.vo.Price;
import org.hibernate.annotations.BatchSize;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    @BatchSize(size = 10)
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
                .map(Price::getValue)
                .reduce(BigDecimal::add)
                .map(Price::new)
                .orElse(Price.ZERO);
    }

    public void add(final Menu menu, final List<MenuProduct> otherMenuProducts) {
        otherMenuProducts.stream()
                .map(menuProductWithoutMenu -> new MenuProduct(
                        menu,
                        menuProductWithoutMenu.getProduct(),
                        menuProductWithoutMenu.getQuantity()
                )).forEach(menuProductWithMenu -> menuProductItems.add(menuProductWithMenu));
    }

    public List<MenuProduct> getMenuProductItems() {
        return menuProductItems;
    }
}

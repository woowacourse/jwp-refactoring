package kitchenpos.menu.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;

import static javax.persistence.CascadeType.PERSIST;


@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = PERSIST, orphanRemoval = true)
    private List<MenuProduct> menuProducts;

    protected MenuProducts() {

    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void setMenu(Menu menu) {
        menuProducts.forEach(menuProduct -> menuProduct.setMenu(menu));
    }

    public BigDecimal calculateSum() {
        return menuProducts.stream()
                .map(menuProduct -> menuProduct.getProduct().getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void add(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}

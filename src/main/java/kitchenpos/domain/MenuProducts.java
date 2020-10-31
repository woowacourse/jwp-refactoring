package kitchenpos.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts;

    protected MenuProducts() { }

    public MenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}

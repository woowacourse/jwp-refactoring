package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu",
            cascade = CascadeType.PERSIST)
    private List<MenuProduct> values = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(Menu menu, List<MenuProduct> menuProducts) {
        this.values.addAll(menuProducts);

        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.mapMenu(menu);
        }
    }

    public void addAll(List<MenuProduct> menuProducts) {
        this.values.addAll(menuProducts);
    }

    public List<MenuProduct> getValues() {
        return values;
    }
}

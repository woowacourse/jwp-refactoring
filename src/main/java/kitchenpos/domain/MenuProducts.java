package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id")
    private List<MenuProduct> values = new ArrayList<>();

    public MenuProducts(final List<MenuProduct> menuProducts) {
        values.addAll(menuProducts);
    }

    protected MenuProducts() {
    }

    public void addAll(final MenuProducts menuProducts) {
        values.addAll(menuProducts.getValues());
    }

    public int size() {
        return values.size();
    }

    public List<MenuProduct> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "MenuProducts{" +
                "values=" + values +
                '}';
    }
}

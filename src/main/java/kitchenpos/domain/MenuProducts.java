package kitchenpos.domain;

import java.math.BigDecimal;
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
    private final List<MenuProduct> elements;

    public MenuProducts() {
        this(new ArrayList<>());
    }

    public MenuProducts(final List<MenuProduct> elements) {
        this.elements = elements;
    }

    public void add(final MenuProduct menuProduct) {
        elements.add(menuProduct);
    }

    public Price totalPrice() {
        return elements.stream()
            .map(MenuProduct::getPrice)
            .reduce(new Price(BigDecimal.ZERO), Price::add);
    }

    public List<MenuProduct> getElements() {
        return elements;
    }
}

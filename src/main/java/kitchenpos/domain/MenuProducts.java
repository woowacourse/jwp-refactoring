package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
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

    public BigDecimal totalPrice() {
        return elements.stream()
            .map(element -> element
                .getProduct()
                .getPrice()
                .getValue()
            ).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<MenuProduct> getElements() {
        return elements;
    }
}

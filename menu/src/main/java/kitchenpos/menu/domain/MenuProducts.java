package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts implements Iterable<MenuProduct> {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "menu_id")
    private final List<MenuProduct> elements;

    public MenuProducts() {
        this(new ArrayList<>());
    }

    public MenuProducts(final List<MenuProduct> elements) {
        this.elements = elements;
    }

    public List<MenuProduct> getElements() {
        return elements;
    }

    @Override
    public Iterator<MenuProduct> iterator() {
        return elements.iterator();
    }
}

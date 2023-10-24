package kitchenpos.domain.menu;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    @JoinColumn(name = "menu_id", nullable = false, updatable = false)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MenuProduct> collection;

    public MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> collection) {
        this.collection = collection;
    }

    public BigDecimal calculateTotalPrice() {
        return collection.stream()
                .map(MenuProduct::calculateTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<MenuProduct> getCollection() {
        return new ArrayList<>(collection);
    }
}

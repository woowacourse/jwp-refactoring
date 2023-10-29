package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class MenuSnapshot {
    
    private String name;
    
    private BigDecimal price;
    
    @ElementCollection
    @CollectionTable(
            name = "menu_product_snapshots",
            joinColumns = @JoinColumn
    )
    private List<MenuProductSnapshot> menuProductSnapshots = new ArrayList<>();
    
    protected MenuSnapshot() {
    }
    
    public static MenuSnapshot from(final Menu menu) {
        return new MenuSnapshot(
                menu.getName(),
                menu.getPrice().getPrice(),
                menu.getMenuProducts().stream()
                    .map(MenuProductSnapshot::from)
                    .collect(Collectors.toList()));
    }
    
    public MenuSnapshot(final String name,
                        final BigDecimal price,
                        final List<MenuProductSnapshot> menuProductSnapshots) {
        this.name = name;
        this.price = price;
        this.menuProductSnapshots = menuProductSnapshots;
    }
    
    public String getName() {
        return name;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public List<MenuProductSnapshot> getMenuProductSnapshots() {
        return menuProductSnapshots;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuSnapshot that = (MenuSnapshot) o;
        return name.equals(that.name)
                && price.equals(that.price)
                && menuProductSnapshots.equals(that.menuProductSnapshots);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, price, menuProductSnapshots);
    }
}

package kitchenpos.order.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import kitchenpos.menu.domain.Menu;

@Embeddable
public class MenuSnapShot {

    private String menuGroupName;
    private String name;
    private BigDecimal price;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "menu_product_snapshot", joinColumns = @JoinColumn)
    private List<MenuProductSnapShot> menuProducts;

    public MenuSnapShot() {
    }

    public MenuSnapShot(String menuGroupName, String name, BigDecimal price, List<MenuProductSnapShot> menuProducts) {
        this.menuGroupName = menuGroupName;
        this.name = name;
        this.price = price;
        this.menuProducts = menuProducts;
    }

    public static MenuSnapShot from(Menu menu) {
        return new MenuSnapShot(
                menu.getMenuGroup().getName(),
                menu.getName(),
                menu.getPrice(),
                menu.getMenuProducts().stream()
                        .map(MenuProductSnapShot::from)
                        .collect(Collectors.toList())
        );
    }

    public String getMenuGroupName() {
        return menuGroupName;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public List<MenuProductSnapShot> getMenuProducts() {
        return menuProducts;
    }
}

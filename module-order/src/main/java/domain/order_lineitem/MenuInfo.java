package domain.order_lineitem;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;

@Embeddable
public class MenuInfo {

    private final String menuName;
    private final String menuGroupName;
    private final BigDecimal price;
    @ElementCollection
    @CollectionTable(name = "menu_product_info", joinColumns = @JoinColumn)
    private final List<MenuProductInfo> menuProductInfo;

    protected MenuInfo() {
        this.menuName = null;
        this.menuGroupName = null;
        this.price = null;
        this.menuProductInfo = null;
    }

    public MenuInfo(
            final String menuName,
            final String menuGroupName,
            final BigDecimal price,
            final List<MenuProductInfo> menuProductInfo
    ) {
        this.menuName = menuName;
        this.menuGroupName = menuGroupName;
        this.price = price;
        this.menuProductInfo = menuProductInfo;
    }

    public String getMenuName() {
        return menuName;
    }

    public String getMenuGroupName() {
        return menuGroupName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public List<MenuProductInfo> getMenuProductInfo() {
        return menuProductInfo;
    }
}

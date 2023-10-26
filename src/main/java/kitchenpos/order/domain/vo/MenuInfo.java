package kitchenpos.order.domain.vo;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import kitchenpos.menu.domain.Menu;

@Embeddable
public class MenuInfo {

    private String menuName;

    private BigDecimal menuPrice;

    @ElementCollection
    @CollectionTable(name = "menu_product_info", joinColumns = @JoinColumn(name = "order_line_item_seq"))
    private List<MenuProductInfo> menuProductInfos;

    protected MenuInfo() {
    }

    private MenuInfo(final String menuName,
                     final BigDecimal menuPrice,
                     final List<MenuProductInfo> menuProductInfos) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuProductInfos = menuProductInfos;
    }

    public static MenuInfo from(final Menu menu) {
        return new MenuInfo(menu.getName(),
                            menu.getPrice().getValue(),
                            MenuProductInfo.from(menu.getMenuProducts()));
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }

    public List<MenuProductInfo> getMenuProductInfos() {
        return menuProductInfos;
    }
}

package kitchenpos.menu;

import kitchenpos.product.Price;
import kitchenpos.product.PriceConverter;

import javax.persistence.Convert;
import javax.persistence.Embeddable;

@Embeddable
public class MenuHistory {

    private Long menuId;

    private String menuName;

    @Convert(converter = PriceConverter.class)
    private Price menuPrice;

    public MenuHistory() {
    }

    public MenuHistory(Long menuId, String menuName, Price menuPrice) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public Price getMenuPrice() {
        return menuPrice;
    }
}

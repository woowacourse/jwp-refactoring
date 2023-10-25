package kitchenpos.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class MenuSnapShot {

    private Long menuId;

    private String originalMenuName;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "original_menu_price"))
    private Price originalMenuPrice;

    private Long originalMenuGroupId;

    public MenuSnapShot() {
    }

    public MenuSnapShot(final Long menuId, final String originalMenuName, final Price originalMenuPrice,
                        final Long originalMenuGroupId) {
        this.menuId = menuId;
        this.originalMenuName = originalMenuName;
        this.originalMenuPrice = originalMenuPrice;
        this.originalMenuGroupId = originalMenuGroupId;
    }

    public static MenuSnapShot make(final Menu menu) {
        return new MenuSnapShot(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroup().getId());
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getOriginalMenuName() {
        return originalMenuName;
    }

    public Price getOriginalMenuPrice() {
        return originalMenuPrice;
    }

    public Long getOriginalMenuGroupId() {
        return originalMenuGroupId;
    }
}

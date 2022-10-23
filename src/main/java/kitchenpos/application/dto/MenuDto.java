package kitchenpos.application.dto;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import lombok.Getter;

@Getter
public class MenuDto {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProduct> menuProducts;

    public MenuDto(Long id,
                   String name,
                   BigDecimal price,
                   Long menuGroupId,
                   List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuDto of(Menu menu, List<MenuProduct> menuProducts) {
        return new MenuDto(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(), menuProducts);
    }
}

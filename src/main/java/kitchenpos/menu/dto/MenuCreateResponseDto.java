package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuCreateResponseDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<Long> menuProductIds;

    public MenuCreateResponseDto() {
    }

    public MenuCreateResponseDto(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.menuGroupId = menu.getMenuGroupId();
        List<Long> ids = new ArrayList<>();
        List<MenuProduct> menuProducts = menu.getMenuProducts();
        for (MenuProduct menuProduct : menuProducts) {
            ids.add(menuProduct.getSeq());
        }
        this.menuProductIds = ids;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<Long> getMenuProductIds() {
        return menuProductIds;
    }
}

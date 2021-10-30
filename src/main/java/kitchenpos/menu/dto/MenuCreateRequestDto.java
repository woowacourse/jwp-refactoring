package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;

public class MenuCreateRequestDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductDto> menuProducts = new ArrayList<>();

    public MenuCreateRequestDto() {
    }

    public MenuCreateRequestDto(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.menuGroupId = menu.getMenuGroupId();
        for (MenuProduct menuProduct : menu.getMenuProducts()) {
            menuProducts.add(new MenuProductDto(menuProduct));
        }
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

    public List<MenuProductDto> getMenuProducts() {
        return menuProducts;
    }

    public Menu toEntity() {
        List<MenuProduct> menuProductGroup = new ArrayList<>();
        for (MenuProductDto menuProductDto : menuProducts) {
            menuProductGroup.add(new MenuProduct(menuProductDto.getProductId(), menuProductDto.getQuantity()));
        }
        Menu menu = new Menu(id, name, price, menuGroupId, new MenuProducts(menuProductGroup));
        return menu;
    }
}

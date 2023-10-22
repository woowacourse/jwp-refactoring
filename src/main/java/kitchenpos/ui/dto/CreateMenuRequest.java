package kitchenpos.ui.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class CreateMenuRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<CreateMenuProductRequest> menuProducts;

    public CreateMenuRequest() {
    }

    public CreateMenuRequest(final String name, final BigDecimal price, final Long menuGroupId, final List<CreateMenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

    public List<CreateMenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public Menu toEntity() {
        final Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        final List<MenuProduct> menuProducts = this.menuProducts.stream()
                                                                .map(CreateMenuProductRequest::toEntity)
                                                                .collect(toList());
        menu.setMenuProducts(menuProducts);

        return menu;
    }
}

package kitchenpos.dto.menu;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.List;

public class MenuRequest {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest(
            Long id,
            String name,
            BigDecimal price,
            Long menuGroupId,
            List<MenuProductRequest> menuProducts
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public List<Long> getMenuProductIds() {
        return menuProducts.stream()
                .map(MenuProductRequest::getProductId)
                .collect(toList());
    }
}

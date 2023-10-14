package kitchenpos.dto;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse(
            Long id,
            String name,
            BigDecimal price,
            Long menuGroupId,
            List<MenuProductResponse> menuProducts
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(Menu menu) {
        List<MenuProductResponse> menuProductResponses = menu.getMenuProducts().stream()
                .map(MenuProductResponse::from)
                .collect(toList());
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getMenuGroupId(),
                menuProductResponses
        );
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

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}

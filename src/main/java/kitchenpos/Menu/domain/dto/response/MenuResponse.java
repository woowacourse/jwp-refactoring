package kitchenpos.Menu.domain.dto.response;

import kitchenpos.Menu.domain.Menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    protected MenuResponse() {
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

    public static MenuResponse toDTO(Menu menu) {
        List<MenuProductResponse> menuProductResponses = menu.getMenuProducts().stream()
                .map(MenuProductResponse::toDTO)
                .collect(Collectors.toList());

        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(),
                menu.getMenuGroupId(), menuProductResponses);
    }
}

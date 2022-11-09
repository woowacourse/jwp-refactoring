package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProductResponses;

    public MenuResponse(String name, BigDecimal price, Long menuGroupId,
                        List<MenuProductResponse> menuProductResponses) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductResponses = menuProductResponses;
    }

    public MenuResponse(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice().getValue();
        this.menuGroupId = menu.getMenuGroupId();
        this.menuProductResponses = menu.getProducts()
                .stream()
                .map(MenuProductResponse::new)
                .collect(Collectors.toUnmodifiableList());
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
        return menuProductResponses;
    }
}

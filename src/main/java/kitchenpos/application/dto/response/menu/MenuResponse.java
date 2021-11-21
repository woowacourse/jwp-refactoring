package kitchenpos.application.dto.response.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroupResponse menuGroupResponse;
    private List<MenuProductResponse> menuProductResponses;

    public MenuResponse(Long id, String name, BigDecimal price, MenuGroupResponse menuGroup,
            List<MenuProductResponse> menuProductResponses) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupResponse = menuGroup;
        this.menuProductResponses = menuProductResponses;
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

    public MenuGroupResponse getMenuGroupResponse() {
        return menuGroupResponse;
    }

    public List<MenuProductResponse> getMenuProductResponses() {
        return menuProductResponses;
    }

    public static MenuResponse create(Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                MenuGroupResponse.create(menu.getMenuGroup()),
                menu.getMenuProducts().stream()
                        .map(MenuProductResponse::create)
                        .collect(Collectors.toList())
        );
    }
}

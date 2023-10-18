package kitchenpos.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;

public class MenuResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final MenuGroupResponse menuGroupResponse;
    private final List<MenuProductResponse> menuProductResponses;

    private MenuResponse(
            Long id,
            String name,
            BigDecimal price,
            MenuGroupResponse menuGroupResponse,
            List<MenuProductResponse> menuProductResponses
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupResponse = menuGroupResponse;
        this.menuProductResponses = menuProductResponses;
    }

    public static MenuResponse from(Menu menu) {
        MenuGroupResponse menuGroupResponse = MenuGroupResponse.from(menu.getMenuGroup());
        List<MenuProductResponse> menuProductResponses = menu.getMenuProducts()
                .stream()
                .map(MenuProductResponse::from)
                .collect(Collectors.toList());

        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menuGroupResponse, menuProductResponses);
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
}

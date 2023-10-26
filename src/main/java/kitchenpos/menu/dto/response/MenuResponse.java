package kitchenpos.menu.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroupResponse menuGroupResponse;
    private List<MenuProductResponse> menuProductResponses;

    public MenuResponse() {
    }

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

    public static MenuResponse of(Menu menu, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        MenuGroupResponse menuGroupResponse = MenuGroupResponse.from(menuGroup);
        List<MenuProductResponse> menuProductResponses = menuProducts
                .stream()
                .map(MenuProductResponse::from)
                .collect(Collectors.toList());

        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice().getValue(), menuGroupResponse, menuProductResponses);
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

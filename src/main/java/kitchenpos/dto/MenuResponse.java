package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroupResponse menuGroup;
    private List<MenuProductResponse> menuProducts;

    public static List<MenuResponse> listFrom(final List<Menu> menus) {
        return menus.stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }

    public static MenuResponse from(final Menu menu) {
        MenuGroupResponse menuGroup = MenuGroupResponse.from(menu.getMenuGroup());
        List<MenuProductResponse> menuProducts = MenuProductResponse
            .listFrom(menu.getMenuProducts());

        return MenuResponse.builder()
            .id(menu.getId())
            .name(menu.getName())
            .price(menu.getPrice())
            .menuGroup(menuGroup)
            .menuProducts(menuProducts)
            .build();
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

    public MenuGroupResponse getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}

package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    public static List<MenuResponse> listFrom(final List<Menu> menus) {
        return menus.stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }

    public static MenuResponse from(final Menu menu) {
        MenuGroup menuGroup = menu.getMenuGroup();
        List<MenuProductResponse> menuProducts = MenuProductResponse
            .listFrom(menu.getMenuProducts());

        return MenuResponse.builder()
            .id(menu.getId())
            .name(menu.getName())
            .price(menu.getPrice())
            .menuGroupId(menuGroup.getId())
            .menuProducts(menuProducts)
            .build();
    }
}

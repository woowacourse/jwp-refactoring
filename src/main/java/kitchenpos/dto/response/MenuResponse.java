package kitchenpos.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Menu;

public class MenuResponse {
    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;

    public MenuResponse(Long id, String name, BigDecimal price, Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public static MenuResponse of(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(),
            menu.getMenuGroupId());
    }

    public static List<MenuResponse> listOf(List<Menu> menus) {
        return menus.stream()
            .map(MenuResponse::of)
            .collect(Collectors.toList());
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
}

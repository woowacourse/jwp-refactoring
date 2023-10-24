package kitchenpos.menu.dto.response;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Menu;

public class MenuResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final MenuGroupResponse menuGroups;

    private MenuResponse(
            Long id,
            String name,
            BigDecimal price,
            MenuGroupResponse menuGroups
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroups = menuGroups;
    }

    public static MenuResponse from(Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                MenuGroupResponse.from(menu.getMenuGroup())
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

    public MenuGroupResponse getMenuGroups() {
        return menuGroups;
    }
}

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

    private final List<MenuProductResponse> menuProductResponses;

    private MenuResponse(
            final Long id,
            final String name,
            final BigDecimal price,
            final Long menuGroupId,
            final List<MenuProductResponse> menuProductResponses
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductResponses = menuProductResponses;
    }

    public static MenuResponse from(final Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getMenuGroupId(),
                menu.getMenuProducts().stream()
                        .map(MenuProductResponse::from)
                        .collect(Collectors.toUnmodifiableList())
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

    public List<MenuProductResponse> getMenuProductResponses() {
        return menuProductResponses;
    }
}

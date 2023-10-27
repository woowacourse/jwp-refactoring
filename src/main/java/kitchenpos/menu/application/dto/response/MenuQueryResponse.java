package kitchenpos.menu.application.dto.response;

import kitchenpos.menu.domain.Menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuQueryResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductQueryResponse> menuProducts;

    public MenuQueryResponse(final Long id, final String name, final BigDecimal price,
                             final Long menuGroupId,
                             final List<MenuProductQueryResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public MenuQueryResponse() {
    }

    public static MenuQueryResponse from(final Menu menu) {
        final List<MenuProductQueryResponse> menuProductQueryResponses =
                menu.getMenuProducts()
                        .getMenuProducts()
                        .stream()
                        .map(menuProduct -> MenuProductQueryResponse.from(menu.getId(), menuProduct))
                        .collect(Collectors.toList());
        return new MenuQueryResponse(menu.getId(), menu.getName(), menu.getPrice().getValue(),
                menu.getMenuGroupId(), menuProductQueryResponses);
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

    public List<MenuProductQueryResponse> getMenuProducts() {
        return menuProducts;
    }

}

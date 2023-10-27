package kitchenpos.dto;

import kitchenpos.menu.domain.MenuHistory;

import java.math.BigDecimal;
import java.util.List;

public class MenuHistoryResponse {

    private long id;
    private String name;
    private BigDecimal price;
    private List<MenuProductHistoryResponse> menuProducts;

    public MenuHistoryResponse(final long id,
                               final String name,
                               final BigDecimal price,
                               final List<MenuProductHistoryResponse> menuProducts
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuProducts = menuProducts;
    }

    public static MenuHistoryResponse from(final MenuHistory menuHistory) {
        return new MenuHistoryResponse(
                menuHistory.getId(),
                menuHistory.getName().getValue(),
                menuHistory.getPrice().getValue(),
                MenuProductHistoryResponse.from(menuHistory.getMenuProductHistories())
        );
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public List<MenuProductHistoryResponse> getMenuProducts() {
        return menuProducts;
    }
}

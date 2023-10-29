package kitchenpos.order.application.response;

import kitchenpos.order.domain.MenuHistory;

import java.math.BigDecimal;

public class MenuHistoryResponse {

    private Long id;
    private String name;
    private BigDecimal price;

    public MenuHistoryResponse(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static MenuHistoryResponse from(final MenuHistory menuHistory) {
        return new MenuHistoryResponse(
                menuHistory.getId(),
                menuHistory.getName().getValue(),
                menuHistory.getPrice().getValue()
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
}

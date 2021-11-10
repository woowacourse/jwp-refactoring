package kitchenpos.menu.ui.request;

import java.math.BigDecimal;

public class MenuUpdateRequest {

    private String name;
    private BigDecimal price;

    public static MenuUpdateRequest create(String name, BigDecimal price) {
        final MenuUpdateRequest menuUpdateRequest = new MenuUpdateRequest();
        menuUpdateRequest.name = name;
        menuUpdateRequest.price = price;
        return menuUpdateRequest;
    }


    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}

package kitchenpos.domain;

import kitchenpos.dto.request.MenuRequest;

public class Menu {

    private final Long id;
    private final String name;
    private final Price price;
    private final Long menuGroupId;

    public Menu(Long id, String name, Price price, Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Menu(String name, Price price, Long menuGroupId) {
        this(null, name, price, menuGroupId);
    }

    public static Menu from(MenuRequest menuRequest) {
        return new Menu(menuRequest.getName(),
                new Price(menuRequest.getPrice()),
                menuRequest.getMenuGroupId());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }
}

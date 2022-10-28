package kitchenpos.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dto.request.MenuRequest;

public class Menu {

    private final Long id;
    private final String name;
    private final Price price;
    private final Long menuGroupId;
    private final List<MenuProduct> menuProducts;

    public Menu(Long id, String name, Price price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu(String name, Price price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public Menu(String name, Price price, Long menuGroupId) {
        this(null, name, price, menuGroupId, null);
    }

    public Menu(Long id, String name, Price price, Long menuGroupId) {
        this(null, name, price, menuGroupId, null);
    }

    public static Menu from(MenuRequest menuRequest) {
        final List<MenuProduct> menuProducts = menuRequest.getMenuProducts().stream().map(MenuProduct::from)
                .collect(Collectors.toList());
        return new Menu(menuRequest.getName(),
                new Price(menuRequest.getPrice()),
                menuRequest.getMenuGroupId(),
                menuProducts);
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}

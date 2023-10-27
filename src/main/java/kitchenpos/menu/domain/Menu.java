package kitchenpos.menu.domain;

public class Menu {

    private final Long id;
    private final String name;
    private final Price price;
    private final Long menuGroupId;
    private final MenuProducts menuProducts;

    public Menu(final Long id, final String name, final Price price, final Long menuGroupId,
                final MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu(final Long id, final String name, final Price price, final Long menuGroupId) {
        this(id, name, price, menuGroupId, null);
    }

    public Menu(final String name, final Price price, final Long menuGroupId,
                final MenuProducts menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
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

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

}

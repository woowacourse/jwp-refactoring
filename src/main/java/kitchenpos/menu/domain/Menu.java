package kitchenpos.menu.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.exception.InvalidMenuPriceCreateException;

public class Menu {

    private final Long id;
    private final Name name;
    private final Price price;
    private Long menuGroupId;

    public Menu(Long id, Name name, Price price, Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Menu(Name name, Price price, Long menuGroupId) {
        this(null, name, price, menuGroupId);
    }

    public void validateMenuPrice(Price menuProductsPrice) {
        if (price.isThanExpensive(menuProductsPrice)) {
            throw new InvalidMenuPriceCreateException();
        }
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }
}

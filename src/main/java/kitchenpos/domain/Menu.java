package kitchenpos.domain;

import java.math.BigDecimal;
import lombok.Getter;

@Getter
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

    public BigDecimal getPrice() {
        return price.getValue();
    }
}

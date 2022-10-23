package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import lombok.Getter;

@Getter
public class Menu {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId) {
        if (price == null || price.doubleValue() < 0) {
            throw new IllegalArgumentException();
        }
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Menu(String name, BigDecimal price, Long menuGroupId) {
        this(null, name, price, menuGroupId);
    }
}

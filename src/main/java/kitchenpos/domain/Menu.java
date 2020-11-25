package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Menu {
    public static final BigDecimal MINIMUM_PRICE = BigDecimal.ZERO;

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        validate();
    }

    private void validate() {
        if (Objects.isNull(price) || price.compareTo(MINIMUM_PRICE) < 0) {
            throw new IllegalArgumentException("메뉴의 가격은 양수여야 합니다.");
        }
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
}

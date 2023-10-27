package kitchenpos.order.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class OrderMenu {

    private Long menuId;
    private String menuName;
    private BigDecimal menuPrice;

    protected OrderMenu() {
    }

    private OrderMenu(final Long menuId, final String menuName, final BigDecimal menuPrice) {
        validate(menuName, menuPrice);
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public static OrderMenu of(final Long menuId, final String name, final BigDecimal price) {
        return new OrderMenu(menuId, name, price);
    }

    private void validate(final String name, final BigDecimal price) {
        validateName(name);
        validatePrice(price);
    }

    private void validateName(final String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("메뉴명이 비어있습니다.");
        }
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException("메뉴 가격이 비어있습니다.");
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("메뉴 가격은 0원 이상이어야 합니다.");
        }
    }

    public Long getMenuId() {
        return menuId;
    }
}

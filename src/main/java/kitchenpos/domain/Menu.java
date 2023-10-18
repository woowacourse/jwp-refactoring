package kitchenpos.domain;

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class Menu {

    private static final BigDecimal PRICE_MINIMUM = ZERO;
    private static final int PRICE_PRECISION_MAX = 19;
    private static final int PRICE_SCALE = 2;
    private static final int NAME_LENGTH_MAXIMUM = 255;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final Long id;
    private final String name;
    private List<MenuProduct> menuProducts;

    public Menu(final Long id,
                final String name,
                final BigDecimal price,
                final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        validateName(name);
        validatePrice(price);
        this.id = id;
        this.name = name;
        this.price = price.setScale(PRICE_SCALE, RoundingMode.DOWN);
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu(final String name,
                final BigDecimal price,
                final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    private void validatePrice(final BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("메뉴 가격은 필수 항목입니다.");
        }
        if (price.compareTo(PRICE_MINIMUM) < 0) {
            throw new IllegalArgumentException("메뉴 최소 가격은 " + PRICE_MINIMUM + "원입니다.");
        }
        if (price.precision() > PRICE_PRECISION_MAX) {
            throw new IllegalArgumentException("메뉴 가격은 최대 " + PRICE_PRECISION_MAX + "자리 수까지 가능합니다.");
        }
    }

    private void validateName(final String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("메뉴 이름은 필수 항목입니다.");
        }
        if (name.length() > NAME_LENGTH_MAXIMUM) {
            throw new IllegalArgumentException("메뉴 이름의 최대 길이는 " + NAME_LENGTH_MAXIMUM + "입니다.");
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }
}

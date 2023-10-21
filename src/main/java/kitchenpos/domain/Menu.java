package kitchenpos.domain;

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Menu {

    private static final BigDecimal PRICE_MINIMUM = ZERO;
    private static final int PRICE_PRECISION_MAX = 19;
    private static final int PRICE_SCALE = 2;
    private static final int NAME_LENGTH_MAXIMUM = 255;
    private static final int MENU_PRODUCT_SIZE_MINIMUM = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;
    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;
    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(final String name,
                final BigDecimal price,
                final MenuGroup menuGroup,
                final List<MenuProduct> menuProducts) {
        final BigDecimal scaledPrice = price.setScale(PRICE_SCALE, RoundingMode.DOWN);
        validateName(name);
        validatePrice(scaledPrice, menuProducts);
        validateMenuProducts(menuProducts);
        this.name = name;
        this.price = scaledPrice;
        this.menuGroup = menuGroup;
        this.menuProducts = new ArrayList<>(menuProducts);
        menuProducts.forEach(menuProduct -> menuProduct.setMenu(this));
    }

    private void validatePrice(final BigDecimal price, final List<MenuProduct> menuProducts) {
        if (price == null) {
            throw new IllegalArgumentException("메뉴 가격은 필수 항목입니다.");
        }
        if (price.compareTo(PRICE_MINIMUM) < 0) {
            throw new IllegalArgumentException("메뉴 최소 가격은 " + PRICE_MINIMUM + "원입니다.");
        }
        if (price.precision() > PRICE_PRECISION_MAX) {
            throw new IllegalArgumentException("메뉴 가격은 최대 " + PRICE_PRECISION_MAX + "자리 수까지 가능합니다.");
        }
        if (price.compareTo(sumOfMenuProducts(menuProducts)) > 0) {
            throw new IllegalArgumentException("메뉴 가격은 메뉴에 포함된 상품 가격의 총합보다 클 수 없습니다.");
        }
    }

    private BigDecimal sumOfMenuProducts(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProduct::totalPrice)
                .reduce(ZERO, BigDecimal::add);
    }

    private void validateName(final String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("메뉴 이름은 필수 항목입니다.");
        }
        if (name.length() > NAME_LENGTH_MAXIMUM) {
            throw new IllegalArgumentException("메뉴 이름의 최대 길이는 " + NAME_LENGTH_MAXIMUM + "입니다.");
        }
    }

    private void validateMenuProducts(final List<MenuProduct> menuProducts) {
        if (menuProducts.size() < MENU_PRODUCT_SIZE_MINIMUM) {
            throw new IllegalArgumentException("메뉴 상품의 최소 개수는 " + MENU_PRODUCT_SIZE_MINIMUM + "개입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getMenuGroupId() {
        if (Objects.isNull(menuGroup)) {
            return null;
        }
        return menuGroup.getId();
    }

    public List<MenuProduct> getMenuProducts() {
        return new ArrayList<>(menuProducts);
    }
}

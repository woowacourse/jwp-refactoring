package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.domain.vo.Price;

@Entity
public class Menu {

    private static final int NAME_LENGTH_MAXIMUM = 255;
    private static final int MENU_PRODUCT_SIZE_MINIMUM = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Embedded
    private Price price;
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
        validateName(name);
        this.name = name;
        this.price = Price.from(price);
        this.menuGroup = menuGroup;
        validateMenuProducts(menuProducts);
        this.menuProducts = new ArrayList<>(menuProducts);
        menuProducts.forEach(menuProduct -> menuProduct.setMenu(this));
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
        if (price.isBiggerThan(sumOfMenuProducts(menuProducts))) {
            throw new IllegalArgumentException("메뉴 가격은 메뉴에 포함된 상품 가격의 총합보다 클 수 없습니다.");
        }
        if (menuProducts.size() < MENU_PRODUCT_SIZE_MINIMUM) {
            throw new IllegalArgumentException("메뉴 상품의 최소 개수는 " + MENU_PRODUCT_SIZE_MINIMUM + "개입니다.");
        }
    }

    private Price sumOfMenuProducts(final List<MenuProduct> menuProducts) {
        final List<Price> prices = menuProducts.stream()
                .map(MenuProduct::totalPrice)
                .collect(Collectors.toList());
        return Price.sum(prices);
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

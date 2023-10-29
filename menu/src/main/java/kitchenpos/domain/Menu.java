package kitchenpos.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Column(precision = 19, scale = 2)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    private Menu(final Long id, final String name, final BigDecimal price, final MenuGroup menuGroup, final List<MenuProduct> menuProducts) {
        validate(name, price);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = MenuProducts.create(menuProducts);
    }

    public static Menu create(final String name, final BigDecimal price, final MenuGroup menuGroup) {
        return new Menu(null, name, price, menuGroup, new ArrayList<>());
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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }

    public void updateMenuProducts(final List<MenuProduct> menuProductsRequest) {
        final MenuProducts menuProducts = MenuProducts.create(menuProductsRequest);
        if (menuProducts.isInvalidPrice(price)) {
            throw new IllegalArgumentException("메뉴 금액의 합계는 각 상품들의 금액 합계보다 클 수 없습니다.");
        }
        this.menuProducts = menuProducts;
    }
}

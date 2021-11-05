package kitchenpos.menu.domain;

import kitchenpos.exception.FieldNotValidException;
import sun.awt.geom.AreaOp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", nullable = false)
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this(null, name, price, menuGroup);
    }

    public Menu(String name, BigDecimal price) {
        this(null, name, price, null);
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup) {
       this(id, name, price, menuGroup, new ArrayList<>());
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validate(name, price, menuGroup);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    private void validate(String name, BigDecimal price, MenuGroup menuGroup) {
        validateName(name);
        validatePrice(price);
        validateMenuGroup(menuGroup);
    }

    private void validateName(String name) {
        if (Objects.isNull(name) || name.isEmpty()) {
            throw new FieldNotValidException("메뉴명이 유효하지 않습니다.");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new FieldNotValidException("메뉴의 가격이 유효하지 않습니다.");
        }
    }

    private void validateMenuGroup(MenuGroup menuGroup) {
        if (Objects.isNull(menuGroup)) {
            throw new FieldNotValidException("메뉴 그룹이 유효하지 않습니다.");
        }
    }

    public void validateTotalPrice(BigDecimal sum) {
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("상품 가격에 비해 메뉴의 가격이 큽니다.");
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

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        for (MenuProduct menuProduct: menuProducts) {
            menuProduct.addMenu(this);
        }
    }
}

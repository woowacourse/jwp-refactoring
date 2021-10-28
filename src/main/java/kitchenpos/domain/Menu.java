package kitchenpos.domain;

import kitchenpos.exception.FieldNotValidException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
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

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this(null, name, price, menuGroup);
    }

    public Menu(String name, BigDecimal price) {
        this(null, name, price, null);
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup) {
        validate(name, price, menuGroup);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    private void validate(String name, BigDecimal price, MenuGroup menuGroup) {
        validateName(name);
        validatePrice(price);
        validateMenuGroup(menuGroup);
    }

    private void validateName(String name) {
        if (Objects.isNull(name)){
            throw new FieldNotValidException(this.getClass().getSimpleName(), "name");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new FieldNotValidException(this.getClass().getSimpleName(), "price");
        }
    }

    private void validateMenuGroup(MenuGroup menuGroup) {
        if (Objects.isNull(menuGroup)){
            throw new FieldNotValidException(this.getClass().getSimpleName(), "menuGroup");
        }
    }

    public Menu addMenuGroup(MenuGroup menuGroup) {
        this.menuGroup = menuGroup;
        return this;
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

    public void validateTotalPrice(BigDecimal sum) {
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("제품 가격에 비해 메뉴의 가격이 큽니다.");
        }
    }
}

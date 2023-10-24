package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.common.domain.Money;
import kitchenpos.common.exception.KitchenPosException;
import kitchenpos.menugroup.domain.MenuGroup;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String name;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "price", precision = 19, scale = 2, nullable = false))
    private Money price;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id", nullable = false, updatable = false)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(Long id, String name, Money price, MenuGroup menuGroup) {
        validate(price);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    private void validate(Money price) {
        if (price == null) {
            throw new KitchenPosException("메뉴의 가격은 null이 될 수 없습니다.");
        }
        if (price.isLessThan(Money.ZERO)) {
            throw new KitchenPosException("메뉴의 가격은 0보다 작을 수 없습니다.");
        }
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
        validateMenuProducts(this.menuProducts);
    }

    private void validateMenuProducts(List<MenuProduct> menuProducts) {
        Money totalPrice = menuProducts.stream()
            .map(MenuProduct::getTotalPrice)
            .reduce(Money.ZERO, Money::plus);
        if (price.isGreaterThan(totalPrice)) {
            throw new KitchenPosException("메뉴의 가격은 메뉴 상품의 총합 가격보다 작아야 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }
}

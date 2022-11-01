package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id", nullable = false)
    private MenuGroup menuGroup;

    public Menu() {
    }

    public Menu(final String name, final BigDecimal price, final MenuGroup menuGroup) {
        this(null, name, price, menuGroup);
    }

    public Menu(final Long id, final String name, final BigDecimal price, final MenuGroup menuGroup) {
        validatePriceNegative(price);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public void validatePriceAppropriate(final List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.getAmount());
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 메뉴 상품들의 총액보다 비쌀 수 없습니다.");
        }
    }

    private void validatePriceNegative(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 음수일 수 없습니다.");
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
}

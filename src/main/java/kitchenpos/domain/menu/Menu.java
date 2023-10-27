package kitchenpos.domain.menu;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "price"))
    private Price price;

    private Long menuGroupId;

    @Embedded
    private final MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(final String name, final BigDecimal price, final Long menuGroupId) {
        this.name = name;
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
    }

    public void addMenuProducts(final MenuProducts menuProducts) {
        validatePrice(menuProducts);

        for (final MenuProduct menuProduct : menuProducts.getMenuProducts()) {
            this.menuProducts.add(menuProduct);
            menuProduct.initMenu(this);
        }
    }

    private void validatePrice(final MenuProducts menuProducts) {
        final Price menuProductsTotalPrice = menuProducts.calculateTotalPrice();
        if (price == null || price.biggerThan(menuProductsTotalPrice)) {
            throw new IllegalArgumentException("메뉴 가격은 메뉴를 구성하는 상품의 가격 합보다 작아야 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Menu menu = (Menu) o;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.exception.InvalidMenuException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menu")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts;

    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
                final MenuProducts menuProducts) {
        validatePrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidMenuException("가격은 0보다 커야합니다.");
        }
    }

    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId) {
        this(id, name, price, menuGroupId, new MenuProducts(Collections.emptyList()));
    }

    public Menu(final String name, final BigDecimal price, final Long menuGroupId) {
        this(null, name, price, menuGroupId, new MenuProducts(Collections.emptyList()));
    }

    public Menu(final String name,
                final BigDecimal price,
                final Long menuGroupId,
                final MenuProducts menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public void addMenuProducts(final MenuProducts menuProducts) {
        menuProducts.belongsTo(id);
        this.menuProducts.addAll(menuProducts);
    }

    public void addMenuProducts(final List<MenuProduct> rawMenuProducts) {
        addMenuProducts(new MenuProducts(rawMenuProducts));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Menu menu)) {
            return false;
        }
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", menuGroupId=" + menuGroupId +
                ", menuProducts=" + menuProducts +
                '}';
    }
}

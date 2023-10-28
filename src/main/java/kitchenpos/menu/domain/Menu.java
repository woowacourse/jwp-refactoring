package kitchenpos.menu.domain;

import kitchenpos.common.vo.Price;
import kitchenpos.exception.InvalidMenuPriceException;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
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
    private MenuProducts menuProducts;

    protected Menu() {}

    private Menu(
            final String name,
            final Price price,
            final Long menuGroupId,
            final MenuProducts menuProducts
    ) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static Menu of(
            final String name,
            final Price price,
            final Long menuGroupId,
            final MenuProducts menuProducts
    ) {
        validateMenuPrice(menuProducts, price);

        return new Menu(name, price, menuGroupId, menuProducts);
    }

    private static void validateMenuPrice(final MenuProducts menuProducts, final Price price) {
        final Price totalPrice = menuProducts.calculateTotalPrice();

        if (price.isHigherThan(totalPrice)) {
            System.out.println("price : " + price.getValue());
            System.out.println("totalPrice : " + totalPrice.getValue());
            throw new InvalidMenuPriceException("메뉴 가격이 상품들의 가격 합보다 클 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
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

    @Override
    public String toString() {
        return "Menu{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", price=" + price +
               '}';
    }
}

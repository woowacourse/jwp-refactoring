package kitchenpos.domain;

import java.math.BigDecimal;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Menu {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Embedded
    private Name name;
    @Embedded
    private Price price;
    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;
    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    public Menu(final String name, final BigDecimal price, final MenuGroup menuGroup) {
        this(null, name, price, menuGroup);
    }

    public Menu(final Long id, final String name, final BigDecimal price,
                final MenuGroup menuGroup) {
        this(id, name, new Price(price), menuGroup);
    }

    public Menu(final Long id, final String name, final Price price, final MenuGroup menuGroup) {
        this(id, name, price, menuGroup, new MenuProducts());
    }

    public Menu(final Long id, final String name, final Price price, final MenuGroup menuGroup,
                final MenuProducts menuProducts) {
        this.id = id;
        this.name = new Name(name);
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public void addProduct(final Product product, final long quantity) {
        validateToAddProduct(product, quantity);
        menuProducts.add(new MenuProduct(this, product, quantity));
    }

    private void validateToAddProduct(final Product product, final long quantity) {
        BigDecimal newTotalPrice = menuProducts.totalPrice()
            .add(totalPriceOfNewProduct(product, quantity));
        if (price.isBiggerThan(newTotalPrice)) {
            throw new IllegalArgumentException(String.format(
                "메뉴 가격은 상품 가격의 합보다 클 수 없습니다.(메뉴 가격: %d, 상품 가격: %d",
                price.getValue().intValue(),
                newTotalPrice.intValue()
            ));
        }
    }

    private BigDecimal totalPriceOfNewProduct(final Product newProduct, final long quantity) {
        return newProduct.getPrice()
            .getValue()
            .multiply(BigDecimal.valueOf(quantity));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public Price getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}

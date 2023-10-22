package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;


    @Embedded
    private Price price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id", nullable = false)
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this(null, name, price, menuGroup, new ArrayList<>());
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validateName(name);

        this.id = id;
        this.name = name;
        this.price = new Price(price);
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;

        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.assignMenu(this);
        }
    }

    private void validateName(String name) {
        if (name.isBlank() || name.length() > 255) {
            throw new MenuException("메뉴의 이름이 유효하지 않습니다.");
        }
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        Price sumOfMenuProductPrices = calculateSumOf(menuProducts);

        if (price.biggerThan(sumOfMenuProductPrices)) {
            throw new MenuException("메뉴 상품의 가격의 총합이 메뉴의 가격보다 작습니다.");
        }

        this.menuProducts.addAll(menuProducts);
    }

    private Price calculateSumOf(List<MenuProduct> menuProducts) {
        Price sumOfMenuProductPrice = new Price(BigDecimal.ZERO);
        for (MenuProduct menuProduct : menuProducts) {
            Price productPrice = menuProduct.getProduct().getPrice();
            long productQuantity = menuProduct.getQuantity();
            Price menuProductPrice = productPrice.multiply(productQuantity);

            sumOfMenuProductPrice = sumOfMenuProductPrice.plus(menuProductPrice);
        }
        return sumOfMenuProductPrice;
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}

package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.application.dto.ProductQuantityDto;
import kitchenpos.domain.vo.MenuName;
import kitchenpos.domain.vo.MenuProducts;
import kitchenpos.domain.vo.Price;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private MenuName menuName;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", nullable = false)
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this.menuName = new MenuName(name);
        this.price = new Price(price);
        this.menuGroup = menuGroup;
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.menuName = new MenuName(name);
        this.price = new Price(price);
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return menuName.getName();
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }

    public void updateProducts(List<ProductQuantityDto> productQuantities) {
        MenuProducts menuProducts = new MenuProducts(getMenuProducts(productQuantities));
        if (menuProducts.isPriceLessThan(getPrice())) {
            throw new IllegalArgumentException("메뉴 가격이 상품 가격의 합보다 클 수 없습니다.");
        }
        this.menuProducts = menuProducts;
    }

    private List<MenuProduct> getMenuProducts(List<ProductQuantityDto> productQuantities) {
        return productQuantities.stream()
                .map(productQuantity ->
                        new MenuProduct(this, productQuantity.getProduct(), productQuantity.getQuantity())
                )
                .collect(Collectors.toList());
    }
}

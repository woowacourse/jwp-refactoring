package kitchenpos.menu.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.product.domain.Price;
import suppoert.domain.BaseEntity;

@Entity
public class Menu extends BaseEntity {

    @Column(nullable = false)
    private String name;
    @Embedded
    private Price price;
    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_menu_to_menu_group"))
    private MenuGroup menuGroup;
    @Embedded
    private MenuProducts menuProducts;

    public Menu(final String name, final Price price, final MenuGroup menuGroup) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = new MenuProducts();
    }

    /*
    국물 떡볶이 세트 (국물 떡볶이 1인분, 순대 1인분) 8000원
    국물 떡볶이 6000원
    순대 3000원
    세트 메뉴가 단품을 시킨것 보다 가격이 높은지 검증
    */
    public void addMenuProduct(MenuProduct menuProduct) {
        final long total = menuProducts.calculateTotalPrice() + menuProduct.calculatePrice();
        if (price.getPrice().intValue() > total) {
            throw new IllegalArgumentException("메뉴 가격은 단품을 가격보다 높을 수 없습니다.");
        }
        menuProducts.add(menuProduct);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public Long getMenuGroup() {
        return menuGroup.getId();
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    protected Menu() {
    }
}

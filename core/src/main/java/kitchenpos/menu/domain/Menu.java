package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.common.Price;
import kitchenpos.menugroup.domain.MenuGroup;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public Menu() {
    }

    public Menu(Long id, String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validateAdditionalPrice(menuProducts, price);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        addMenuProducts(menuProducts);
    }

    private void validateAdditionalPrice(List<MenuProduct> menuProducts, Price requestPrice) {
        Price totalPrice = new Price(BigDecimal.valueOf(0));
        for (MenuProduct menuProduct : menuProducts) {
            totalPrice = totalPrice.plus(menuProduct.price());
        }
        if (requestPrice.compareTo(totalPrice) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 메뉴 상품들의 총합보다 비쌀 수 없습니다.");
        }
    }

    private void addMenuProducts(List<MenuProduct> menuProducts) {
        validateUniqueMenu(menuProducts);
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.assignMenu(this);
            this.menuProducts.add(menuProduct);
        }
    }

    private void validateUniqueMenu(List<MenuProduct> menuProducts) {
        List<MenuProduct> allMenu = new ArrayList<>(this.menuProducts);
        allMenu.addAll(menuProducts);
        if (allMenu.size() != uniqueProductIdSize(allMenu)) {
            throw new IllegalArgumentException("중복된 menu 가 있습니다.");
        }
    }

    private long uniqueProductIdSize(List<MenuProduct> allMenu) {
        return allMenu.stream()
            .map(menuProduct -> menuProduct.getProduct().getId())
            .distinct()
            .count();
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

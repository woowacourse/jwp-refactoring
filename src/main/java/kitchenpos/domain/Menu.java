package kitchenpos.domain;

import kitchenpos.domain.vo.Price;
import org.hibernate.annotations.BatchSize;

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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Menu {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Embedded
    private Price price;

    @JoinColumn(name = "menu_group_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;

    @BatchSize(size = 10)
    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true, mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public Menu() {
    }

    public Menu(final String name,
                final Price price,
                final MenuGroup menuGroup,
                final List<MenuProduct> menuProducts
    ) {
        this(null, name, price, menuGroup, menuProducts);
    }

    public Menu(final Long id,
                final String name,
                final Price price,
                final MenuGroup menuGroup,
                final List<MenuProduct> menuProducts
    ) {
        validate(price);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    private void validate(final Price price) {
        if (isPriceNullOrNegative(price)) {
            throw new IllegalArgumentException("상품의 가격은 null 이거나 음수일 수 없습니다.");
        }
    }

    private boolean isPriceNullOrNegative(final Price price) {
        return price == null || price.getValue().compareTo(BigDecimal.ZERO) < 0;
    }

    public void addMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts.stream()
                .map(menuProduct -> new MenuProduct(this, menuProduct.getProduct(), menuProduct.getQuantity()))
                .collect(Collectors.toList());
    }

    public boolean isGreaterThan(final BigDecimal otherPrice) {
        return price.getValue().compareTo(otherPrice) > 0;
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

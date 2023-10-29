package kitchenpos.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;
import static org.hibernate.internal.util.StringHelper.isBlank;
import static org.springframework.util.CollectionUtils.isEmpty;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private Price price;
    private Long menuGroupId;

    @JoinColumn(name = "menu_id")
    @OneToMany(cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(final Long id, final String name, final Price price, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        validate(name, menuGroupId, menuProducts);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    private void validate(final String name, final Long menuGroupId, final List<MenuProduct> menuProducts) {
        if (isBlank(name)) {
            throw new IllegalArgumentException("메뉴 이름이 필요합니다.");
        }
        if (isNull(menuGroupId)) {
            throw new IllegalArgumentException("메뉴 그룹이 필요합니다.");
        }
        if (isEmpty(menuProducts)) {
            throw new IllegalArgumentException("메뉴 상품이 필요합니다.");
        }
    }

    public Menu(final String name, final Price price, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
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
        return menuProducts;
    }
}

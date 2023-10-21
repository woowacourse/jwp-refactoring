package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.exception.InvalidMenuException;

public class Menu {
    private Long id;
    private String name;
    private Price price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public Menu() {
    }

    public Menu(final String name, final BigDecimal price, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        validateMenuGroupId(menuGroupId);
        validateMenuProducts(menuProducts);
        this.id = id;
        this.name = name;
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    private void validateMenuGroupId(final Long menuGroupId) {
        if (menuGroupId == null) {
            throw new InvalidMenuException("메뉴 그룹은 null일 수 없습니다.");
        }
    }

    private void validateMenuProducts(final List<MenuProduct> menuProducts) {
        if (menuProducts.isEmpty()) {
            throw new InvalidMenuException("최소 한 개의 메뉴 상품이 등록되어야 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public void setPrice(final BigDecimal price) {
        this.price = new Price(price);
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(final Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Menu)) {
            return false;
        }
        final Menu menu = (Menu) o;
        return Objects.equals(id, menu.id) && Objects.equals(name, menu.name)
                && Objects.equals(price, menu.price) && Objects.equals(menuGroupId, menu.menuGroupId)
                && Objects.equals(menuProducts, menu.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroupId, menuProducts);
    }
}

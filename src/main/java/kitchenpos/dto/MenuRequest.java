package kitchenpos.dto;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static kitchenpos.support.money.Money.valueOf;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuRequest {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest(
            Long id,
            String name,
            BigDecimal price,
            Long menuGroupId,
            List<MenuProductRequest> menuProducts
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toMenu(Map<Long, Product> products) {
        return menuProducts.stream()
                .map(menuProduct -> new MenuProduct(
                        products.get(menuProduct.getProductId()),
                        menuProduct.getQuantity())
                )
                .collect(collectingAndThen(toList(), items -> new Menu(name, valueOf(price), menuGroupId, items)));

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public List<Long> getMenuProductIds() {
        return menuProducts.stream()
                .map(MenuProductRequest::getProductId)
                .collect(toList());
    }
}

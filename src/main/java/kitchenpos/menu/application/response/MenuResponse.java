package kitchenpos.menu.application.response;

import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.RelatedProduct;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    @JsonCreator
    public MenuResponse(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
                        final List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public MenuResponse(final Menu menu, final List<MenuProductResponse> menuProducts) {
        this(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(), menuProducts);
    }

    public static MenuResponse from(final Menu menu) {
        final List<RelatedProduct> relatedProducts = menu.getMenuProducts().getRelatedProducts();
        final List<MenuProductResponse> menuProducts = relatedProducts.stream()
                .map(it -> new MenuProductResponse(menu.getId(), it.getProduct().getId(), it.getQuantity()))
                .collect(toList());

        return new MenuResponse(menu, menuProducts);
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
}

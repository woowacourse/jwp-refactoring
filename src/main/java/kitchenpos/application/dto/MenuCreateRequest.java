package kitchenpos.application.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuCreateRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductCreateRequest> productRequests;

    private MenuCreateRequest() {
    }

    public MenuCreateRequest(
            String name,
            BigDecimal price,
            Long menuGroupId,
            List<MenuProductCreateRequest> productRequests
    ) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.productRequests = productRequests;
    }

    public Menu toMenu(List<Product> products) {
        return new Menu(
                name,
                price,
                menuGroupId,
                productRequests.stream()
                        .map(menuProductCreateRequest -> toMenuProduct(menuProductCreateRequest, products))
                        .collect(Collectors.toList())
        );
    }

    private static MenuProduct toMenuProduct(
            MenuProductCreateRequest menuProductCreateRequest,
            List<Product> products
    ) {
        return menuProductCreateRequest.toMenuProduct(
                products.stream()
                .filter(product -> product.getId().equals(menuProductCreateRequest.getProductId()))
                .findAny()
                .orElseThrow()
        );
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

    public List<MenuProductCreateRequest> getProductRequests() {
        return productRequests;
    }
}

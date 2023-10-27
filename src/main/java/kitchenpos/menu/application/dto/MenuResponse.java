package kitchenpos.menu.application.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<ProductQuantityDto> menuProducts;

    private MenuResponse() {}

    private MenuResponse(final Long id,
                        final String name,
                        final BigDecimal price,
                        final Long menuGroupId,
                        final List<ProductQuantityDto> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(final Menu menu) {
        final List<ProductQuantityDto> productQuantityDtos = menu.getMenuProducts()
            .stream()
            .map(ProductQuantityDto::from)
            .collect(Collectors.toList());

        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(), productQuantityDtos);
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

    public List<ProductQuantityDto> getMenuProducts() {
        return menuProducts;
    }
}

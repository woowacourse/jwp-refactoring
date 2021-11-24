package kitchenpos.ui.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private List<MenuProductResponse> menuProducts;

    private MenuResponse() {
    }

    public MenuResponse(
        Long id,
        String name,
        BigDecimal price,
        List<MenuProductResponse> menuProducts
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuProducts = menuProducts;
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

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}

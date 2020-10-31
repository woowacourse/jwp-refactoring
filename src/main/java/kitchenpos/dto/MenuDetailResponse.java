package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;

public class MenuDetailResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private List<MenuProductResponse> menuProductResponses;

    protected MenuDetailResponse() {
    }

    public MenuDetailResponse(Long id, String name, BigDecimal price,
            List<MenuProductResponse> menuProductResponses) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuProductResponses = menuProductResponses;
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

    public List<MenuProductResponse> getMenuProductResponses() {
        return menuProductResponses;
    }
}

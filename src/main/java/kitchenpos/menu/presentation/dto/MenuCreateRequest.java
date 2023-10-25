package kitchenpos.menu.presentation.dto;

import java.math.BigDecimal;
import java.util.List;

public class MenuCreateRequest {

    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<Long> productIds;
    private final List<Integer> counts;

    public MenuCreateRequest(String name, BigDecimal price, Long menuGroupId, List<Long> productIds, List<Integer> counts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.productIds = productIds;
        this.counts = counts;
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

    public List<Long> getProductIds() {
        return productIds;
    }

    public List<Integer> getCounts() {
        return counts;
    }
}

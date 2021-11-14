package kitchenpos.menu.dto;

import java.util.List;

public class MenuRequest {

    private String name;
    private double price;
    private Long menuGroupId;
    private List<Long> productIds;
    private long quantity;

    public MenuRequest() {

    }

    public MenuRequest(String name, double price, Long menuGroupId, List<Long> productIds,
        long quantity) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.productIds = productIds;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public long getQuantity() {
        return quantity;
    }
}

package kitchenpos.application.request;

import java.util.List;

public class MenuCreateRequest {

    private String name;
    private long price;
    private Long menuGroupId;
    private List<ProductQuantityDto> productQuantityDtos;

    private MenuCreateRequest() {
    }

    public MenuCreateRequest(String name, long price, Long menuGroupId, List<ProductQuantityDto> productQuantityDtos) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.productQuantityDtos = productQuantityDtos;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<ProductQuantityDto> getProductQuantityDtos() {
        return productQuantityDtos;
    }
}

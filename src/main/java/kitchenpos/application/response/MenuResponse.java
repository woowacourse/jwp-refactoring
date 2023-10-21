package kitchenpos.application.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.request.ProductQuantityDto;
import kitchenpos.domain.Menu;

public class MenuResponse {

    private Long id;
    private String name;
    private String price;
    private Long menuGroupId;
    private List<ProductQuantityDto> productQuantityDtos;

    protected MenuResponse() {
    }

    private MenuResponse(Long id, String name, String price, Long menuGroupId,
                         List<ProductQuantityDto> productQuantityDtos) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.productQuantityDtos = productQuantityDtos;
    }

    public static MenuResponse from(Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice().toString(),
                menu.getMenuGroup().getId(),
                menu.getMenuProducts().stream()
                        .map(ProductQuantityDto::from)
                        .collect(Collectors.toList())
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<ProductQuantityDto> getProductQuantityDtos() {
        return productQuantityDtos;
    }
}

package kitchenpos.ui.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;

public class MenuResponse {

    private Long id;
    private String name;
    private Integer price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProductResponses;

    public MenuResponse() {
    }

    public MenuResponse(Long id, String name, Integer price, Long menuGroupId,
                        List<MenuProductResponse> menuProductResponses) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductResponses = menuProductResponses;
    }

    public static MenuResponse of(Menu menu) {
        return new MenuResponse(
                menu.getId(), menu.getName(), menu.getPrice().getPrice().intValue(), menu.getMenuGroup().getId(),
                menu.getMenuProducts()
                        .stream()
                        .map(it -> MenuProductResponse.of(it, menu.getId()))
                        .collect(Collectors.toList())
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductResponse> getMenuProductResponses() {
        return menuProductResponses;
    }
}

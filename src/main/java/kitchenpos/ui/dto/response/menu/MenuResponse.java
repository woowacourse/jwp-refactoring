package kitchenpos.ui.dto.response.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.response.menu.MenuProductResponseDto;
import kitchenpos.application.dto.response.menu.MenuResponseDto;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroupResponse menuGroupResponse;
    private List<MenuProductResponse> menuProductResponses;

    private MenuResponse() {
    }

    public MenuResponse(Long id, String name, BigDecimal price,
        MenuGroupResponse menuGroupResponse,
        List<MenuProductResponse> menuProductResponses) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupResponse = menuGroupResponse;
        this.menuProductResponses = menuProductResponses;
    }

    public static MenuResponse from(MenuResponseDto menuResponseDto) {
        return new MenuResponse(
            menuResponseDto.getId(),
            menuResponseDto.getName(),
            menuResponseDto.getPrice(),
            MenuGroupResponse.from(menuResponseDto.getMenuGroupResponseDto()),
            convert(menuResponseDto.getMenuProductResponseDtos())
        );
    }

    private static List<MenuProductResponse> convert(List<MenuProductResponseDto> menuProductResponseDtos) {
        return menuProductResponseDtos.stream()
            .map(MenuProductResponse::from)
            .collect(Collectors.toList());
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

    public MenuGroupResponse getMenuGroupResponse() {
        return menuGroupResponse;
    }

    public List<MenuProductResponse> getMenuProductResponses() {
        return menuProductResponses;
    }
}

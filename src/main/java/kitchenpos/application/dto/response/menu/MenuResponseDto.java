package kitchenpos.application.dto.response.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuResponseDto {

    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroupResponseDto menuGroupResponseDto;
    private List<MenuProductResponseDto> menuProductResponseDtos;

    private MenuResponseDto() {
    }

    public MenuResponseDto(
        Long id,
        String name,
        BigDecimal price,
        MenuGroupResponseDto menuGroupResponseDto,
        List<MenuProductResponseDto> menuProductResponseDtos
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupResponseDto = menuGroupResponseDto;
        this.menuProductResponseDtos = menuProductResponseDtos;
    }

    public static MenuResponseDto from(Menu menu) {
        return new MenuResponseDto(
            menu.getId(),
            menu.getName(),
            menu.getPrice(),
            MenuGroupResponseDto.from(menu.getMenuGroup()),
            getMenuProductDtos(menu.getMenuProducts())
        );
    }

    private static List<MenuProductResponseDto> getMenuProductDtos(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
            .map(MenuProductResponseDto::from)
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

    public MenuGroupResponseDto getMenuGroupResponseDto() {
        return menuGroupResponseDto;
    }

    public List<MenuProductResponseDto> getMenuProductResponseDtos() {
        return menuProductResponseDtos;
    }
}

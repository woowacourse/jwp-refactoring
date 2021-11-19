package kitchenpos.ui;

import static java.util.stream.Collectors.toList;

import java.net.URI;
import java.util.List;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.ui.dto.request.menu.MenuProductRequestDto;
import kitchenpos.ui.dto.request.menu.MenuRequestDto;
import kitchenpos.ui.dto.response.menu.MenuProductResponseDto;
import kitchenpos.ui.dto.response.menu.MenuResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuRestController {
    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuResponseDto> create(
        @RequestBody final MenuRequestDto menuRequestDto) {
        final Menu created = menuService.create(
            new Menu(
                menuRequestDto.getName(),
                menuRequestDto.getPrice(),
                menuRequestDto.getMenuGroupId(),
                toMenuProducts(menuRequestDto.getMenuProducts())
            )
        );

        final MenuResponseDto responseDto = toMenuResponseDto(created);

        final URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri)
            .body(responseDto)
            ;
    }

    private List<MenuProduct> toMenuProducts(List<MenuProductRequestDto> menuProductRequestDtos) {
        return menuProductRequestDtos.stream()
            .map(dto -> new MenuProduct(dto.getProductId(), dto.getQuantity()))
            .collect(toList());
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponseDto>> list() {
        List<Menu> menus = menuService.list();
        List<MenuResponseDto> responseDto = menus.stream()
            .map(this::toMenuResponseDto)
            .collect(toList());

        return ResponseEntity.ok()
            .body(responseDto)
            ;
    }

    private MenuResponseDto toMenuResponseDto(Menu created) {
        return new MenuResponseDto(
            created.getId(),
            created.getName(),
            created.getPrice(),
            created.getMenuGroupId(),
            toMenuProductsResponse(created.getMenuProducts())
        );
    }

    private List<MenuProductResponseDto> toMenuProductsResponse(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
            .map(menuProduct -> new MenuProductResponseDto(
                menuProduct.getSeq(),
                menuProduct.getMenuId(),
                menuProduct.getProductId(),
                menuProduct.getQuantity()
            )).collect(toList());
    }
}

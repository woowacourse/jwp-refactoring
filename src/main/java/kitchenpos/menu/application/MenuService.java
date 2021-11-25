package kitchenpos.menu.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductCalculator;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import kitchenpos.menugroup.exception.InvalidMenuGroupException;
import kitchenpos.menu.ui.dto.request.MenuProductRequestDto;
import kitchenpos.menu.ui.dto.request.MenuRequestDto;
import kitchenpos.menu.ui.dto.response.MenuProductResponseDto;
import kitchenpos.menu.ui.dto.response.MenuResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductCalculator menuProductCalculator;

    public MenuService(
        MenuRepository menuRepository,
        MenuGroupRepository menuGroupRepository,
        MenuProductCalculator menuProductCalculator
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductCalculator = menuProductCalculator;
    }

    @Transactional
    public MenuResponseDto create(final MenuRequestDto menuRequestDto) {
        validateMenuGroupId(menuRequestDto);

        Menu menu = new Menu(
            menuRequestDto.getName(),
            menuRequestDto.getPrice(),
            menuRequestDto.getMenuGroupId(),
            toMenuProducts(menuRequestDto.getMenuProducts())
        );
        menu.validateMenuPrice(menuProductCalculator);

        final Menu savedMenu = menuRepository.save(menu);
        return toMenuResponseDto(savedMenu);
    }

    private void validateMenuGroupId(MenuRequestDto menuRequestDto) {
        if (!menuGroupRepository.existsById(menuRequestDto.getMenuGroupId())) {
            throw new InvalidMenuGroupException();
        }
    }

    private List<MenuProduct> toMenuProducts(List<MenuProductRequestDto> menuProductRequestDtos) {
        return menuProductRequestDtos.stream()
            .map(dto -> new MenuProduct(dto.getProductId(), dto.getQuantity()))
            .collect(toList());
    }

    private MenuResponseDto toMenuResponseDto(Menu created) {
        return new MenuResponseDto(
            created.getId(),
            created.getName(),
            created.getPrice(),
            created.getMenuGroupId(),
            toMenuProductsResponse(created.getMenuProducts(), created.getId())
        );
    }

    private List<MenuProductResponseDto> toMenuProductsResponse(
        List<MenuProduct> menuProducts, Long menuId
    ) {
        return menuProducts.stream()
            .map(menuProduct -> new MenuProductResponseDto(
                menuProduct.getSeq(),
                menuId,
                menuProduct.getProductId(),
                menuProduct.getQuantity()
            )).collect(toList());
    }

    public List<MenuResponseDto> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
            .map(this::toMenuResponseDto)
            .collect(toList());
    }
}

package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.exception.InvalidMenuGroupException;
import kitchenpos.exception.InvalidProductException;
import kitchenpos.ui.dto.request.menu.MenuProductRequestDto;
import kitchenpos.ui.dto.request.menu.MenuRequestDto;
import kitchenpos.ui.dto.response.menu.MenuProductResponseDto;
import kitchenpos.ui.dto.response.menu.MenuResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
        final MenuRepository menuRepository,
        final MenuGroupRepository menuGroupRepository,
        final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponseDto create(final MenuRequestDto menuRequestDto) {
        Menu menu = new Menu(
            menuRequestDto.getName(),
            menuRequestDto.getPrice(),
            findMenuGroupById(menuRequestDto.getMenuGroupId()),
            toMenuProducts(menuRequestDto.getMenuProducts())
        );

        final Menu savedMenu = menuRepository.save(menu);
        return toMenuResponseDto(savedMenu);
    }

    private MenuGroup findMenuGroupById(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
            .orElseThrow(InvalidMenuGroupException::new);
    }

    private List<MenuProduct> toMenuProducts(List<MenuProductRequestDto> menuProductRequestDtos) {
        return menuProductRequestDtos.stream()
            .map(dto -> new MenuProduct(findProductById(dto.getProductId()), dto.getQuantity()))
            .collect(toList());
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(InvalidProductException::new);
    }

    private MenuResponseDto toMenuResponseDto(Menu created) {
        return new MenuResponseDto(
            created.getId(),
            created.getName(),
            created.getPrice(),
            created.getMenuGroup().getId(),
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
                menuProduct.getProduct().getId(),
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

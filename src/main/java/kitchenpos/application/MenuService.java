package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuVerifier;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreateRequestDto;
import kitchenpos.dto.MenuProductCreateRequestDto;
import kitchenpos.dto.MenuResponseDto;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
        final MenuRepository menuRepository,
        final MenuGroupRepository menuGroupRepository,
        final MenuProductRepository menuProductRepository,
        final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponseDto create(final MenuCreateRequestDto menuCreateRequest) {
        validateMenuGroupExistence(menuCreateRequest.getMenuGroupId());

        final List<Long> productsIds = menuCreateRequest.getMenuProductRequestDto().stream()
            .map(MenuProductCreateRequestDto::getProductId)
            .collect(Collectors.toList());
        final List<MenuProduct> menuProducts = menuCreateRequest.getMenuProductRequestDto().stream()
            .map(MenuProductCreateRequestDto::toEntity)
            .collect(Collectors.toList());
        final List<Product> products = productRepository.findAllById(productsIds);

        MenuVerifier.validateMenuPrice(menuCreateRequest.getPrice(), menuProducts, products);

        final Menu savedMenu = menuRepository.save(menuCreateRequest.toEntity());
        final Long menuId = savedMenu.getId();
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.changeMenuId(menuId);
        }
        menuProductRepository.saveAll(menuProducts);

        return MenuResponseDto.from(savedMenu, menuProducts);
    }

    private void validateMenuGroupExistence(Long menuGroupId) {
        if (Objects.isNull(menuGroupId) || !menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴그룹입니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDto> list() {
        final List<Menu> menus = menuRepository.findAll();

        List<MenuResponseDto> menuResponses = new ArrayList<>();
        for (final Menu menu : menus) {
            List<MenuProduct> menuProducts = menuProductRepository.findAllByMenuId(menu.getId());
            menuResponses.add(MenuResponseDto.from(menu, menuProducts));
        }
        return menuResponses;
    }
}

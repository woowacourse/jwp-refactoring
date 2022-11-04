package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuCreationDto;
import kitchenpos.menu.application.dto.MenuDto;
import kitchenpos.menu.application.dto.MenuProductCreationDto;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuProductRepository menuProductRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuDto create(final MenuCreationDto menuCreationDto) {
        final List<MenuProduct> menuProducts = getMenuProducts(menuCreationDto.getMenuProducts());
        final Menu menu = new Menu(menuCreationDto.getName(), menuCreationDto.getPrice(),
                menuCreationDto.getMenuGroupId(), menuProducts);
        final Menu savedMenu = menuRepository.save(menu);
        final List<MenuProduct> savedMenuProducts = saveMenuProducts(savedMenu.getId(), menuProducts);

        return MenuDto.from(
                new Menu(savedMenu.getId(),
                        savedMenu.getName(),
                        savedMenu.getPrice(),
                        savedMenu.getMenuGroupId(),
                        savedMenuProducts));
    }

    private List<MenuProduct> getMenuProducts(final List<MenuProductCreationDto> menuProductCreationDtos) {
        return menuProductCreationDtos.stream()
                .map(menuProductDto -> {
                    final Product product = productRepository.findById(menuProductDto.getProductId())
                            .orElseThrow(IllegalArgumentException::new);
                    return new MenuProduct(product, menuProductDto.getQuantity());
                })
                .collect(Collectors.toList());
    }

    private List<MenuProduct> saveMenuProducts(final Long menuId, final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(menuProduct -> menuProductRepository.save(
                        new MenuProduct(menuId, menuProduct.getProduct(), menuProduct.getQuantity())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MenuDto> getMenus() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(menu -> {
                    final List<MenuProduct> menuProducts = menuProductRepository.findAllByMenuId(menu.getId());
                    return new Menu(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(), menuProducts);
                })
                .map(MenuDto::from)
                .collect(Collectors.toList());
    }
}

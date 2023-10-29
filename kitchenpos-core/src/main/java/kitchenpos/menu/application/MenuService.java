package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.Products;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuProductRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.product.domain.repository.ProductRepository;
import kitchenpos.common.dto.request.CreateMenuRequest;
import kitchenpos.common.dto.request.MenuProductDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    public Menu create(final CreateMenuRequest menuRequest) {
        final MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                                                       .orElseThrow(() -> new IllegalArgumentException(
                                                               "존재하지 않는 메뉴 그룹입니다."
                                                       ));
        final Menu menu = Menu.of(menuGroup, menuRequest.getName(), menuRequest.getPrice());
        final Menu savedMenu = menuRepository.save(menu);
        final Products products = findProducts(menuRequest);
        createMenuProducts(menuRequest, savedMenu, products);
        return savedMenu;
    }

    private void createMenuProducts(final CreateMenuRequest menuRequest, final Menu savedMenu, final Products products) {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (final MenuProductDto menuProductDto : menuRequest.getMenuProducts()) {
            final Product product = products.findProductById(menuProductDto.getProductId());
            final MenuProduct menuProduct = new MenuProduct(savedMenu, product, menuProductDto.getQuantity());
            menuProducts.add(menuProduct);
        }
        menuProductRepository.saveAll(menuProducts);
    }

    private Products findProducts(final CreateMenuRequest menuRequest) {
        final List<Product> products = new ArrayList<>();
        for (final MenuProductDto menuProductDto : menuRequest.getMenuProducts()) {
            final Product product = productRepository.findById(menuProductDto.getProductId())
                                                     .orElseThrow(IllegalArgumentException::new);
            products.add(product);
        }
        return new Products(products);
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        final List<Menu> menus = menuRepository.findAll();
        for (final Menu menu : menus) {
            menuProductRepository.findAllByMenuId(menu.getId());
        }
        return menus;
    }
}

package kitchenpos.menu.application;

import kitchenpos.exception.NotFoundMenuGroupException;
import kitchenpos.exception.NotFoundProductException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.menu.ui.dto.MenuProductDto;
import kitchenpos.menu.ui.dto.MenuRequest;
import kitchenpos.menu.ui.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public MenuResponse create(final MenuRequest menuRequest) {
        final MenuGroup menuGroup =
                menuGroupRepository.findById(menuRequest.getMenuGroupId())
                                   .orElseThrow(() -> new NotFoundMenuGroupException("해당 메뉴 그룹이 존재하지 않습니다."));
        final List<MenuProduct> menuProducts = convertToMenuProducts(menuRequest.getMenuProducts());
        final Menu menu = menuRequest.toEntity(menuGroup, menuProducts);
        final Menu savedMenu = menuRepository.save(menu);

        return MenuResponse.from(savedMenu);
    }

    private List<MenuProduct> convertToMenuProducts(final List<MenuProductDto> menuProductDtos) {
        final Map<Long, Product> products = findAllProducts(menuProductDtos);

        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductDto menuProductDto : menuProductDtos) {
            final Product product = products.get(menuProductDto.getProductId());
            menuProducts.add(menuProductDto.toEntity(product));
        }

        return menuProducts;
    }

    private Map<Long, Product> findAllProducts(final List<MenuProductDto> menuProductDtos) {
        final List<Long> productIds = menuProductDtos.stream()
                                                     .map(MenuProductDto::getProductId)
                                                     .collect(Collectors.toList());
        final Map<Long, Product> products = findProductsWithId(productIds);

        return products;
    }

    private Map<Long, Product> findProductsWithId(final List<Long> productIds) {
        final Map<Long, Product> products = productRepository.findAllByIdIn(productIds).stream()
                                                             .collect(Collectors.toMap(Product::getId, product -> product));
        validateProducts(productIds, products);

        return products;
    }

    private static void validateProducts(final List<Long> productIds, final Map<Long, Product> products) {
        if (products.size() != productIds.size()) {
            throw new NotFoundProductException("존재하지 않는 상품이 있습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            menu.updateMenuProducts(menuProductRepository.findAllByMenuId(menu.getId()));
        }

        return menus.stream()
                    .map(MenuResponse::from)
                    .collect(Collectors.toList());
    }
}

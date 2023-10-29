package kitchenpos.menu.application;

import kitchenpos.exception.NotFoundMenuGroupException;
import kitchenpos.exception.NotFoundProductException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.ui.dto.MenuProductDto;
import kitchenpos.menu.ui.dto.MenuRequest;
import kitchenpos.menu.ui.dto.MenuResponse;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public MenuResponse create(final MenuRequest menuRequest) {
        final Long menuGroupId = menuRequest.getMenuGroupId();
        validateMenuGroup(menuGroupId);

        final Map<Long, Product> products = findAllProducts(menuRequest.getMenuProducts());
        final MenuProducts menuProducts = convertToMenuProducts(menuRequest.getMenuProducts(), products);
        final Menu menu = menuRequest.toEntity(menuGroupId, menuProducts);

        final Menu savedMenu = menuRepository.save(menu);

        return MenuResponse.from(savedMenu);
    }

    private void validateMenuGroup(final Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new NotFoundMenuGroupException("해당 메뉴 그룹이 존재하지 않습니다.");
        }
    }

    private Map<Long, Product> findAllProducts(final List<MenuProductDto> menuProducts) {
        final List<Long> productIds = menuProducts.stream()
                                                  .map(MenuProductDto::getProductId)
                                                  .collect(Collectors.toList());

        return findProductsWithId(productIds);
    }

    private Map<Long, Product> findProductsWithId(final List<Long> productIds) {
        final Map<Long, Product> products = productRepository.findAllByIdIn(productIds).stream()
                                                             .collect(Collectors.toMap(Product::getId, product -> product));
        validateProducts(productIds, products);

        return products;
    }

    private void validateProducts(final List<Long> productIds, final Map<Long, Product> products) {
        if (products.size() != productIds.size()) {
            throw new NotFoundProductException("존재하지 않는 상품이 있습니다.");
        }
    }

    private MenuProducts convertToMenuProducts(
            final List<MenuProductDto> menuProductDtos,
            final Map<Long, Product> products
    ) {
        final List<MenuProduct> menuProducts =
                menuProductDtos.stream()
                               .map(menuProductDto -> createMenuProduct(products, menuProductDto))
                               .collect(Collectors.toList());

        return new MenuProducts(menuProducts);
    }

    private static MenuProduct createMenuProduct(final Map<Long, Product> products, final MenuProductDto menuProductDto) {
        final Product product = products.get(menuProductDto.getProductId());
        return menuProductDto.toEntity(product.getPrice());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();


        return menus.stream()
                    .map(MenuResponse::from)
                    .collect(Collectors.toList());
    }
}

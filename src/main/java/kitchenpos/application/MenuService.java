package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository,
                       final MenuGroupRepository menuGroupRepository,
                       final MenuProductRepository menuProductRepository,
                       final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        validateMenuGroupExist(request.getMenuGroupId());

        final List<MenuProductRequest> menuProductRequests = request.getMenuProducts();
        validateMenuPriceAppropriate(request.getPrice(), menuProductRequests);

        final Menu menu = new Menu(request.getName(), request.getPrice(), request.getMenuGroupId());
        final Menu savedMenu = menuRepository.save(menu);
        final List<MenuProduct> savedMenuProducts = saveMenuProduct(menuProductRequests, savedMenu.getId());
        return MenuResponse.of(savedMenu, savedMenuProducts);
    }

    public List<MenuResponse> findAll() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(menu -> MenuResponse.of(menu, menuProductRepository.findAllByMenuId(menu.getId())))
                .collect(Collectors.toList());
    }

    private void validateMenuGroupExist(final Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("포함된 메뉴 그룹이 있어야 합니다.");
        }
    }

    private void validateMenuPriceAppropriate(final BigDecimal menuPrice,
                                              final List<MenuProductRequest> menuProductRequests) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())));
        }

        if (menuPrice.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 메뉴 상품들의 총액보다 비쌀 수 없습니다.");
        }
    }

    private List<MenuProduct> saveMenuProduct(final List<MenuProductRequest> menuProductRequests, final Long menuId) {
        final List<MenuProduct> menuProducts = menuProductRequests.stream()
                .map(menuProductRequest -> new MenuProduct(menuId, menuProductRequest.getProductId(),
                        menuProductRequest.getQuantity()))
                .collect(Collectors.toList());

        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (MenuProduct menuProduct : menuProducts) {
            final MenuProduct savedMenuProduct = menuProductRepository.save(menuProduct);
            savedMenuProducts.add(savedMenuProduct);
        }

        return savedMenuProducts;
    }
}

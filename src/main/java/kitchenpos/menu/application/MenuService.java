package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.menu.dto.MenuCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
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

    public Menu create(final MenuCreateRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("요청한 menuGroupId에 해당하는 MenuGroup이 존재하지 않습니다."));
        Map<Long, Product> productsById = productRepository.findAllByIdIn(request.getProductIds()).stream()
                .collect(Collectors.toMap(Product::getId, product -> product));
        validateRequestedProductsExist(request.getProductIds(), productsById);

        final Menu menu = Menu.create(request.getName(), BigDecimal.valueOf(request.getPrice()), menuGroup);
        createMenuProducts(request, productsById, menu);
        return menuRepository.save(menu);
    }

    private void validateRequestedProductsExist(final List<Long> requestedProductIds, final Map<Long, Product> productsById) {
        if (requestedProductIds.size() != productsById.size()) {
            throw new IllegalArgumentException("요청한 상품들 중 존재하지 않는 상품이 존재합니다.");
        }
    }

    private void createMenuProducts(final MenuCreateRequest request, final Map<Long, Product> productsById, final Menu menu) {
        final List<MenuProduct> menuProducts = request.getMenuProducts().stream()
                .map(menuProductRequest -> {
                    long productId = menuProductRequest.getProductId();
                    long quantity = menuProductRequest.getQuantity();
                    return MenuProduct.create(menu, productsById.get(productId), quantity);
                }).collect(Collectors.toList());
        menu.updateMenuProducts(menuProducts);
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}

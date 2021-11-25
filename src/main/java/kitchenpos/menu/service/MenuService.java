package kitchenpos.menu.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.common.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.service.dto.MenuRequest;
import kitchenpos.menu.service.dto.MenuResponse;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
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

    public MenuResponse create(final MenuRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new NoSuchElementException();
        }
        final List<MenuProduct> menuProducts = request.getMenuProducts().stream()
            .map(menuProduct -> new MenuProduct(menuProduct.getProductId(), menuProduct.getQuantity()))
            .collect(Collectors.toList());
        final List<Product> products = findProducts(menuProducts);
        Menu menu = new Menu(request.getName(), request.getPrice(), request.getMenuGroupId(), menuProducts);
        validateCreation(menu, products);

        final Menu savedMenu = menuRepository.save(menu);
        menuProductRepository.saveAll(menuProducts);

        return MenuResponse.of(savedMenu);
    }

    private List<Product> findProducts(List<MenuProduct> menuProducts) {
        List<Long> productIds = menuProducts.stream()
            .map(MenuProduct::getProductId)
            .collect(Collectors.toList());
        return productRepository.findAllByIdIn(productIds);
    }

    private void validateCreation(Menu menu, List<Product> products) {
        if (menu.getMenuProducts().size() != products.size()) {
            throw new NoSuchElementException();
        }

        Price sum = products.stream()
            .map(Product::getPrice)
            .reduce(Price::sum)
            .orElseGet(() -> Price.ZERO);

        if (menu.getPrice().isGreater(sum)) {
            throw new IllegalStateException("메뉴의 가격은 상품들의 가격 합보다 클 수 없습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
            .map(MenuResponse::of)
            .collect(Collectors.toList());
    }
}

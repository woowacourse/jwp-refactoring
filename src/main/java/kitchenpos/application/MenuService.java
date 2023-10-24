package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.menu.MenuRequest;
import kitchenpos.application.dto.menu.MenuResponse;
import kitchenpos.application.dto.menu.ProductQuantityDto;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        final MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
            .orElseThrow(IllegalArgumentException::new);

        final Menu menu = new Menu(menuRequest.getName(), Price.from(menuRequest.getPrice()), menuGroup);

        final List<MenuProduct> menuProducts = getMenuProductsFromRequest(menuRequest.getMenuProducts(), menu);
        menu.addMenuProducts(menuProducts);
        menuRepository.save(menu);
        menuProductRepository.saveAll(menuProducts);

        return MenuResponse.from(menu);
    }

    private List<MenuProduct> getMenuProductsFromRequest(final List<ProductQuantityDto> requests, final Menu menu) {
        return requests.stream()
            .map(request -> new MenuProduct(menu, getProduct(request.getProductId()), request.getQuantity()))
            .collect(Collectors.toList());
    }

    private Product getProduct(final Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }
}

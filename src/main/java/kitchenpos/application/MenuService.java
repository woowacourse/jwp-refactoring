package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.dto.MenuAssembler;
import kitchenpos.ui.dto.MenuProductRequest;
import kitchenpos.ui.dto.MenuRequest;
import kitchenpos.ui.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository,
        final MenuGroupRepository menuGroupRepository, final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        Long menuGroupId = menuRequest.getMenuGroupId();
        MenuGroup menuGroup = menuGroupRepository.findById(menuGroupId)
            .orElseThrow(
                () -> new IllegalArgumentException(menuGroupId + " : MenuGroupID가 존재하지 않습니다."));
        List<Product> products = getProducts(menuRequest.getMenuProducts());

        Menu menu = MenuAssembler.assemble(menuRequest, menuGroup, products);
        Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.of(savedMenu);
    }

    private List<Product> getProducts(List<MenuProductRequest> menuProducts) {
        List<Product> products = productRepository.findAllById(getProductIds(menuProducts));
        if (menuProducts.size() != products.size()) {
            throw new IllegalArgumentException("MenuProduct에 해당하는 Product가 유효하지 않습니다.");
        }
        return products;
    }

    private List<Long> getProductIds(List<MenuProductRequest> menuProducts) {
        return menuProducts.stream()
            .map(MenuProductRequest::getProductId)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        List<Menu> foundMenus = menuRepository.findAll();
        return MenuResponse.listOf(foundMenus);
    }
}

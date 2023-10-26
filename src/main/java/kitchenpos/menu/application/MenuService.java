package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final ProductService productService;
    private final MenuGroupService menuGroupService;

    public MenuService(final MenuRepository menuRepository,
                       final ProductService productService,
                       final MenuGroupService menuGroupService) {
        this.menuRepository = menuRepository;
        this.productService = productService;
        this.menuGroupService = menuGroupService;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        menuGroupService.validateExistenceById(request.getMenuGroupId());

        final List<MenuProduct> menuProducts = getMenuProducts(request);
        final Menu menu = menuRepository.save(new Menu(
                request.getName(),
                request.getPrice(),
                request.getMenuGroupId(),
                menuProducts));
        return MenuResponse.from(menu);
    }

    private List<MenuProduct> getMenuProducts(final MenuCreateRequest request) {
        return request.getMenuProducts().stream()
                .map(this::createMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct createMenuProduct(final MenuProductRequest menuProductRequest) {
        final Product product = productService.findByIdOrThrow(menuProductRequest.getProductId());
        return new MenuProduct(product, menuProductRequest.getQuantity());
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}

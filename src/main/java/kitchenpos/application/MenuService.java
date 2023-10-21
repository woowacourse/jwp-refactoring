package kitchenpos.application;

import kitchenpos.dao.MenuRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.MenuCreateRequest;
import kitchenpos.ui.dto.MenuResponse;
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
                .map(menuProductRequest -> {
                    final Product product = productService.findByIdOrThrow(menuProductRequest.getProductId());
                    return new MenuProduct(product, menuProductRequest.getQuantity());
                })
                .collect(Collectors.toList());
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    public void validateExistenceByIds(final List<Long> menuIds) {
        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 주문 항목입니다.");
        }
    }
}

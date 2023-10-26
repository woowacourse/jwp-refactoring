package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuProductCreateRequest;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu_group.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu_group.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository, final MenuGroupRepository menuGroupRepository,
                       final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        validateMenuGroup(request.getMenuGroupId());
        final MenuGroup menuGroup = menuGroupRepository.getById(request.getMenuGroupId());
        final List<MenuProduct> menuProducts = createMenuProduct(request.getMenuProducts());
        final Menu menu = Menu.forSave(request.getName(), menuProducts, menuGroup.getId());
        validateMenuPrice(request.getPrice(), menu);

        return MenuResponse.from(menuRepository.save(menu));
    }

    private void validateMenuGroup(final Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다.");
        }
    }

    private List<MenuProduct> createMenuProduct(final List<MenuProductCreateRequest> requests) {
        return requests.stream()
            .map(request -> MenuProduct.forSave(productRepository.getById(request.getProductId()),
                                                request.getQuantity()))
            .collect(Collectors.toList());
    }

    private void validateMenuPrice(final int price, final Menu menu) {
        if (!menu.hasSamePrice(price)) {
            throw new IllegalArgumentException("메뉴 가격이 일치하지 않습니다.");
        }
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }
}

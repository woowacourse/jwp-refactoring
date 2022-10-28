package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.exception.CustomErrorCode;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.Quantity;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.repository.ProductRepository;
import kitchenpos.menu.ui.dto.MenuCreateRequest;
import kitchenpos.menu.ui.dto.MenuProductCreateRequest;
import kitchenpos.menu.ui.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository, final MenuGroupRepository menuGroupRepository, final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        validateExistMenuGroup(request);
        // TODO: 2022/10/26 to pretty
        final List<MenuProduct> menuProducts = mapToMenuProduct(request.getMenuProducts());
        return MenuResponse.from(menuRepository.save(request.toMenu(menuProducts)));
    }

    private void validateExistMenuGroup(final MenuCreateRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new NotFoundException(CustomErrorCode.MENU_GROUP_NOT_FOUND_ERROR);
        }
    }

    private List<MenuProduct> mapToMenuProduct(final List<MenuProductCreateRequest> request) {
        return request.stream()
                .map(mpr -> new MenuProduct(
                        findProductById(mpr.getProductId()),
                        new Quantity(mpr.getQuantity())
                )).collect(Collectors.toList());
    }

    private Product findProductById(final Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(CustomErrorCode.PRODUCT_NOT_FOUND_ERROR));
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }
}

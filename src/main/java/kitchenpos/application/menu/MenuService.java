package kitchenpos.application.menu;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Quantity;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.exception.CustomError;
import kitchenpos.exception.NotFoundException;
import kitchenpos.repository.menu.MenuGroupRepository;
import kitchenpos.repository.menu.MenuRepository;
import kitchenpos.repository.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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
        final List<MenuProduct> menuProducts = mapToMenuProduct(request.getMenuProducts());
        return MenuResponse.from(menuRepository.save(request.toMenu(menuProducts)));
    }

    private void validateExistMenuGroup(final MenuCreateRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new NotFoundException(CustomError.MENU_GROUP_NOT_FOUND_ERROR);
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
                .orElseThrow(() -> new NotFoundException(CustomError.PRODUCT_NOT_FOUND_ERROR));
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }
}

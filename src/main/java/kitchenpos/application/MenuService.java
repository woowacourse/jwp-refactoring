package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.application.dto.MenuProductCreateRequest;
import kitchenpos.application.dto.MenuResponse;
import kitchenpos.application.dto.MenuUpdateValuesRequest;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository,
                       final MenuGroupRepository menuGroupRepository,
                       final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public MenuResponse create(final MenuCreateRequest request) {
        validateMenuGroup(request);

        final Menu menu = new Menu(
            request.getName(), request.getPrice(), request.getMenuGroupId(), new MenuProducts(request.getMenuProducts()
            .stream()
            .map(this::createMenuProduct)
            .collect(Collectors.toList())));

        return MenuResponse.createResponse(menuRepository.save(menu));
    }

    private void validateMenuGroup(final MenuCreateRequest request) {
        if (request.getMenuGroupId() != null && !menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    private MenuProduct createMenuProduct(final MenuProductCreateRequest request) {
        final Product product = productRepository.findById(request.getProductId())
            .orElseThrow(IllegalArgumentException::new);
        return new MenuProduct(product.getId(), product.getPrice(), request.getQuantity());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
            .map(MenuResponse::createResponse)
            .collect(Collectors.toList());
    }

    public void updateValues(final Long id, final MenuUpdateValuesRequest request) {
        final Menu menu = menuRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));

        menu.updateValues(request.getName(), request.getPrice());
    }
}

package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.request.MenuCreateRequest;
import kitchenpos.menu.dto.request.MenuProductCreateRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.doamin.Product;
import kitchenpos.product.repository.ProductRepository;
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
    public Long create(final MenuCreateRequest request) {
        validateMenuGroup(request);
        final List<MenuProductCreateRequest> menuProductCreateRequests = request.getMenuProductRequests();

        final Menu menu = new Menu(request.getName(), new Price(request.getPrice()), request.getMenuGroupId());
        validateMenuPrice(menuProductCreateRequests, menu);

        final Menu savedMenu = menuRepository.save(menu);
        saveMenuProduct(menuProductCreateRequests, savedMenu);
        return savedMenu.getId();
    }

    private void validateMenuGroup(final MenuCreateRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }
    }

    private void validateMenuPrice(final List<MenuProductCreateRequest> menuProductCreateRequests, final Menu menu) {
        Price sum = Price.ZERO;
        for (final MenuProductCreateRequest menuProductCreateRequest : menuProductCreateRequests) {
            final Product product = productRepository.findById(menuProductCreateRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
            sum = sum.add(product.getPrice().multiply(menuProductCreateRequest.getQuantity()));
        }
        menu.validateMenuPrice(sum);
    }

    private void saveMenuProduct(final List<MenuProductCreateRequest> menuProductCreateRequests, final Menu savedMenu) {
        for (final MenuProductCreateRequest menuProductCreateRequest : menuProductCreateRequests) {
            final MenuProduct menuProduct = new MenuProduct(
                    savedMenu,
                    menuProductCreateRequest.getProductId(),
                    menuProductCreateRequest.getQuantity()
            );
            MenuProduct savedMenuProduct = menuProductRepository.save(menuProduct);
            savedMenu.addMenuProduct(savedMenuProduct);
        }
    }

    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }
}

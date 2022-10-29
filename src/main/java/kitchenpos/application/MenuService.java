package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.application.dto.MenuProductCreateRequest;
import kitchenpos.application.dto.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
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
        if (request.getMenuGroupId() == null) {
            throw new IllegalArgumentException();
        }
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        Menu menu = menuRepository.save(new Menu(request.getName(), request.getPrice(), request.getMenuGroupId()));
        menu.addMenuProducts(new MenuProducts(request.getMenuProducts()
            .stream()
            .map(this::createMenuProduct)
            .collect(Collectors.toList())));

        return MenuResponse.createResponse(menuRepository.save(menu));
    }

    private MenuProduct createMenuProduct(final MenuProductCreateRequest request) {
        Product product = productRepository.findById(request.getProductId())
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
}

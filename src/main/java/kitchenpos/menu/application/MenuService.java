package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;

    public MenuService(
            ProductRepository productRepository,
            MenuGroupRepository menuGroupRepository,
            MenuRepository menuRepository
    ) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public MenuResponse create(MenuCreateRequest request) {
        List<MenuProduct> menuProducts = request.getMenuProducts()
                .stream()
                .map(it -> new MenuProduct(productRepository.getById(it.getProductId()), it.getQuantity()))
                .toList();
        Menu menu = new Menu(
                request.getName(),
                request.getPrice(),
                menuGroupRepository.getById(request.getMenuGroupId()),
                menuProducts
        );
        return MenuResponse.from(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::from)
                .toList();
    }
}

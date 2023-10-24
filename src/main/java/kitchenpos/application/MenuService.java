package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.dto.CreateMenuProductRequest;
import kitchenpos.ui.dto.CreateMenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final CreateMenuRequest request) {
        final MenuGroup savedMenuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                                                            .orElseThrow(() -> new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다."));

        final List<MenuProduct> menuProducts = processMenuProducts(request);

        final Menu menu = new Menu(request.getName(), request.getPrice(), savedMenuGroup);
        menu.addMenuProducts(new MenuProducts(menuProducts));

        return menuRepository.save(menu);
    }

    private List<MenuProduct> processMenuProducts(final CreateMenuRequest request) {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (final CreateMenuProductRequest createMenuProductRequest : request.getMenuProducts()) {
            final Product product = productRepository.findById(createMenuProductRequest.getProductId())
                                                     .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

            menuProducts.add(new MenuProduct(product, createMenuProductRequest.getQuantity()));
        }
        return menuProducts;
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}

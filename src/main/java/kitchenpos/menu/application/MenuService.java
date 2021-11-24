package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.ui.dto.request.MenuRequest;
import kitchenpos.menu.ui.dto.response.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

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
    public MenuResponse create(final MenuRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다."));
        final List<MenuProduct> menuProducts = request.getMenuProducts()
                .stream()
                .map(menuProduct -> {
                    Product product = productRepository.findById(menuProduct.getProductId())
                            .orElseThrow(IllegalArgumentException::new);
                    return MenuProduct.create(product, menuProduct.getQuantity());
                })
                .collect(toList());

        final Menu savedMenu = menuRepository.save(Menu.create(request.getName(), request.getPrice(), menuGroup, menuProducts));

        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.addMenu(savedMenu);
        }
        menuProductRepository.saveAll(menuProducts);

        return MenuResponse.create(savedMenu);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::create)
                .collect(toList());
    }
}

package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.dto.request.MenuRequest;
import kitchenpos.ui.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

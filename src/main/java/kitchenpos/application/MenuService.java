package kitchenpos.application;

import kitchenpos.application.dto.MenuRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public Menu create(final MenuRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 그룹이 존재 해야합니다");
        }
        Menu menu = getMenu(request);
        return menuRepository.save(menu);
    }

    private Menu getMenu(MenuRequest request) {
        List<MenuProduct> menuProducts = toMenuProducts(request);
        return new Menu(request.getName(), request.getPrice(), request.getMenuGroupId(), menuProducts);
    }

    private List<MenuProduct> toMenuProducts(MenuRequest request) {
        Map<Long, Product> products = productRepository.findAllByIdIn(request.getMenuProductIds()).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        return request.getMenuProductRequests().stream()
                .map(it -> new MenuProduct(products.get(it.getProductId()), it.getQuantity()))
                .collect(Collectors.toList());
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}

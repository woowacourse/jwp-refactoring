package kitchenpos.menu.appllication;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.request.MenuProductCreateRequest;
import kitchenpos.menu.dto.request.MenuCreateRequest;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Menu create(final MenuCreateRequest request) {
        final Menu menu = new Menu(
                request.name(),
                request.price(),
                menuGroupRepository.getById(request.menuGroupId())
        );
        menu.addMenuProducts(createMenuProducts(request));
        return menuRepository.save(menu);
    }

    private List<MenuProduct> createMenuProducts(final MenuCreateRequest request) {
        final List<MenuProductCreateRequest> menuProductCreateRequests = request.menuProducts();
        final List<Long> productIds = menuProductCreateRequests.stream()
                .map(MenuProductCreateRequest::productId)
                .collect(Collectors.toUnmodifiableList());
        final List<Product> products = productRepository.getAllById(productIds);

        return IntStream.range(0, products.size())
                .mapToObj(index -> new MenuProduct(
                                products.get(index),
                                menuProductCreateRequests.get(index).quantity()
                        )
                )
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}

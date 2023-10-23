package kitchenpos.menu.appllication;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.request.MenuCreateRequest;
import kitchenpos.menu.dto.request.MenuProductCreateRequest;
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

        final List<Long> productIds = parseProcess(menuProductCreateRequests, MenuProductCreateRequest::productId);
        final List<Product> products = productRepository.getAllById(productIds);

        final List<Long> quantities = parseProcess(menuProductCreateRequests, MenuProductCreateRequest::quantity);

        return IntStream.range(0, products.size())
                .mapToObj(index -> new MenuProduct(
                                products.get(index),
                                quantities.get(index)
                        )
                )
                .collect(Collectors.toUnmodifiableList());
    }

    private <R, T> List<T> parseProcess(
            List<R> requests,
            Function<R, T> processor
    ) {
        return requests.stream()
                .map(processor)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}

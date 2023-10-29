package application;

import application.dto.MenuCreateRequest;
import application.dto.MenuCreateRequest.MenuProductRequest;
import application.dto.MenuResponse;
import domain.Menu;
import domain.MenuGroup;
import domain.MenuName;
import domain.MenuValidator;
import domain.Product;
import domain.menu_product.MenuProduct;
import domain.menu_product.MenuProductValidator;
import domain.menu_product.MenuProducts;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.MenuRepository;
import support.AggregateReference;
import vo.Price;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;
    private final MenuProductValidator menuProductValidator;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuValidator menuValidator,
            final MenuProductValidator menuProductValidator
    ) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
        this.menuProductValidator = menuProductValidator;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        final MenuName menuName = new MenuName(request.getName());
        final Price menuPrice = new Price(request.getPrice());

        final MenuProducts menuProducts = createMenuProducts(request);
        final AggregateReference<MenuGroup> menuGroupId = new AggregateReference<>(request.getMenuGroupId());
        final Menu menu = new Menu(menuName, menuPrice, menuProducts, menuGroupId, menuValidator);

        return MenuResponse.from(menuRepository.save(menu));
    }

    private MenuProducts createMenuProducts(final MenuCreateRequest request) {
        return new MenuProducts(request.getMenuProducts()
                .stream()
                .map(this::createMenuProduct)
                .collect(Collectors.toUnmodifiableList())
        );
    }

    private MenuProduct createMenuProduct(final MenuProductRequest request) {
        final AggregateReference<Product> productId = new AggregateReference<>(request.getProductId());
        return new MenuProduct(productId, request.getQuantity(), menuProductValidator);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAllByFetch()
                .stream()
                .map(MenuResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }
}

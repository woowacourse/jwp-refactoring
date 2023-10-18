package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.vo.Price;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.dto.request.MenuCreateRequest;
import kitchenpos.ui.dto.request.MenuProductCreateRequest;
import kitchenpos.ui.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public MenuResponse create(final MenuCreateRequest menuInput) {
        final MenuGroup menuGroup = menuGroupRepository.findMandatoryById(menuInput.getMenuGroupId());
        final Price price = new Price(menuInput.getPrice());
        final Menu menu = new Menu(menuInput.getName(), price, menuGroup);
        final List<MenuProduct> menuProducts = createMenuProducts(menu, menuInput.getMenuProducts());
        validatePrice(price, menuProducts);
        menu.addMenuProducts(menuProducts);
        return MenuResponse.from(menuRepository.save(menu));
    }

    private List<MenuProduct> createMenuProducts(
            final Menu menu,
            final List<MenuProductCreateRequest> menuProductsInput
    ) {
       return menuProductsInput.stream()
                .map(req -> {
                    final Product product = productRepository.findMandatoryById(req.getProductId());
                    return new MenuProduct(req.getQuantity(), menu, product);
                })
                .collect(Collectors.toUnmodifiableList());
    }

    private void validatePrice(final Price price, final List<MenuProduct> menuProducts) {
        final Price productsSum = menuProducts.stream()
                .map(MenuProduct::getPrice)
                .reduce(Price.init(), Price::add);
        if (price.biggerThan(productsSum)) {
            throw new IllegalArgumentException();
        }
    }

    public List<MenuResponse> list() {
        return menuRepository.joinAllMenuProducts().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }
}

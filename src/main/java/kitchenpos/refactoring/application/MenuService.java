package kitchenpos.refactoring.application;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import kitchenpos.refactoring.application.dto.MenuProductRequest;
import kitchenpos.refactoring.application.dto.MenuRequest;
import kitchenpos.refactoring.application.dto.MenuResponse;
import kitchenpos.refactoring.domain.Menu;
import kitchenpos.refactoring.domain.MenuGroup;
import kitchenpos.refactoring.domain.MenuGroupRepository;
import kitchenpos.refactoring.domain.MenuProduct;
import kitchenpos.refactoring.domain.MenuProductPriceCalculator;
import kitchenpos.refactoring.domain.MenuRepository;
import kitchenpos.refactoring.domain.Price;
import kitchenpos.refactoring.domain.Product;
import kitchenpos.refactoring.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final MenuProductPriceCalculator priceCalculator;


    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository,
            final MenuProductPriceCalculator priceCalculator
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.priceCalculator = priceCalculator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        Price price = new Price(menuRequest.getPrice());
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        List<MenuProduct> menuProducts = getMenuProducts(menuRequest);

        Map<Long, Product> products = getProducts(menuRequest);
        Price totalPrice = priceCalculator.calculateTotalPrice(menuProducts, products);

        Menu menu = Menu.of(
                totalPrice,
                menuRequest.getName(),
                price,
                menuGroup.getId(),
                menuProducts
        );

        return MenuResponse.from(menuRepository.save(menu));
    }

    private List<MenuProduct> getMenuProducts(MenuRequest menuRequest) {
        return menuRequest.getMenuProducts().stream()
                .map(menuProductRequest -> new MenuProduct(
                        menuProductRequest.getProductId(),
                        menuProductRequest.getQuantity()
                ))
                .collect(Collectors.toList());
    }

    private Map<Long, Product> getProducts(MenuRequest menuRequest) {
        List<Long> productIds = menuRequest.getMenuProducts().stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());

        return productRepository.findAllByIdIn(productIds)
                .stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
    }


    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}

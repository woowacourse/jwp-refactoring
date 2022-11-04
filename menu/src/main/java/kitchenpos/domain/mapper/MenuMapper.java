package kitchenpos.domain.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.vo.MenuPrice;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.dto.request.MenuCreateRequest;
import kitchenpos.ui.dto.request.MenuProductRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Component
public class MenuMapper {
    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuMapper(final ProductRepository productRepository, final MenuGroupRepository menuGroupRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public Menu mapFrom(final MenuCreateRequest request) {
        final var name = validateMenuName(request.getName());
        final var menuGroupId = validateMenuGroupId(request.getMenuGroupId());
        final var menuProducts = validateMenuProducts(request.getMenuProducts());
        final var price = validatePrice(menuProducts, request.getPrice());

        return new Menu(name, price, menuGroupId, menuProducts);
    }

    private String validateMenuName(final String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException();
        }

        return name;
    }

    private Long validateMenuGroupId(final Long menuGroupId) {
        return menuGroupRepository.getById(menuGroupId).getId();
    }

    private List<MenuProduct> validateMenuProducts(final List<MenuProductRequest> menuProducts) {
        if (CollectionUtils.isEmpty(menuProducts)) {
            throw new IllegalArgumentException("유효하지 않은 메뉴 프로덕트 : " + menuProducts);
        }

        return map(menuProducts);
    }

    private List<MenuProduct> map(final List<MenuProductRequest> request) {
        final var productIds = extractProductIds(request);
        final var products = fetchProductsIdIn(productIds);

        return convertToMenuProducts(request, products);
    }

    private List<Long> extractProductIds(final List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
    }

    private Map<Long, Product> fetchProductsIdIn(final List<Long> productIds) {
        return productRepository.findByIdIn(productIds)
                .stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
    }

    private List<MenuProduct> convertToMenuProducts(final List<MenuProductRequest> request,
                                                    final Map<Long, Product> products) {
        return request.stream()
                .map(req -> new MenuProduct(products.get(req.getProductId()), req.getQuantity()))
                .collect(Collectors.toList());
    }

    private MenuPrice validatePrice(final List<MenuProduct> menuProducts, final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (price.compareTo(totalPriceOfMenuProducts(menuProducts)) > 0) {
            throw new IllegalArgumentException();
        }

        return MenuPrice.from(price);
    }

    private BigDecimal totalPriceOfMenuProducts(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(menuProduct -> menuProduct.getProduct()
                        .getPrice()
                        .multiply(menuProduct.getQuantity())
                        .getValue()
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

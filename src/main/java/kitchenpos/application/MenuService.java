package kitchenpos.application;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuProductRequest;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.exception.MenuException.NotExistsProductException;
import kitchenpos.domain.exception.MenuException.PriceMoreThanProductsException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository,
                       final MenuProductRepository menuProductRepository,
                       final MenuGroupRepository menuGroupRepository,
                       final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupRepository.getById(menuRequest.getMenuGroupId());
        List<Product> products = productRepository.findAllById(menuRequest.getProductIds());
        validate(menuRequest, products);
        Menu menu = Menu.of(menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        Menu savedMenu = menuRepository.save(menu);
        List<MenuProduct> menuProducts = getMenuProducts(menu, menuRequest.getMenuProductRequests());

        menuProductRepository.saveAll(menuProducts);
        return savedMenu;
    }

    private void validate(final MenuRequest menuRequest, final List<Product> products) {
        if (products.size() != menuRequest.getProductSize()) {
            throw new NotExistsProductException();
        }

        BigDecimal totalMenuProductPrice = getTotalMenuProductPrice(menuRequest, products);
        if (menuRequest.getPrice().compareTo(totalMenuProductPrice) > 0) {
            throw new PriceMoreThanProductsException(menuRequest.getPrice(), totalMenuProductPrice);
        }
    }

    private BigDecimal getTotalMenuProductPrice(final MenuRequest menuRequest, final List<Product> products) {
        Map<Long, Product> productById = new HashMap<>();
        for (Product product : products) {
            productById.put(product.getId(), product);
        }

        BigDecimal totalMenuProductPrice = BigDecimal.ZERO;
        for (MenuProductRequest menuProductRequest : menuRequest.getMenuProductRequests()) {
            BigDecimal productPrice = productById.get(menuProductRequest.getProductId()).getPrice();
            BigDecimal menuProductPrice = productPrice.multiply(BigDecimal.valueOf(menuProductRequest.getQuantity()));
            totalMenuProductPrice = totalMenuProductPrice.add(menuProductPrice);
        }
        return totalMenuProductPrice;
    }

    private List<MenuProduct> getMenuProducts(final Menu menu, final List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(menuProductRequest -> new MenuProduct(menu,
                        menuProductRequest.getProductId(),
                        menuProductRequest.getQuantity()))
                .collect(Collectors.toList());
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}

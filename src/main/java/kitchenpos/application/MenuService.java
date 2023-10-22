package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.dto.request.MenuCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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
    public Menu create(final MenuCreateRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("요청한 menuGroupId에 해당하는 MenuGroup이 존재하지 않습니다."));

        final List<Product> products = productRepository.findAllByIdIn(request.getProductIds());
        Map<Long, Product> productsById = products.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));
        if (request.getProductIds().size() != productsById.size()) {
            throw new IllegalArgumentException("요청한 상품들 중 존재하지 않는 상품이 존재합니다.");
        }

        Menu menu = Menu.create(request.getName(), BigDecimal.valueOf(request.getPrice()), menuGroup);
        final List<MenuProduct> menuProducts = request.getMenuProducts().stream()
                .map(menuProductRequest -> {
                    long productId = menuProductRequest.getProductId();
                    long quantity = menuProductRequest.getQuantity();
                    return MenuProduct.create(menu, productsById.get(productId), quantity);
                }).collect(Collectors.toList());
        menu.updateMenuProducts(menuProducts);
        menuRepository.save(menu);
        return menu;
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}

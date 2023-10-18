package kitchenpos.application;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuCreationRequest;
import kitchenpos.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository,
            ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(MenuCreationRequest request) {
        MenuGroup menuGroup = findMenuGroupById(request.getMenuGroupId());
        Menu menu = Menu.createWithEmptyMenuProducts(request.getName(), request.getPrice(), menuGroup);

        initializeMenuProducts(request, menu);
        menuRepository.save(menu);

        return MenuResponse.from(menu);
    }

    private void initializeMenuProducts(MenuCreationRequest request, Menu menu) {
        List<Long> productIdsInMenuProduct = request.getProductIdsInMenuProduct();
        Map<Long, Long> productQuantitiesByProductId = request.getProductQuantitiesByProductId();
        Map<Long, Product> products = productRepository.findAllById(productIdsInMenuProduct)
                .stream()
                .collect((Collectors.toMap(Product::getId, product -> product)));

        if (productIdsInMenuProduct.size() != products.size()) {
            throw new IllegalArgumentException("메뉴에 존재하지 않는 상품이 포함되어 있습니다.");
        }

        productIdsInMenuProduct.forEach(id -> {
            Long quantity = productQuantitiesByProductId.get(id);
            Product product = products.get(id);

            menu.addMenuProduct(MenuProduct.create(menu, quantity, product));
        });
    }

    private MenuGroup findMenuGroupById(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new NoSuchElementException("ID에 해당하는 메뉴 그룹이 존재하지 않습니다."));
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

}

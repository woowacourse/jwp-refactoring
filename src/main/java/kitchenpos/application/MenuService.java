package kitchenpos.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreationRequest;
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
    public Menu create(MenuCreationRequest request) {
        MenuGroup menuGroup = findMenuGroupById(request.getMenuGroupId());
        Menu menu = Menu.createWithEmptyMenuProducts(request.getName(), request.getPrice(), menuGroup);

        initializeMenuProducts(request, menu);

        return menuRepository.save(menu);
    }

    private void initializeMenuProducts(MenuCreationRequest request, Menu menu) {
        List<Long> productIdsInMenuProduct = request.getProductIdsInMenuProduct();
        Map<Long, Long> productQuantitiesByProductId = request.getProductQuantitiesByProductId();
        Map<Long, Product> products = productRepository.findAllById(productIdsInMenuProduct)
                .stream()
                .collect((Collectors.toMap(Product::getId, product -> product)));

        productIdsInMenuProduct.forEach(id -> {
            Long quantity = productQuantitiesByProductId.get(id);
            Product product = products.get(id);

            menu.addMenuProduct(MenuProduct.create(menu, quantity, product));
        });
    }

    private MenuGroup findMenuGroupById(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다."));
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }

}

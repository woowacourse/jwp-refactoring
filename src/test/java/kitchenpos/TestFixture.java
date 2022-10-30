package kitchenpos;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.MenuService;
import kitchenpos.application.ProductService;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.application.dto.request.MenuRequest;
import kitchenpos.application.dto.request.ProductRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.OrderLineItemRepository;

@Component
@Transactional
public class TestFixture {

    private final ProductService productService;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuService menuService;
    private final TableGroupService tableGroupService;
    private final OrderLineItemRepository orderLineItemRepository;

    public TestFixture(ProductService productService, MenuGroupRepository menuGroupRepository, MenuService menuService,
                       TableGroupService tableGroupService, OrderLineItemRepository orderLineItemRepository) {
        this.productService = productService;
        this.menuGroupRepository = menuGroupRepository;
        this.menuService = menuService;
        this.tableGroupService = tableGroupService;
        this.orderLineItemRepository = orderLineItemRepository;
    }

    public Product 상품을_생성한다(String menuName, Long price) {
        return productService.create(
                new ProductRequest(menuName, BigDecimal.valueOf(price)));
    }

    public MenuGroup 메뉴_분류를_생성한다(String menuGroupName) {
        return menuGroupRepository.save(new MenuGroup(menuGroupName));
    }

    public Menu 메뉴를_각_상품당_여러개씩_넣어서_생성한다(String menuName, BigDecimal menuPrice, List<Product> products, Long menuGroupId, Long quantity) {
        List<MenuProductRequest> productRequests = products.stream()
                .map(product -> new MenuProductRequest(product.getId(), quantity))
                .collect(Collectors.toList());

        return menuService.create(
                new MenuRequest(
                        menuName,
                        menuPrice,
                        menuGroupId,
                        productRequests
                )
        );
    }

    public Menu 메뉴를_각_상품당_한개씩_넣어서_생성한다(String menuName, BigDecimal menuPrice, List<Product> products, Long menuGroupId) {
        return 메뉴를_각_상품당_여러개씩_넣어서_생성한다(menuName, menuPrice, products, menuGroupId, 1L);
    }
}

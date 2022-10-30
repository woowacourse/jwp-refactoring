package kitchenpos;

import java.math.BigDecimal;
import java.util.List;

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

    public Product 삼겹살() {
        return productService.create(
                new ProductRequest("삼겹살", BigDecimal.valueOf(1000L)));
    }

    public MenuGroup 삼겹살_종류() {
        return menuGroupRepository.save(new MenuGroup("삼겹살 종류"));
    }

    public Menu 삼겹살_메뉴() {
        Product product = 삼겹살();
        MenuGroup menuGroup = menuGroupRepository.save(삼겹살_종류());
        MenuProduct savedMenuProduct = new MenuProduct(product, 1L);

        return menuService.create(
                new MenuRequest(
                        "메뉴",
                        BigDecimal.valueOf(1000L),
                        menuGroup.getId(),
                        List.of(new MenuProductRequest(savedMenuProduct.getProduct().getId(),
                                savedMenuProduct.getQuantity()))
                )
        );
    }
}

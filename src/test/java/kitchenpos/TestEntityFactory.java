package kitchenpos;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.application.ProductService;
import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.application.dto.request.MenuRequest;
import kitchenpos.application.dto.request.OrderLineItemRequest;
import kitchenpos.application.dto.request.OrderRequest;
import kitchenpos.application.dto.request.ProductRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.OrderTableRepository;

@Component
@Transactional
public class TestEntityFactory {

    private final ProductService productService;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuService menuService;
    private final OrderTableRepository orderTableRepository;
    private final OrderService orderService;

    public TestEntityFactory(ProductService productService, MenuGroupRepository menuGroupRepository,
                             MenuService menuService, OrderTableRepository orderTableRepository,
                             OrderService orderService) {
        this.productService = productService;
        this.menuGroupRepository = menuGroupRepository;
        this.menuService = menuService;
        this.orderTableRepository = orderTableRepository;
        this.orderService = orderService;
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

    public OrderTable 주문_테이블을_생성한다(int numberOfGuests, boolean empty) {
        return orderTableRepository.save(new OrderTable(numberOfGuests, empty));
    }

    public Order 주문을_개수만큼_하도록_생성한다(Long orderTableId, List<Long> menuIds, Long quantity) {
        List<OrderLineItemRequest> orderLineItemRequests = menuIds.stream()
                .map(menuId -> new OrderLineItemRequest(menuId, quantity))
                .collect(Collectors.toList());
        OrderRequest request = new OrderRequest(
          orderTableId, orderLineItemRequests
        );
        return orderService.create(request);
    }
}

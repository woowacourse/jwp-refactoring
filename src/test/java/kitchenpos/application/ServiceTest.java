package kitchenpos.application;


import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.application.dto.MenuGroupRequestDto;
import kitchenpos.menu.application.dto.MenuRequestDto;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.application.dto.ProductRequestDto;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ServiceTest {

    @Autowired
    protected ProductService productService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected MenuGroupService menuGroupService;

    @Autowired
    protected TableGroupService tableGroupService;

    @Autowired
    protected TableService tableService;

    protected Product 상품_등록(final ProductRequestDto productRequestDto) {
        return productService.create(productRequestDto);
    }

    protected OrderTable 주문_테이블_등록(final OrderTable orderTable) {
        return tableService.create(orderTable);
    }

    protected TableGroup 테이블_그룹_등록(final TableGroup tableGroup) {
        return tableGroupService.create(tableGroup);
    }

    protected MenuGroup 메뉴_그룹_등록(final MenuGroupRequestDto menuGroup) {
        return menuGroupService.create(menuGroup);
    }

    protected Order 주문_등록(final Order order) {
        return orderService.create(order);
    }

    protected MenuResponse 메뉴_등록(final MenuRequestDto menuRequestDto) {
        return menuService.create(menuRequestDto);
    }
}

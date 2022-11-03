package kitchenpos.application;


import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.application.dto.MenuGroupRequestDto;
import kitchenpos.menu.application.dto.MenuRequestDto;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.dto.OrderRequestDto;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.application.dto.ProductRequestDto;
import kitchenpos.product.application.dto.ProductResponse;
import kitchenpos.support.DatabaseCleaner;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.application.dto.OrderTableRequestDto;
import kitchenpos.table.application.dto.OrderTableResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.table.application.dto.TableGroupRequestDto;
import kitchenpos.table.application.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void clear() {
        databaseCleaner.clear();
    }

    protected ProductResponse 상품_등록(final ProductRequestDto productRequestDto) {
        return productService.create(productRequestDto);
    }

    protected OrderTableResponse 주문_테이블_등록(final OrderTableRequestDto orderTableRequestDto) {
        return tableService.create(orderTableRequestDto);
    }

    protected TableGroupResponse 테이블_그룹_등록(final TableGroupRequestDto tableGroup) {
        return tableGroupService.create(tableGroup);
    }

    protected MenuGroup 메뉴_그룹_등록(final MenuGroupRequestDto menuGroup) {
        return menuGroupService.create(menuGroup);
    }

    protected OrderResponse 주문_등록(final OrderRequestDto order) {
        return orderService.create(order);
    }

    protected MenuResponse 메뉴_등록(final MenuRequestDto menuRequestDto) {
        return menuService.create(menuRequestDto);
    }
}

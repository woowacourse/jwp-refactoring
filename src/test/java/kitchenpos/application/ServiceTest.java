package kitchenpos.application;

import static kitchenpos.DtoFixture.getProductCreateRequest;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.request.menu.MenuCreateRequest;
import kitchenpos.ui.request.menu.MenuProductDto;
import kitchenpos.ui.request.menugroup.MenuGroupCreateRequest;
import kitchenpos.ui.request.order.OrderCreateRequest;
import kitchenpos.ui.request.prodcut.ProductCreateRequest;
import kitchenpos.ui.request.table.TableCreateRequest;
import kitchenpos.ui.request.tablegroup.TableGroupCreatRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public abstract class ServiceTest {

    @Autowired
    protected ProductService productService;

    @Autowired
    protected MenuGroupService menuGroupService;

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected TableService tableService;

    @Autowired
    protected TableGroupService tableGroupService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected OrderTableDao orderTableDao;

    protected Product 상품_등록(final ProductCreateRequest request) {
        return productService.create(request);
    }

    protected MenuGroup 메뉴_그룹_등록(final MenuGroupCreateRequest request) {
        return menuGroupService.create(request);
    }

    protected Menu 메뉴_등록(final MenuCreateRequest request) {
        return menuService.create(request);
    }

    protected OrderTable 테이블_등록(final TableCreateRequest request) {
        return tableService.create(request);
    }

    protected TableGroup 단체_지정(final TableGroupCreatRequest request) {
        return tableGroupService.create(request);
    }

    protected Order 주문_등록(final OrderCreateRequest request) {
        return orderService.create(request);
    }

    protected List<MenuProductDto> createMenuProductDtos() {
        final Product product = 상품_등록(getProductCreateRequest("마이쮸", BigDecimal.valueOf(800)));
        return List.of(new MenuProductDto(product.getId(), 1));
    }
}

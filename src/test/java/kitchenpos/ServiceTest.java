package kitchenpos;

import static kitchenpos.DtoFixture.getProductCreateRequest;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuGroupCreateRequest;
import kitchenpos.menu.dto.MenuProductDto;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableCreateRequest;
import kitchenpos.table.dto.TableGroupCreatRequest;
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

package kitchenpos.application;

import static kitchenpos.DomainFixture.getProduct;

import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.request.MenuCreateRequest;
import kitchenpos.ui.request.MenuProductDto;
import kitchenpos.ui.request.OrderCreateRequest;
import kitchenpos.ui.request.TableCreateRequest;
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

    protected Product 상품_등록(final Product product) {
        return productService.create(product);
    }

    protected MenuGroup 메뉴_그룹_등록(final MenuGroup menuGroup) {
        return menuGroupService.create(menuGroup);
    }

    protected Menu 메뉴_등록(final MenuCreateRequest request) {
        return menuService.create(request);
    }

    protected OrderTable 테이블_등록(final TableCreateRequest request) {
        return tableService.create(request);
    }

    protected TableGroup 단체_지정(final TableGroup tableGroup) {
        return tableGroupService.create(tableGroup);
    }

    protected Order 주문_등록(final OrderCreateRequest request) {
        return orderService.create(request);
    }

    protected List<MenuProductDto> createMenuProductDtos() {
        final Product product = 상품_등록(getProduct());
        return List.of(new MenuProductDto(product.getId(), 1));
    }
}

package kitchenpos;

import static kitchenpos.DtoFixture.getProductCreateRequest;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.MenuProductDto;
import kitchenpos.menu.dto.request.MenuCreateRequest;
import kitchenpos.menu.dto.request.MenuGroupCreateRequest;
import kitchenpos.menu.dto.response.MenuGroupResponse;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.request.ProductCreateRequest;
import kitchenpos.product.dto.response.ProductResponse;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.request.TableCreateRequest;
import kitchenpos.table.dto.request.TableGroupCreatRequest;
import kitchenpos.table.dto.response.TableResponse;
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

    protected ProductResponse 상품_등록(final ProductCreateRequest request) {
        return productService.create(request);
    }

    protected MenuGroupResponse 메뉴_그룹_등록(final MenuGroupCreateRequest request) {
        return menuGroupService.create(request);
    }

    protected MenuResponse 메뉴_등록(final MenuCreateRequest request) {
        return menuService.create(request);
    }

    protected TableResponse 테이블_등록(final TableCreateRequest request) {
        return tableService.create(request);
    }

    protected TableGroup 단체_지정(final TableGroupCreatRequest request) {
        return tableGroupService.create(request);
    }

    protected OrderResponse 주문_등록(final OrderCreateRequest request) {
        return orderService.create(request);
    }

    protected List<MenuProductDto> createMenuProductDtos() {
        final ProductResponse product = 상품_등록(getProductCreateRequest("마이쮸", BigDecimal.valueOf(800)));
        return List.of(new MenuProductDto(product.getId(), 1));
    }
}

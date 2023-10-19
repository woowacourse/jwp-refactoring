package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.createMenuProductDto;
import static kitchenpos.fixture.MenuFixture.한마리메뉴_DTO;
import static kitchenpos.fixture.MenuFixture.후라이드치킨_DTO;
import static kitchenpos.fixture.OrderFixture.createOrderLineItem;
import static kitchenpos.fixture.ProductFixture.후라이드_DTO;
import static kitchenpos.fixture.TableFixture.비어있는_주문_테이블_DTO;
import static kitchenpos.fixture.TableFixture.비어있지_않는_주문_테이블_DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import kitchenpos.application.dto.MenuDto;
import kitchenpos.application.dto.MenuGroupDto;
import kitchenpos.application.dto.MenuProductDto;
import kitchenpos.application.dto.OrderDto;
import kitchenpos.application.dto.OrderLineItemDto;
import kitchenpos.application.dto.OrderTableDto;
import kitchenpos.application.dto.ProductDto;
import kitchenpos.application.dto.TableGroupDto;
import kitchenpos.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/test-data.sql")
public abstract class ServiceIntegrationTest {

    @Autowired
    protected MenuService menuService;
    @Autowired
    protected TableGroupService tableGroupService;
    @Autowired
    protected ProductService productService;
    @Autowired
    protected TableService tableService;
    @Autowired
    protected MenuGroupService menuGroupService;
    @Autowired
    protected OrderService orderService;

    protected MenuDto createMenu() {
        final ProductDto savedProduct = productService.create(후라이드_DTO());
        final MenuProductDto menuProductDto = createMenuProductDto(savedProduct, 1L);
        final MenuGroupDto savedMenuGroupDto = menuGroupService.create(한마리메뉴_DTO());
        final MenuDto menuDto = 후라이드치킨_DTO(
            savedMenuGroupDto, List.of(menuProductDto), BigDecimal.valueOf(16000)
        );

        return menuService.create(menuDto);
    }

    protected OrderDto createOrderSuccessfully() {
        final MenuDto menuDto = createMenu();
        final OrderLineItemDto orderLineItemDto = createOrderLineItem(menuDto.getId(), 1L);
        final OrderTableDto savedOrderTableDto = createNotEmptyOrderTable();

        final OrderDto orderDto = new OrderDto(
            null,
            savedOrderTableDto.getId(),
            null,
            LocalDateTime.now(),
            List.of(orderLineItemDto)
        );
        return orderService.create(orderDto);
    }

    protected OrderTableDto createNotEmptyOrderTable() {
        final OrderTableDto orderTable = 비어있지_않는_주문_테이블_DTO();
        return tableService.create(orderTable);
    }

    protected void saveTableGroup(final OrderTableDto savedOrderTableDto) {
        final OrderTableDto orderTable = tableService.create(비어있는_주문_테이블_DTO());
        final TableGroupDto tableGroupDto = new TableGroupDto(
            null,
            LocalDateTime.now(),
            List.of(savedOrderTableDto, orderTable)
        );
        tableGroupService.create(tableGroupDto);
    }
}

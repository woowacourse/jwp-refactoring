package kitchenpos.support;

import static kitchenpos.support.fixture.MenuFixture.createMenuProductDto;
import static kitchenpos.support.fixture.MenuFixture.한마리메뉴_DTO;
import static kitchenpos.support.fixture.MenuFixture.후라이드치킨_DTO;
import static kitchenpos.support.fixture.OrderFixture.createOrderLineItem;
import static kitchenpos.support.fixture.ProductFixture.후라이드_DTO;
import static kitchenpos.support.fixture.TableFixture.비어있지_않는_주문_테이블_DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.application.dto.MenuDto;
import kitchenpos.menu.application.dto.MenuProductDto;
import kitchenpos.menu_group.application.MenuGroupDto;
import kitchenpos.menu_group.application.MenuGroupService;
import kitchenpos.order.application.OrderRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.dto.OrderDto;
import kitchenpos.order.application.dto.OrderLineItemDto;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.application.dto.ProductDto;
import kitchenpos.table.application.TableService;
import kitchenpos.table.application.dto.OrderTableDto;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table_group.application.TableGroupService;
import kitchenpos.table_group.application.dto.OrderTableDtoInTableGroup;
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

    @Autowired
    private OrderRepository orderRepository;

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

    protected static OrderTableDtoInTableGroup map(final OrderTableDto orderTableDto) {
        return new OrderTableDtoInTableGroup(
            orderTableDto.getId(),
            orderTableDto.getTableGroupId(),
            orderTableDto.getNumberOfGuests(),
            orderTableDto.getEmpty()
        );
    }

    protected void saveOrderMeal(final OrderTable savedOrderTable) {
        final Order order = new Order(
            savedOrderTable.getId(),
            OrderStatus.MEAL,
            LocalDateTime.now(),
            List.of()
        );
        orderRepository.save(order);
    }
}

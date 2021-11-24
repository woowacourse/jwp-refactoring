package kitchenpos.integration.table;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sun.tools.javac.util.List;
import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.application.dto.request.MenuGroupRequestDto;
import kitchenpos.application.dto.request.MenuProductRequestDto;
import kitchenpos.application.dto.request.MenuRequestDto;
import kitchenpos.application.dto.request.OrderLineItemRequestDto;
import kitchenpos.application.dto.request.OrdersRequestDto;
import kitchenpos.application.dto.request.OrdersStatusRequestDto;
import kitchenpos.application.dto.request.ProductRequestDto;
import kitchenpos.application.dto.request.TableEmptyRequestDto;
import kitchenpos.application.dto.request.TableGroupIdRequestDto;
import kitchenpos.application.dto.request.TableGroupRequestDto;
import kitchenpos.application.dto.request.TableIdRequestDto;
import kitchenpos.application.dto.request.TableRequestDto;
import kitchenpos.application.dto.response.MenuGroupResponseDto;
import kitchenpos.application.dto.response.MenuResponseDto;
import kitchenpos.application.dto.response.OrdersResponseDto;
import kitchenpos.application.dto.response.OrdersStatusResponseDto;
import kitchenpos.application.dto.response.ProductResponseDto;
import kitchenpos.application.dto.response.TableGroupResponseDto;
import kitchenpos.application.dto.response.TableResponseDto;
import kitchenpos.integration.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupServiceIntegrationTest extends IntegrationTest {

    private static final Long QUANTITY = 2L;

    private MenuRequestDto menuRequestDto;

    @BeforeEach
    void setUp() {
        ProductResponseDto productResponseDto = productService
            .create(new ProductRequestDto("얌 프라이", BigDecimal.valueOf(8000)));
        MenuGroupResponseDto menuGroupResponseDto = menuGroupService
            .create(new MenuGroupRequestDto("시즌 메뉴"));

        MenuProductRequestDto menuProduct = new MenuProductRequestDto(
            productResponseDto.getId(),
            QUANTITY
        );
        menuRequestDto = new MenuRequestDto(
            "얌 프라이",
            BigDecimal.valueOf(8000),
            menuGroupResponseDto.getId(),
            Collections.singletonList(menuProduct)
        );
    }

    @DisplayName("단체를 등록할 수 있다.")
    @Test
    void create_Valid_Success() {
        // given
        TableRequestDto tableRequestDto1 = new TableRequestDto(0L, TRUE);
        TableRequestDto tableRequestDto2 = new TableRequestDto(0L, TRUE);

        TableResponseDto tableResponseDto1 = tableService.create(tableRequestDto1);
        TableResponseDto tableResponseDto2 = tableService.create(tableRequestDto2);

        TableIdRequestDto tableIdRequestDto1 = new TableIdRequestDto(tableResponseDto1.getId());
        TableIdRequestDto tableIdRequestDto2 = new TableIdRequestDto(tableResponseDto2.getId());

        TableGroupRequestDto requestDto = new TableGroupRequestDto(
            List.of(tableIdRequestDto1, tableIdRequestDto2)
        );

        // when
        TableGroupResponseDto responseDto = tableGroupService.create(requestDto);

        // then
        assertThat(responseDto.getId()).isNotNull();
    }

    @DisplayName("단체 지정 시 주문 테이블이 없으면 등록할 수 없다.")
    @Test
    void create_EmptyOrderTable_Fail() {
        // given
        TableGroupRequestDto requestDto = new TableGroupRequestDto(Collections.emptyList());

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(requestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시 주문 테이블이 1개면 등록할 수 없다.")
    @Test
    void create_OneOrderTable_Fail() {
        // given
        TableRequestDto tableRequestDto = new TableRequestDto(0L, TRUE);
        TableResponseDto tableResponseDto = tableService.create(tableRequestDto);

        TableIdRequestDto tableIdRequestDto = new TableIdRequestDto(tableResponseDto.getId());
        TableGroupRequestDto requestDto = new TableGroupRequestDto(List.of(tableIdRequestDto));

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(requestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시 주문 테이블이 하나라도 존재하지 않으면 등록할 수 없다.")
    @Test
    void create_NonExistingOrderTable_Fail() {
        // given
        TableRequestDto tableRequestDto1 = new TableRequestDto(0L, TRUE);
        TableRequestDto tableRequestDto2 = new TableRequestDto(0L, TRUE);

        TableResponseDto tableResponseDto1 = tableService.create(tableRequestDto1);
        TableResponseDto tableResponseDto2 = tableService.create(tableRequestDto2);

        TableIdRequestDto tableIdRequestDto1 = new TableIdRequestDto(tableResponseDto1.getId());
        TableIdRequestDto tableIdRequestDto2 = new TableIdRequestDto(tableResponseDto2.getId());
        TableIdRequestDto tableIdRequestDto3 = new TableIdRequestDto(Long.MAX_VALUE);

        TableGroupRequestDto requestDto = new TableGroupRequestDto(
            List.of(tableIdRequestDto1, tableIdRequestDto2, tableIdRequestDto3)
        );

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(requestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시 주문 테이블이 비어있지 않으면 등록할 수 없다.")
    @Test
    void create_NonEmptyOrderTable_Fail() {
        // given
        TableRequestDto tableRequestDto1 = new TableRequestDto(0L, TRUE);
        TableRequestDto tableRequestDto2 = new TableRequestDto(0L, FALSE);

        TableResponseDto tableResponseDto1 = tableService.create(tableRequestDto1);
        TableResponseDto tableResponseDto2 = tableService.create(tableRequestDto2);

        TableIdRequestDto tableIdRequestDto1 = new TableIdRequestDto(tableResponseDto1.getId());
        TableIdRequestDto tableIdRequestDto2 = new TableIdRequestDto(tableResponseDto2.getId());

        TableGroupRequestDto requestDto = new TableGroupRequestDto(
            List.of(tableIdRequestDto1, tableIdRequestDto2)
        );

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(requestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시 이미 그룹화된 테이블이 하나라도 속해 있으면 등록할 수 없다.")
    @Test
    void create_AlreadyGroupOrderTable_Fail() {
        // given
        TableRequestDto tableRequestDto1 = new TableRequestDto(0L, TRUE);
        TableRequestDto tableRequestDto2 = new TableRequestDto(0L, TRUE);
        TableRequestDto tableRequestDto3 = new TableRequestDto(0L, TRUE);

        TableResponseDto tableResponseDto1 = tableService.create(tableRequestDto1);
        TableResponseDto tableResponseDto2 = tableService.create(tableRequestDto2);
        TableResponseDto tableResponseDto3 = tableService.create(tableRequestDto3);

        TableIdRequestDto tableIdRequestDto1 = new TableIdRequestDto(tableResponseDto1.getId());
        TableIdRequestDto tableIdRequestDto2 = new TableIdRequestDto(tableResponseDto2.getId());
        TableIdRequestDto tableIdRequestDto3 = new TableIdRequestDto(tableResponseDto3.getId());

        TableGroupRequestDto tableGroupRequestDto = new TableGroupRequestDto(
            List.of(tableIdRequestDto1, tableIdRequestDto2)
        );
        tableGroupService.create(tableGroupRequestDto);

        TableGroupRequestDto requestDto = new TableGroupRequestDto(
            List.of(tableIdRequestDto1, tableIdRequestDto3)
        );

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(requestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체를 삭제할 수 있다.")
    @Test
    void ungroup_Valid_Success() {
        // given
        TableRequestDto tableRequestDto1 = new TableRequestDto(0L, TRUE);
        TableRequestDto tableRequestDto2 = new TableRequestDto(0L, TRUE);

        TableResponseDto tableResponseDto1 = tableService.create(tableRequestDto1);
        TableResponseDto tableResponseDto2 = tableService.create(tableRequestDto2);

        TableIdRequestDto tableIdRequestDto1 = new TableIdRequestDto(tableResponseDto1.getId());
        TableIdRequestDto tableIdRequestDto2 = new TableIdRequestDto(tableResponseDto2.getId());

        TableGroupRequestDto tableGroupRequestDto = new TableGroupRequestDto(
            List.of(tableIdRequestDto1, tableIdRequestDto2)
        );
        TableGroupResponseDto tableGroupResponseDto = tableGroupService
            .create(tableGroupRequestDto);

        TableGroupIdRequestDto requestDto = new TableGroupIdRequestDto(
            tableGroupResponseDto.getId()
        );

        // when
        // then
        assertThatCode(() -> tableGroupService.ungroup(requestDto))
            .doesNotThrowAnyException();
    }

    @DisplayName("단체 해제 시 단체가 없으면 삭제할 수 없다.")
    @Test
    void ungroup_NonExistingTableGroup_Fail() {
        // given
        TableGroupIdRequestDto requestDto = new TableGroupIdRequestDto(Long.MAX_VALUE);

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(requestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 해제 시 주문 상태가 `조리`면 삭제할 수 없다.")
    @Test
    void ungroup_CookingOrderStatus_Fail() {
        // given
        MenuResponseDto menuResponseDto = menuService.create(menuRequestDto);

        TableRequestDto tableRequestDto1 = new TableRequestDto(2L, FALSE);
        TableRequestDto tableRequestDto2 = new TableRequestDto(2L, FALSE);

        TableResponseDto tableResponseDto1 = tableService.create(tableRequestDto1);
        TableResponseDto tableResponseDto2 = tableService.create(tableRequestDto2);

        OrderLineItemRequestDto orderLineItemRequestDto = new OrderLineItemRequestDto(
            menuResponseDto.getId(),
            QUANTITY
        );

        OrdersRequestDto ordersRequestDto1 = new OrdersRequestDto(
            tableResponseDto1.getId(),
            Collections.singletonList(orderLineItemRequestDto)
        );
        OrdersRequestDto ordersRequestDto2 = new OrdersRequestDto(
            tableResponseDto2.getId(),
            Collections.singletonList(orderLineItemRequestDto)
        );

        OrdersResponseDto ordersResponseDto1 = ordersService.create(ordersRequestDto1);
        OrdersResponseDto ordersResponseDto2 = ordersService.create(ordersRequestDto2);

        OrdersStatusRequestDto ordersStatusRequestDto1 = new OrdersStatusRequestDto(
            ordersResponseDto1.getId(),
            null
        );
        OrdersStatusRequestDto ordersStatusRequestDto2 = new OrdersStatusRequestDto(
            ordersResponseDto2.getId(),
            null
        );

        OrdersStatusResponseDto ordersStatusResponseDto1 = ordersService
            .changeOrderStatus(ordersStatusRequestDto1);
        OrdersStatusResponseDto ordersStatusResponseDto2 = ordersService
            .changeOrderStatus(ordersStatusRequestDto2);

        TableEmptyRequestDto tableEmptyRequestDto1 = new TableEmptyRequestDto(
            tableResponseDto1.getId(),
            TRUE
        );
        TableEmptyRequestDto tableEmptyRequestDto2 = new TableEmptyRequestDto(
            tableResponseDto2.getId(),
            TRUE
        );

        tableService.changeEmpty(tableEmptyRequestDto1);
        tableService.changeEmpty(tableEmptyRequestDto2);

        OrdersStatusRequestDto ordersStatusRequestDto3 = new OrdersStatusRequestDto(
            ordersStatusResponseDto1.getOrderTableId(),
            COOKING.name()
        );
        OrdersStatusRequestDto ordersStatusRequestDto4 = new OrdersStatusRequestDto(
            ordersStatusResponseDto2.getOrderTableId(),
            COOKING.name()
        );

        ordersService.changeOrderStatus(ordersStatusRequestDto3);
        ordersService.changeOrderStatus(ordersStatusRequestDto4);

        TableIdRequestDto tableIdRequestDto1 = new TableIdRequestDto(tableResponseDto1.getId());
        TableIdRequestDto tableIdRequestDto2 = new TableIdRequestDto(tableResponseDto2.getId());

        TableGroupRequestDto tableGroupRequestDto = new TableGroupRequestDto(
            List.of(tableIdRequestDto1, tableIdRequestDto2)
        );
        TableGroupResponseDto tableGroupResponseDto = tableGroupService
            .create(tableGroupRequestDto);

        TableGroupIdRequestDto requestDto = new TableGroupIdRequestDto(
            tableGroupResponseDto.getId()
        );

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(requestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 해제 시 주문 상태가 `식사`면 삭제할 수 없다.")
    @Test
    void ungroup_MealOrderStatus_Fail() {
        // given
        MenuResponseDto menuResponseDto = menuService.create(menuRequestDto);

        TableRequestDto tableRequestDto1 = new TableRequestDto(2L, FALSE);
        TableRequestDto tableRequestDto2 = new TableRequestDto(2L, FALSE);

        TableResponseDto tableResponseDto1 = tableService.create(tableRequestDto1);
        TableResponseDto tableResponseDto2 = tableService.create(tableRequestDto2);

        OrderLineItemRequestDto orderLineItemRequestDto = new OrderLineItemRequestDto(
            menuResponseDto.getId(),
            QUANTITY
        );

        OrdersRequestDto ordersRequestDto1 = new OrdersRequestDto(
            tableResponseDto1.getId(),
            Collections.singletonList(orderLineItemRequestDto)
        );
        OrdersRequestDto ordersRequestDto2 = new OrdersRequestDto(
            tableResponseDto2.getId(),
            Collections.singletonList(orderLineItemRequestDto)
        );

        OrdersResponseDto ordersResponseDto1 = ordersService.create(ordersRequestDto1);
        OrdersResponseDto ordersResponseDto2 = ordersService.create(ordersRequestDto2);

        OrdersStatusRequestDto ordersStatusRequestDto1 = new OrdersStatusRequestDto(
            ordersResponseDto1.getId(),
            null
        );
        OrdersStatusRequestDto ordersStatusRequestDto2 = new OrdersStatusRequestDto(
            ordersResponseDto2.getId(),
            null
        );

        OrdersStatusResponseDto ordersStatusResponseDto1 = ordersService
            .changeOrderStatus(ordersStatusRequestDto1);
        OrdersStatusResponseDto ordersStatusResponseDto2 = ordersService
            .changeOrderStatus(ordersStatusRequestDto2);

        TableEmptyRequestDto tableEmptyRequestDto1 = new TableEmptyRequestDto(
            tableResponseDto1.getId(),
            TRUE
        );
        TableEmptyRequestDto tableEmptyRequestDto2 = new TableEmptyRequestDto(
            tableResponseDto2.getId(),
            TRUE
        );

        tableService.changeEmpty(tableEmptyRequestDto1);
        tableService.changeEmpty(tableEmptyRequestDto2);

        OrdersStatusRequestDto ordersStatusRequestDto3 = new OrdersStatusRequestDto(
            ordersStatusResponseDto1.getOrderTableId(),
            MEAL.name()
        );
        OrdersStatusRequestDto ordersStatusRequestDto4 = new OrdersStatusRequestDto(
            ordersStatusResponseDto2.getOrderTableId(),
            MEAL.name()
        );

        ordersService.changeOrderStatus(ordersStatusRequestDto3);
        ordersService.changeOrderStatus(ordersStatusRequestDto4);

        TableIdRequestDto tableIdRequestDto1 = new TableIdRequestDto(tableResponseDto1.getId());
        TableIdRequestDto tableIdRequestDto2 = new TableIdRequestDto(tableResponseDto2.getId());

        TableGroupRequestDto tableGroupRequestDto = new TableGroupRequestDto(
            List.of(tableIdRequestDto1, tableIdRequestDto2)
        );
        TableGroupResponseDto tableGroupResponseDto = tableGroupService
            .create(tableGroupRequestDto);

        TableGroupIdRequestDto requestDto = new TableGroupIdRequestDto(
            tableGroupResponseDto.getId()
        );

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(requestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }
}

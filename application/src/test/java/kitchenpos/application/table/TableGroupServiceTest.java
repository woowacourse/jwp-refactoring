package kitchenpos.application.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.menu.MenuService;
import kitchenpos.application.order.OrderService;
import kitchenpos.application.menu.dto.request.CreateMenuDto;
import kitchenpos.application.menu.dto.request.CreateMenuProductDto;
import kitchenpos.application.order.dto.request.CreateOrderDto;
import kitchenpos.application.order.dto.request.CreateOrderLineItemDto;
import kitchenpos.application.table.dto.request.CreateTableDto;
import kitchenpos.application.table.dto.request.CreateTableGroupDto;
import kitchenpos.application.table.dto.response.TableGroupDto;
import kitchenpos.core.repository.table.OrderTableRepository;
import kitchenpos.core.domain.table.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class TableGroupServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @DisplayName("create 메서드는")
    @Nested
    class CreateTest {

        @Test
        void 복수의_빈_테이블을_단체로_묶고_주문_테이블로_수정하여_반환한다() {
            Long emptyTableId1 = generateEmptyTable();
            Long emptyTableId2 = generateEmptyTable();
            CreateTableGroupDto createTableGroupDto = new CreateTableGroupDto(List.of(emptyTableId1, emptyTableId2));

            TableGroupDto actual = tableGroupService.create(createTableGroupDto);
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getCreatedDate()).isNotNull(),
                    () -> assertThat(actual.getOrderTables().get(0).getTableGroupId()).isEqualTo(actual.getId()),
                    () -> assertThat(actual.getOrderTables().get(0).getEmpty()).isFalse(),
                    () -> assertThat(actual.getOrderTables().get(1).getTableGroupId()).isEqualTo(actual.getId()),
                    () -> assertThat(actual.getOrderTables().get(1).getEmpty()).isFalse()
            );
        }

        @Test
        void 테이블_정보가_누락된_경우_예외가_발생한다() {
            assertThatThrownBy(() -> tableGroupService.create(new CreateTableGroupDto(null)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_테이블이_포함된_경우_예외가_발생한다() {
            Long existingTableId = generateEmptyTable();
            Long nonExistingTableId = 999999999L;
            CreateTableGroupDto createTableGroupDto = new CreateTableGroupDto(
                    List.of(existingTableId, nonExistingTableId));

            assertThatThrownBy(() -> tableGroupService.create(createTableGroupDto))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("ungroup 메서드는")
    @Nested
    class UngroupTest {

        Long orderTableId1;
        Long orderTableId2;

        @BeforeEach
        void setup() {
            orderTableId1 = generateEmptyTable();
            orderTableId2 = generateEmptyTable();
        }

        @Test
        void 단체로_묶인_테이블을_분리시킨다() {
            CreateTableGroupDto createTableGroupDto = new CreateTableGroupDto(List.of(orderTableId1, orderTableId2));
            TableGroupDto savedTableGroup = tableGroupService.create(createTableGroupDto);

            tableGroupService.ungroup(savedTableGroup.getId());

            List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(List.of(orderTableId1, orderTableId2));
            assertAll(
                    () -> assertThat(orderTables.get(0).getTableGroupId()).isNull(),
                    () -> assertThat(orderTables.get(0).isEmpty()).isFalse(),
                    () -> assertThat(orderTables.get(1).getTableGroupId()).isNull(),
                    () -> assertThat(orderTables.get(1).isEmpty()).isFalse()
            );
        }

        @Test
        void 주문이_들어간_테이블이_포함된_경우_예외를_발생시킨다() {
            Long savedMenuId = saveMenu();
            CreateTableGroupDto createTableGroupDto = new CreateTableGroupDto(List.of(orderTableId1, orderTableId2));
            Long savedTableGroupId = tableGroupService.create(createTableGroupDto).getId();
            List<CreateOrderLineItemDto> orderLineItems = List.of(new CreateOrderLineItemDto(savedMenuId, 1));
            orderService.create(new CreateOrderDto(orderTableId1, orderLineItems));

            assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroupId))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    private Long generateEmptyTable() {
        return tableService.create(new CreateTableDto(0, true)).getId();
    }

    private Long saveMenu() {
        CreateMenuDto createMenuDto = new CreateMenuDto("후라이드+후라이드", BigDecimal.valueOf(19000), 1L,
                List.of(new CreateMenuProductDto(1L, 2)));
        return menuService.create(createMenuDto).getId();
    }
}

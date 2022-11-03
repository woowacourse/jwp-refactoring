package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.request.CreateMenuDto;
import kitchenpos.application.dto.request.CreateMenuProductDto;
import kitchenpos.application.dto.request.CreateOrderDto;
import kitchenpos.application.dto.request.CreateOrderLineItemDto;
import kitchenpos.application.dto.request.CreateTableDto;
import kitchenpos.application.dto.request.EmptyTableDto;
import kitchenpos.application.dto.response.TableDto;
import kitchenpos.application.dto.request.UpdateGuestNumberDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class TableServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderService orderService;

    @DisplayName("create 메서드는")
    @Nested
    class CreateTest {

        @Test
        void 생성한_테이블을_반환한다() {
            CreateTableDto createTableDto = new CreateTableDto(1, false);
            TableDto actual = tableService.create(createTableDto);
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getNumberOfGuests()).isEqualTo(1),
                    () -> assertThat(actual.getTableGroupId()).isNull(),
                    () -> assertThat(actual.getEmpty()).isFalse()
            );
        }

        @Test
        void 빈_테이블_여부가_누락된_경우_주문_테이블로_간주한다() {
            CreateTableDto createTableDto = new CreateTableDto(1, null);
            TableDto actual = tableService.create(createTableDto);
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getNumberOfGuests()).isEqualTo(1),
                    () -> assertThat(actual.getEmpty()).isFalse()
            );
        }

        @Test
        void 고객_인원_정보가_누락된_경우_0명으로_간주한다() {
            CreateTableDto createTableDto = new CreateTableDto(null, true);
            TableDto actual = tableService.create(createTableDto);
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getNumberOfGuests()).isZero(),
                    () -> assertThat(actual.getEmpty()).isTrue()
            );
        }
    }

    @Test
    void list_메서드는_모든_테이블_목록을_조회한다() {
        List<TableDto> tables = tableService.list();

        assertThat(tables).hasSizeGreaterThan(1);
    }

    @DisplayName("changeEmpty 메서드는")
    @Nested
    class ChangeEmptyTest {

        @Test
        void 존재하지_않는_테이블의_빈_테이블_여부를_수정하려는_경우_예외를_발생시킨다() {
            EmptyTableDto emptyTableDto = new EmptyTableDto(999999L, false);

            assertThatThrownBy(() -> tableService.changeEmpty(emptyTableDto))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문이_들어간_테이블의_빈_테이블_여부를_수정하려는_경우_예외를_발생시킨다() {
            Long savedMenuId = saveMenu();
            Long orderTableId = saveOrderTable();
            List<CreateOrderLineItemDto> orderLineItems = List.of(new CreateOrderLineItemDto(savedMenuId, 1));
            orderService.create(new CreateOrderDto(orderTableId, orderLineItems));

            assertThatThrownBy(() -> tableService.changeEmpty(new EmptyTableDto(orderTableId, true)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("changeNumberOfGuests 메서드는")
    @Nested
    class ChangeNumberOfGuestsTest {

        @Test
        void 주문_테이블의_고객_수를_수정하고_수정된_테이블_정보를_반환한다() {
            Long savedOrderTableId = saveOrderTable();
            UpdateGuestNumberDto updateGuestNumberDto = new UpdateGuestNumberDto(savedOrderTableId, 5);
            TableDto actual = tableService.changeNumberOfGuests(updateGuestNumberDto);
            assertAll(
                    () -> assertThat(actual.getId()).isEqualTo(savedOrderTableId),
                    () -> assertThat(actual.getNumberOfGuests()).isEqualTo(5)
            );
        }

        @Test
        void 존재하지_않는_테이블의_고객_수를_수정하려는_경우_예외를_발생시킨다() {
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(new UpdateGuestNumberDto(999999L, 10)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    private Long saveMenu() {
        CreateMenuDto createMenuDto = new CreateMenuDto("후라이드+후라이드", BigDecimal.valueOf(19000), 1L,
                List.of(new CreateMenuProductDto(1L, 2)));
        return menuService.create(createMenuDto).getId();
    }

    private Long saveOrderTable() {
        return tableService.create(new CreateTableDto(1, false)).getId();
    }
}

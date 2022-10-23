package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.application.dto.CreateOrderDto;
import kitchenpos.application.dto.CreateOrderLineItemDto;
import kitchenpos.application.dto.CreateTableDto;
import kitchenpos.application.dto.CreateTableGroupDto;
import kitchenpos.application.dto.EmptyTableDto;
import kitchenpos.application.dto.TableDto;
import kitchenpos.application.dto.UpdateGuestNumberDto;
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
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

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
        void 단체로_지정된_테이블의_빈_테이블_여부를_수정하려는_경우_예외를_발생시킨다() {
            Long orderTableId1 = saveEmptyTable();
            Long orderTableId2 = saveEmptyTable();
            tableGroupService.create(new CreateTableGroupDto(List.of(orderTableId1, orderTableId2)));

            assertThatThrownBy(() -> tableService.changeEmpty(new EmptyTableDto(orderTableId1, false)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문이_들어간_테이블의_빈_테이블_여부를_수정하려는_경우_예외를_발생시킨다() {
            Long orderTableId = saveOrderTable();
            List<CreateOrderLineItemDto> orderLineItems = List.of(new CreateOrderLineItemDto(1L, 1));
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
        void 테이블의_고객_수를_음수로_수정하려는_경우_예외를_발생시킨다() {
            Long savedOrderTableId = saveOrderTable();
            UpdateGuestNumberDto updateGuestNumberDto = new UpdateGuestNumberDto(savedOrderTableId, -1);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(updateGuestNumberDto))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_테이블의_고객_수를_수정하려는_경우_예외를_발생시킨다() {
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(new UpdateGuestNumberDto(999999L, 10)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 빈_테이블의_고객_수를_수정하려는_경우_예외를_발생시킨다() {
            Long savedEmptyTableId = saveEmptyTable();
            UpdateGuestNumberDto updateGuestNumberDto = new UpdateGuestNumberDto(savedEmptyTableId, 10);
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(updateGuestNumberDto))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    private Long saveEmptyTable() {
        return tableService.create(new CreateTableDto(0, true)).getId();
    }

    private Long saveOrderTable() {
        return tableService.create(new CreateTableDto(1, false)).getId();
    }
}

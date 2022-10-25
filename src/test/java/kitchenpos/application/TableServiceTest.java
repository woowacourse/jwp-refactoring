package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @DisplayName("테이블 생성 테스트")
    @Nested
    class CreateTest {

        @Test
        void 테이블을_생성하고_결과를_반환한다() {
            // given
            final var table = new OrderTable();
            table.setNumberOfGuests(2);
            table.setEmpty(true);

            // when
            final var createdTable = tableService.create(table);

            // then
            assertAll(
                    () -> assertThat(createdTable.getId()).isNotNull(),
                    () -> assertThat(createdTable.getTableGroupId()).isNull(),
                    () -> assertThat(createdTable.getNumberOfGuests()).isEqualTo(table.getNumberOfGuests())
            );
        }
    }

    @Test
    void 테이블_목록을_조회한다() {
        // given
        테이블_생성_및_저장();
        테이블_생성_및_저장();

        // when
        List<OrderTable> foundTables = tableService.list();

        // then
        assertThat(foundTables).hasSizeGreaterThanOrEqualTo(2);
    }

    @DisplayName("테이블 비어있는지 여부 변경 테스트")
    @Nested
    class ChangeEmptyTest {

        @Test
        void 비어있는지_여부를_변경한다() {
            // given
            final var table = 테이블_생성_및_저장();
            final var newTable = new OrderTable();
            newTable.setEmpty(true);

            // when
            final var changedTable = tableService.changeEmpty(table.getId(), newTable);

            // then
            assertAll(
                    () -> assertThat(changedTable.getId()).isEqualTo(table.getId()),
                    () -> assertThat(changedTable.isEmpty()).isNotEqualTo(table.isEmpty())
            );
        }

        @Test
        void 없는_테이블인_경우_예외를_던진다() {
            // given
            final var table = new OrderTable();
            final var newTable = new OrderTable();
            newTable.setEmpty(false);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(table.getId(), newTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 단체_지정이_되어있는_테이블인_경우_예외를_던진다() {
            // given
            final var table = 테이블_생성_및_저장();
            단체_지정(table);
            final var newTable = new OrderTable();
            newTable.setEmpty(true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(table.getId(), newTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("테이블 방문 손님 수 변경")
    @Nested
    class ChangeNumberOfGuests {

        @Test
        void 방문_손님_수를_변경한다() {
            // given
            final var table = 테이블_생성_및_저장();
            final var newTable = new OrderTable();
            newTable.setNumberOfGuests(5);

            // when
            final var changedTable = tableService.changeNumberOfGuests(table.getId(), newTable);

            // then
            assertAll(
                    () -> assertThat(changedTable.getId()).isEqualTo(table.getId()),
                    () -> assertThat(changedTable.getNumberOfGuests()).isEqualTo(newTable.getNumberOfGuests())
            );
        }

        @Test
        void 방문_손님_수가_0보다_작으면_예외를_던진다() {
            // given
            final var table = 테이블_생성_및_저장();
            final var newTable = new OrderTable();
            newTable.setNumberOfGuests(-1);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(table.getId(), newTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 없는_테이블인_경우_예외를_던진다() {
            // given
            final var table = new OrderTable();
            final var newTable = new OrderTable();
            newTable.setNumberOfGuests(5);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(table.getId(), newTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 비어있는_테이블인_경우_예외를_던진다() {
            // given
            final var table = 빈_테이블_생성_및_저장();
            final var newTable = new OrderTable();
            newTable.setNumberOfGuests(5);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(table.getId(), newTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}

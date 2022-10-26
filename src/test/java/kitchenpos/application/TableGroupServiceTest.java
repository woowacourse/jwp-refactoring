package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    private OrderTable tableA;
    private OrderTable tableB;

    @BeforeEach
    void setUp() {
        tableA = 빈_테이블_생성_및_저장();
        tableB = 빈_테이블_생성_및_저장();
    }

    @DisplayName("단체 지정 생성 테스트")
    @Nested
    class CreateTest {

        @Test
        void 단체_지정을_생성한다() {
            // given
            final var tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(tableA, tableB));

            // when
            final var createdGroup = tableGroupService.create(tableGroup);

            // then
            assertAll(
                    () -> assertThat(createdGroup.getId()).isNotNull(),
                    () -> assertThat(createdGroup.getCreatedDate()).isBefore(LocalDateTime.now()),
                    () -> assertThat(createdGroup.getOrderTables()).hasSize(2)
            );
        }

        @Test
        void 단체_지정하는_테이블의_수가_2보다_작으면_예외를_던진다() {
            // given
            final var tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(tableA));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 단체_지정하는_테이블이_비어있지_않은_경우_예외를_던진다() {
            // given
            final var tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(tableA, 테이블_생성_및_저장()));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 단체_지정하는_테이블이_이미_단체_지정되어_있는_경우_예외를_던진다() {
            // given
            단체_지정(tableA);

            final var newTableGroup = new TableGroup();
            newTableGroup.setOrderTables(List.of(tableA, tableB));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(newTableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 단체_지정을_삭제한다() {
        // given
        final var tableGroupId = 단체_지정().getId();

        // when & then
        assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroupId));
    }
}

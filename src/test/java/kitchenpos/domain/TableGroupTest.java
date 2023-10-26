package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class TableGroupTest {

    @Nested
    class appendOrderTables_성공_테스트 {

        @Test
        void 주문_테이블을_추가할_수_있다() {
            // given
            final var tableGroup = new TableGroup();
            final var orderTable1 = new OrderTable(null, 5, false);
            final var orderTable2 = new OrderTable(null, 3, false);
            final var orderTables = List.of(orderTable1, orderTable2);

            // when
            tableGroup.appendOrderTables(orderTables);

            // then
            assertSoftly(soft -> {
                soft.assertThat(tableGroup.getOrderTables().get(0)).isEqualTo(orderTable1);
                soft.assertThat(tableGroup.getOrderTables().get(1)).isEqualTo(orderTable2);
            });
        }
    }

    @Nested
    class appendOrderTables_실패_테스트 {

        @Test
        void 주문하는_테이블이_없으면_에러를_반환한다() {
            // given
            final var tableGroup = new TableGroup();

            // when & then
            assertThatThrownBy(() -> tableGroup.appendOrderTables(List.of()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 주문하는 테이블이 없거나, 단체 주문 대상이 아닙니다.");
        }

        @Test
        void 단체_주문_테이블이_아니라면_에러를_반환한다() {
            // given
            final var tableGroup = new TableGroup();
            final var orderTable = new OrderTable(null, 5, true);

            // when & then
            assertThatThrownBy(() -> tableGroup.appendOrderTables(List.of(orderTable)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 주문하는 테이블이 없거나, 단체 주문 대상이 아닙니다.");
        }

        @Test
        void 빈_테이블을_추가하는게_아니라면_에러를_반환한다() {
            // given
            final var tableGroup = new TableGroup();
            final var orderTable1 = new OrderTable(null, 5, true);
            final var orderTable2 = new OrderTable(null, 3, true);
            final var orderTables = List.of(orderTable1, orderTable2);

            // when & then
            assertThatThrownBy(() -> tableGroup.appendOrderTables(orderTables))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 빈 테이블이 생성되지 않았습니다.");
        }

        @Test
        void 이미_다른_테이블_그룹에_속한_테이블을_추가하면_에러를_반환한다() {
            // given
            final var tableGroup = new TableGroup();
            final var anotherTableGroup = new TableGroup();
            final var orderTable1 = new OrderTable(anotherTableGroup, 5, false);
            final var orderTable2 = new OrderTable(anotherTableGroup, 3, false);
            final var orderTables = List.of(orderTable1, orderTable2);

            // when & then
            assertThatThrownBy(() -> tableGroup.appendOrderTables(orderTables))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 이미 다른 테이블 그룹에 속하는 테이블입니다.");
        }
    }
}

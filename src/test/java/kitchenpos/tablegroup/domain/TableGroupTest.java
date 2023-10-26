package kitchenpos.tablegroup.domain;

import static kitchenpos.order.domain.OrderFixture.계산_완료_상태_주문;
import static kitchenpos.order.domain.OrderFixture.조리_상태_주문;
import static kitchenpos.order.domain.OrderTableFixture.단체_지정_없는_빈_주문_테이블;
import static kitchenpos.order.domain.OrderTableFixture.단체_지정_없는_채워진_주문_테이블;
import static kitchenpos.order.domain.OrderTableFixture.단체_지정_주문_테이블;
import static kitchenpos.tablegroup.domain.TableGroupFixture.단체_지정;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.tablegroup.vo.GroupTableIds;
import kitchenpos.tablegroup.vo.GroupTables;
import kitchenpos.tablegroup.vo.TableOrders;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TableGroupTest {

    @Nested
    class 단체_지정을_할_때 {

        @Test
        void 주문_테이블_ID_목록과_저장된_주문_테이블_목록의_사이즈가_다를_경우_예외를_던진다() {
            // given
            TableGroup tableGroup = 단체_지정();
            List<Long> invalidOrderTableIds = List.of(Long.MIN_VALUE, Long.MAX_VALUE);

            GroupTableIds groupTableIds = new GroupTableIds(invalidOrderTableIds);
            List<OrderTable> savedOrderTables = List.of(단체_지정_없는_빈_주문_테이블());

            // expect
            assertThatThrownBy(() -> tableGroup.group(savedOrderTables, groupTableIds))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 주문 테이블이 있거나 주문 테이블이 중복되었습니다.");
        }

        @Test
        void 주문_테이블_목록이_비었으면_예외를_던진다() {
            // given
            TableGroup tableGroup = 단체_지정();
            List<Long> orderTableIds = List.of();

            GroupTableIds groupTableIds = new GroupTableIds(orderTableIds);
            List<OrderTable> invalidOrderTables = List.of();

            // expect
            assertThatThrownBy(() -> tableGroup.group(invalidOrderTables, groupTableIds))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("단체 지정의 주문 테이블 개수는 최소 2 이상이어야 합니다.");
        }

        @Test
        void 주문_테이블의_개수가_2개_미만이면_예외를_던진다() {
            // given
            TableGroup tableGroup = 단체_지정();
            List<Long> orderTableIds = List.of(1L);

            GroupTableIds groupTableIds = new GroupTableIds(orderTableIds);
            List<OrderTable> invalidOrderTables = List.of(단체_지정_없는_빈_주문_테이블());

            // expect
            assertThatThrownBy(() -> tableGroup.group(invalidOrderTables, groupTableIds))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("단체 지정의 주문 테이블 개수는 최소 2 이상이어야 합니다.");
        }

        @Test
        void 주문_테이블_중_채워진_테이블이_있는_경우_예외를_던진다() {
            // given
            TableGroup tableGroup = 단체_지정();
            List<Long> orderTableIds = List.of(1L, 2L);

            GroupTableIds groupTableIds = new GroupTableIds(orderTableIds);
            List<OrderTable> invalidOrderTables = List.of(단체_지정_없는_빈_주문_테이블(), 단체_지정_없는_채워진_주문_테이블());

            // expect
            assertThatThrownBy(() -> tableGroup.group(invalidOrderTables, groupTableIds))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("단체 지정의 주문 테이블이 차 있거나 이미 단체 지정되었습니다.");
        }

        @Test
        void 주문_테이블_중_이미_단체_지정된_테이블이_있는_경우_예외를_던진다() {
            // given
            TableGroup tableGroup = 단체_지정();
            List<Long> orderTableIds = List.of(1L, 2L);

            GroupTableIds groupTableIds = new GroupTableIds(orderTableIds);
            Long tableGroupId = 1L;
            List<OrderTable> invalidOrderTables = List.of(단체_지정_없는_빈_주문_테이블(), 단체_지정_주문_테이블(tableGroupId));

            // expect
            assertThatThrownBy(() -> tableGroup.group(invalidOrderTables, groupTableIds))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("단체 지정의 주문 테이블이 차 있거나 이미 단체 지정되었습니다.");
        }
    }

    @Nested
    class 단체_지정을_해제할_때 {

        @Test
        void 조리_혹은_식사_주문이_존재하는_경우_예외를_던진다() {
            // given
            TableGroup tableGroup = 단체_지정();

            GroupTables groupTables = new GroupTables(List.of(단체_지정_주문_테이블(1L)));
            TableOrders tableOrders = new TableOrders(List.of(조리_상태_주문()));

            // expect
            assertThatThrownBy(() -> tableGroup.ungroup(groupTables, tableOrders))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("조리 혹은 식사 주문이 존재하는 단체 지정은 단체 지정을 취소할 수 없습니다.");
        }

        @Test
        void 정상적으로_해제한다() {
            // given
            TableGroup tableGroup = 단체_지정();

            GroupTables groupTables = new GroupTables(List.of(단체_지정_주문_테이블(1L)));
            TableOrders tableOrders = new TableOrders(List.of(계산_완료_상태_주문()));

            // expect
            assertThatNoException().isThrownBy(() -> tableGroup.ungroup(groupTables, tableOrders));
        }
    }
}

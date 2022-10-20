package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.SpringServiceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest {

    @Nested
    class create_메소드는 {

        @Nested
        class 주문테이블이_비어있는_경우 extends SpringServiceTest {

            private final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), new ArrayList<>());

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블 2개 이상인 경우 단체 지정이 가능합니다.");
            }
        }

        @Nested
        class 주문테이블이_1개인_경우 extends SpringServiceTest {

            private final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), createOneTableGroups());

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블 2개 이상인 경우 단체 지정이 가능합니다.");
            }

            private List<OrderTable> createOneTableGroups() {
                List<OrderTable> orderTables = new ArrayList<>();
                orderTables.add(new OrderTable(1, true));
                return orderTables;
            }
        }
    }
}

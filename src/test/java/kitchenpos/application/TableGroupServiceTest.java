package kitchenpos.application;

import fixture.OrderTableBuilder;
import fixture.TableGroupBuilder;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    OrderTableDao orderTableDao;

    @Autowired
    TableGroupService tableGroupService;

    @Test
    void 테이블_그룹을_생성한다() {
        TableGroup tableGroup = TableGroupBuilder.init().build();

        TableGroup created = tableGroupService.create(tableGroup);

        assertThat(created.getId()).isNotNull();
    }

    @Test
    void 주문_테이블이_2개_이하면_예외를_발생한다() {
        TableGroup tableGroup = TableGroupBuilder.init()
                .orderTables(List.of(OrderTableBuilder.init().id(1L).build()))
                .build();

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹이_비어_있으면_예외를_발생한다() {
        TableGroup tableGroup = TableGroupBuilder.init()
                .orderTables(List.of())
                .build();

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문테이블아이디들로_주문테이블을_조회해서_개수가_맞지_않으면_예외를_발생한다() {
        TableGroup tableGroup = TableGroupBuilder.init()
                .orderTables(
                        List.of(
                                OrderTableBuilder.init().id(1L).build(),
                                OrderTableBuilder.init().id(100L).build()
                        ))
                .build();

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문테이블중_빈_주문테이블이_있으면_예외를_발생한다() {
        TableGroup tableGroup = TableGroupBuilder.init()
                .orderTables(
                        List.of(
                                OrderTableBuilder.init().id(1L).build(),
                                OrderTableBuilder.init().id(9L).build()
                        ))
                .build();

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹의_주문테이블들을_빈_테이블로_변경한다() {
        tableGroupService.ungroup(1L);

        assertSoftly(softAssertions -> {
            List<OrderTable> allByTableGroupId = orderTableDao.findAllByTableGroupId(1L);
            for (OrderTable orderTable : allByTableGroupId) {
                assertThat(orderTable.isEmpty()).isTrue();
            }
        });
    }
}

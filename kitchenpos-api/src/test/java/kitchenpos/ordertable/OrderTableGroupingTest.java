package kitchenpos.ordertable;

import java.util.List;
import javax.persistence.EntityManager;
import kitchenpos.application.ServiceTest;
import kitchenpos.fixtures.Fixtures;
import kitchenpos.tablegroup.TableGroup;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderTableGroupingTest extends ServiceTest {

    @Autowired
    JpaOrderTableRepository jpaOrderTableRepository;

    @Autowired
    OrderTableGrouping orderTableGrouping;

    @Autowired
    Fixtures fixtures;

    @Autowired
    EntityManager entityManager;

    @Test
    void 단체_지정을_한다() {
        // given
        OrderTable orderTableA = fixtures.빈_테이블_저장();
        OrderTable orderTableB = fixtures.빈_테이블_저장();
        List<Long> orderTableIds = List.of(orderTableA.getId(), orderTableB.getId());
        TableGroup tableGroup = fixtures.단체_지정_저장();

        // when
        orderTableGrouping.group(tableGroup, orderTableIds);
        entityManager.flush();
        entityManager.clear();

        // then
        List<OrderTable> orderTables = jpaOrderTableRepository.findAllByIdIn(orderTableIds);
        Assertions.assertThat(orderTables.get(0).getTableGroupId()).isNotNull();
        Assertions.assertThat(orderTables.get(1).getTableGroupId()).isNotNull();
    }


    @Test
    void 주문_테이블이_1개_인경우_예외가_발생한다() {
        // given
        OrderTable orderTableA = fixtures.빈_테이블_저장();
        TableGroup tableGroup = fixtures.단체_지정_저장();

        // when, then
        AssertionsForClassTypes.assertThatThrownBy(
                        () -> orderTableGrouping.group(tableGroup, List.of(orderTableA.getId())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_존재하지_않는_경우_예외가_발생한다() {
        // given
        OrderTable orderTableA = fixtures.빈_테이블_저장();
        TableGroup tableGroup = fixtures.단체_지정_저장();

        // when, then
        AssertionsForClassTypes.assertThatThrownBy(
                        () -> orderTableGrouping.group(tableGroup, List.of(orderTableA.getId(), -1L)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_비어있지_않는_경우_예외가_발생한다() {
        // given
        OrderTable orderTableA = fixtures.빈_테이블_저장();
        OrderTable orderTableB = fixtures.주문_테이블_저장();
        TableGroup tableGroup = fixtures.단체_지정_저장();

        // when, then
        AssertionsForClassTypes.assertThatThrownBy(
                        () -> orderTableGrouping.group(tableGroup, List.of(orderTableA.getId(), orderTableB.getId())))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    void 이미_단체_지정이_등록이_되어있는_경우_예외가_발생한다() {
        // given
        TableGroup tableGroup = fixtures.단체_지정_저장();
        OrderTable orderTableA = fixtures.주문_테이블_저장(tableGroup, false);
        OrderTable orderTableB = fixtures.주문_테이블_저장(tableGroup, false);

        TableGroup newTableGroup = fixtures.단체_지정_저장();
        // when, then
        AssertionsForClassTypes.assertThatThrownBy(
                        () -> orderTableGrouping.group(newTableGroup, List.of(orderTableA.getId(), orderTableB.getId())))
                .isInstanceOf(IllegalArgumentException.class);
    }


}

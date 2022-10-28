package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.support.TestFixtureFactory.주문_테이블을_생성한다;
import static kitchenpos.support.TestFixtureFactory.주문을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.TransactionalTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.response.TableGroupCreateResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@TransactionalTest
class TableGroupServiceTest {

    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TableGroupService tableGroupService;

    @Test
    void 단체_지정을_할_수_있다() {
        OrderTable orderTable1 = orderTableRepository.save(주문_테이블을_생성한다(null, 1, true));
        OrderTable orderTable2 = orderTableRepository.save(주문_테이블을_생성한다(null, 2, true));

        TableGroupCreateResponse tableGroupCreateResponse =
                tableGroupService.create(List.of(orderTable1.getId(), orderTable2.getId()));

        assertAll(
                () -> assertThat(tableGroupCreateResponse.getId()).isNotNull(),
                () -> assertThat(tableGroupCreateResponse.getOrderTables())
                        .extracting("id")
                        .containsExactly(orderTable1.getId(), orderTable2.getId())
        );
    }

    @Test
    void 단체_지정하려는_테이블이_2개_미만이면_예외를_반환한다() {
        OrderTable orderTable = orderTableRepository.save(주문_테이블을_생성한다(null, 1, true));

        assertThatThrownBy(() -> tableGroupService.create(List.of(orderTable.getId()))).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    void 단체_지정하려는_테이블이_존재하지_않으면_예외를_반환한다() {
        OrderTable orderTable1 = orderTableRepository.save(주문_테이블을_생성한다(null, 1, true));

        assertThatThrownBy(() -> tableGroupService.create(List.of(orderTable1.getId(), 0L)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정하려는_테이블이_빈_테이블이_아니면_예외를_반환한다() {
        OrderTable orderTable1 = orderTableRepository.save(주문_테이블을_생성한다(null, 1, true));
        OrderTable orderTable2 = orderTableRepository.save(주문_테이블을_생성한다(null, 2, false));

        assertThatThrownBy(() -> tableGroupService.create(List.of(orderTable1.getId(), orderTable2.getId())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이미_지정된_테이블을_단체_지정할_수_없다() {
        OrderTable orderTable1 = orderTableRepository.save(주문_테이블을_생성한다(null, 1, true));
        OrderTable orderTable2 = orderTableRepository.save(주문_테이블을_생성한다(null, 2, true));
        tableGroupService.create(List.of(orderTable1.getId(), orderTable2.getId()));

        assertThatThrownBy(() -> tableGroupService.create(List.of(orderTable1.getId(), orderTable2.getId())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_단체_지정을_해제할_수_있다() {
        OrderTable orderTable1 = orderTableRepository.save(주문_테이블을_생성한다(null, 1, true));
        OrderTable orderTable2 = orderTableRepository.save(주문_테이블을_생성한다(null, 2, true));
        Long tableGroupId = tableGroupService.create(List.of(orderTable1.getId(), orderTable2.getId()))
                .getId();

        assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroupId));
    }

    @Test
    void 단체_지정하려는_테이블의_주문_목록_중_식사_중인_주문이_있을_경우_예외를_반환한다() {
        OrderTable orderTable1 = orderTableRepository.save(주문_테이블을_생성한다(null, 1, true));
        OrderTable orderTable2 = orderTableRepository.save(주문_테이블을_생성한다(null, 2, true));
        Long tableGroupId = tableGroupService.create(List.of(orderTable1.getId(), orderTable2.getId()))
                .getId();
        orderRepository.save(주문을_생성한다(orderTable1.getId(), COOKING.name(), LocalDateTime.now(), null));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId)).isInstanceOf(IllegalArgumentException.class);
    }
}

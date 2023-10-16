package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.request.OrderTableDto;
import kitchenpos.application.request.TableGroupCreateRequest;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.persistence.OrderTableRepository;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupService tableGroupService;

    @Nested
    class 테이블_그룹_생성 {

        @Test
        void 성공() {
            // given
            TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(
                getPersistOrderTableRequest(2, true),
                getPersistOrderTableRequest(3, true))
            );

            // when
            TableGroup actual = tableGroupService.create(request);

            // then
            assertAll(
                () -> assertThat(actual.getOrderTables()).allMatch(it -> !it.isEmpty()),
                () -> assertThat(actual.getOrderTables()).allMatch(it -> it.getTableGroupId() != null),
                () -> assertThat(actual.getId()).isPositive(),
                () -> assertThat(actual.getOrderTables()).hasSize(2)
            );
        }

        @Test
        void 주문_그룹이_존재하지_않으면_예외() {
            // given
            TableGroupCreateRequest request = new TableGroupCreateRequest(List.of());

            // when && then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_그룹이_1개면_예외() {
            // given
            TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(
                getPersistOrderTableRequest(3, true))
            );

            // when && then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 저장된_주문_그룹과_주어진_주문_그룹_갯수가_다르면_예외() {
            // given
            TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(
                getPersistOrderTableRequest(2, true),
                new OrderTableDto(2L)
            ));

            // when && then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문테이블이_비어있지않으면_예외() {
            // given
            TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(
                getPersistOrderTableRequest(2, true),
                getPersistOrderTableRequest(2, false)
            ));

            // when && then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 테이블_그룹_해제 {

        @Test
        void 성공() {
            // given
            OrderTable orderTableA = orderTableRepository.save(new OrderTable(2, true));
            OrderTable orderTableB = orderTableRepository.save(new OrderTable(3, true));

            orderDao.save(
                new Order(orderTableA.getId(), OrderStatus.COMPLETION, LocalDateTime.now(), Collections.emptyList()));
            orderDao.save(
                new Order(orderTableB.getId(), OrderStatus.COMPLETION, LocalDateTime.now(), Collections.emptyList()));
            TableGroup tableGroup = tableGroupService.create(new TableGroupCreateRequest(
                List.of(new OrderTableDto(orderTableA.getId()),new OrderTableDto(orderTableB.getId()))));

            // when
            assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroup.getId()));
        }

        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        void 해당하는_테이블의_주문이_요리중이거나_식사중이면_예외(OrderStatus orderStatus) {
            // given
            OrderTable orderTableA = orderTableRepository.save(new OrderTable(2, true));
            OrderTable orderTableB = orderTableRepository.save(new OrderTable(3, true));
            orderDao.save(
                new Order(orderTableA.getId(), orderStatus, LocalDateTime.now(), Collections.emptyList()));
            orderDao.save(
                new Order(orderTableB.getId(), orderStatus, LocalDateTime.now(), Collections.emptyList()));
            TableGroup tableGroup = tableGroupService.create(new TableGroupCreateRequest(
                List.of(new OrderTableDto(orderTableA.getId()),new OrderTableDto(orderTableB.getId()))));

            // when && then
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
        }

    }

    private OrderTableDto getPersistOrderTableRequest(int numberOfGuests, boolean empty) {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(numberOfGuests, empty));
        return new OrderTableDto(orderTable.getId());
    }
}

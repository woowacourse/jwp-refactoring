package kitchenpos.table.application;

import static kitchenpos.test.fixture.OrderFixture.주문;
import static kitchenpos.test.fixture.TableFixture.테이블;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.order.domain.vo.OrderStatus;
import kitchenpos.table.dto.request.TableGroupCreateRequest;
import kitchenpos.table.dto.response.TableGroupResponse;
import kitchenpos.test.ServiceTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Nested
    class 테이블_그룹_추가_시 {

        @Test
        void 정상적인_테이블_그룹이라면_테이블_그룹을_추가한다() {
            //given
            OrderTable orderTableA = orderTableRepository.save(테이블(0, true));
            OrderTable orderTableB = orderTableRepository.save(테이블(0, true));
            TableGroupCreateRequest request = new TableGroupCreateRequest(
                    List.of(orderTableA.getId(), orderTableB.getId()));

            //when
            LocalDateTime now = LocalDateTime.now();
            TableGroupResponse tableGroupResponse = tableGroupService.create(request);

            //then
            assertSoftly(softly -> {
                softly.assertThat(tableGroupResponse.getId()).isNotNull();
                softly.assertThat(tableGroupResponse.getCreatedDate()).isAfter(now);
            });
        }

        @Test
        void 테이블_목록이_비어있으면_예외를_던진다() {
            //given
            TableGroupCreateRequest request = new TableGroupCreateRequest(Collections.emptyList());

            //when, then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블_개수가_2보다_작으면_예외를_던진다() {
            //given
            OrderTable orderTable = orderTableRepository.save(테이블(0, true));
            TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(orderTable.getId()));

            //when, then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_테이블이_있으면_예외를_던진다() {
            //given
            OrderTable orderTableA = orderTableRepository.save(테이블(0, true));
            OrderTable orderTableB = 테이블(0, true);
            ReflectionTestUtils.setField(orderTableB, "id", -1L);
            TableGroupCreateRequest request = new TableGroupCreateRequest(
                    List.of(orderTableA.getId(), orderTableB.getId()));

            //when, then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블이_비어있지_않으면_예외를_던진다() {
            //given®
            OrderTable orderTableA = orderTableRepository.save(테이블(1, false));
            OrderTable orderTableB = orderTableRepository.save(테이블(0, true));
            TableGroupCreateRequest request = new TableGroupCreateRequest(
                    List.of(orderTableA.getId(), orderTableB.getId()));

            //when, then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 이미_그룹화된_테이블이_있으면_예외를_던진다() {
            //given
            OrderTable orderTableA = orderTableRepository.save(테이블(0, true));
            OrderTable orderTableB = orderTableRepository.save(테이블(0, true));
            TableGroupCreateRequest request = new TableGroupCreateRequest(
                    List.of(orderTableA.getId(), orderTableB.getId()));
            tableGroupService.create(request);

            //when, then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 테이블_그룹_해제_시 {

        @Test
        void 정상적인_테이블_그룹이라면_테이블_그룹을_해제한다() {
            //given
            OrderTable orderTableA = orderTableRepository.save(테이블(0, true));
            OrderTable orderTableB = orderTableRepository.save(테이블(0, true));
            TableGroupCreateRequest request = new TableGroupCreateRequest(
                    List.of(orderTableA.getId(), orderTableB.getId()));
            TableGroupResponse response = tableGroupService.create(request);

            //when
            tableGroupService.ungroup(response.getId());

            //then
            assertSoftly(softly -> {
                softly.assertThat(orderTableRepository.findById(orderTableA.getId()).get().getTableGroup()).isNull();
                softly.assertThat(orderTableRepository.findById(orderTableB.getId()).get().getTableGroup()).isNull();
            });
        }

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        void 테이블의_주문이_조리중이거나_식사중이라면_예외를_던진다(OrderStatus orderStatus) {
            //given
            OrderTable orderTableA = orderTableRepository.save(테이블(0, true));
            OrderTable orderTableB = orderTableRepository.save(테이블(0, true));
            TableGroupCreateRequest request = new TableGroupCreateRequest(
                    List.of(orderTableA.getId(), orderTableB.getId()));
            TableGroupResponse response = tableGroupService.create(request);
            orderRepository.save(주문(orderTableA, orderStatus, LocalDateTime.now()));

            //when, then
            assertThatThrownBy(() -> tableGroupService.ungroup(response.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블의_주문이_종료되었으면_예외를_던지지_않는다() {
            //given
            OrderTable orderTableA = orderTableRepository.save(테이블(0, true));
            OrderTable orderTableB = orderTableRepository.save(테이블(0, true));
            TableGroupCreateRequest request = new TableGroupCreateRequest(
                    List.of(orderTableA.getId(), orderTableB.getId()));
            TableGroupResponse response = tableGroupService.create(request);
            orderRepository.save(주문(orderTableA, OrderStatus.COMPLETION, LocalDateTime.now()));

            //when, then
            assertThatNoException()
                    .isThrownBy(() -> tableGroupService.ungroup(response.getId()));
        }
    }
}

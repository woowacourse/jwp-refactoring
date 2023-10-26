package kitchenpos.tablegroup.application;

import static kitchenpos.order.domain.OrderTableFixture.단체_지정_없는_빈_주문_테이블;
import static kitchenpos.order.domain.OrderTableFixture.단체_지정_없는_채워진_주문_테이블;
import static kitchenpos.tablegroup.application.TableGroupServiceTest.TableGroupRequestFixture.단체_지정_생성_요청;
import static kitchenpos.tablegroup.domain.TableGroupFixture.단체_지정;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.common.ServiceTest;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.dto.OrderTableIdRequest;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Nested
    class 단체_지정을_생성할_때 {

        private List<OrderTableIdRequest> orderTableIds;
        private OrderTable emptyOrderTable;
        private OrderTable filledOrderTable;
        private OrderTable groupedOrderTable;

        @BeforeEach
        void setUp() {
            orderTableIds = List.of(
                    new OrderTableIdRequest(orderTableRepository.save(단체_지정_없는_빈_주문_테이블()).getId()),
                    new OrderTableIdRequest(orderTableRepository.save(단체_지정_없는_빈_주문_테이블()).getId())
            );
            emptyOrderTable = orderTableRepository.save(단체_지정_없는_빈_주문_테이블());
            filledOrderTable = orderTableRepository.save(단체_지정_없는_채워진_주문_테이블());

            OrderTable orderTable = 단체_지정_없는_채워진_주문_테이블();
            tableGroupRepository.save(단체_지정());
            groupedOrderTable = orderTableRepository.save(orderTable);
        }

        @Test
        void 정상적으로_생성한다() {
            // given
            TableGroupCreateRequest tableGroup = 단체_지정_생성_요청(orderTableIds);

            // when
            TableGroupResponse createdTableGroup = tableGroupService.create(tableGroup);

            // then
            assertSoftly(softly -> {
                softly.assertThat(createdTableGroup.getId()).isNotNull();
                softly.assertThat(createdTableGroup).usingRecursiveComparison()
                        .ignoringFields("id", "orderTables.id", "orderTables.tableGroupId")
                        .ignoringFieldsOfTypes(LocalDateTime.class)
                        .isEqualTo(TableGroupResponse.of(
                                단체_지정(),
                                List.of(단체_지정_없는_채워진_주문_테이블(), 단체_지정_없는_채워진_주문_테이블())
                        ));
            });
        }

        @Test
        void 주문_테이블_목록이_비었으면_예외를_던진다() {
            // given
            TableGroupCreateRequest invalidTableGroup = 단체_지정_생성_요청(List.of());

            // expect
            assertThatThrownBy(() -> tableGroupService.create(invalidTableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블의_개수가_2_미만이면_예외를_던진다() {
            // given
            TableGroupCreateRequest invalidTableGroup = 단체_지정_생성_요청(
                    List.of(new OrderTableIdRequest(emptyOrderTable.getId())));

            // expect
            assertThatThrownBy(() -> tableGroupService.create(invalidTableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블_목록에_중복된_주문_테이블이_있으면_예외를_던진다() {
            // given
            TableGroupCreateRequest invalidTableGroup = 단체_지정_생성_요청(
                    List.of(new OrderTableIdRequest(emptyOrderTable.getId()),
                            new OrderTableIdRequest(emptyOrderTable.getId())));

            // expect
            assertThatThrownBy(() -> tableGroupService.create(invalidTableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블_목록에_비어있지_않은_테이블이_있으면_예외를_던진다() {
            // given
            TableGroupCreateRequest invalidTableGroup = 단체_지정_생성_요청(
                    List.of(new OrderTableIdRequest(emptyOrderTable.getId()),
                            new OrderTableIdRequest(filledOrderTable.getId())));

            // expect
            assertThatThrownBy(() -> tableGroupService.create(invalidTableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블_목록에_이미_단체_지정된_주문_테이블이_있으면_예외를_던진다() {
            // given
            TableGroupCreateRequest invalidTableGroup = 단체_지정_생성_요청(
                    List.of(new OrderTableIdRequest(emptyOrderTable.getId()),
                            new OrderTableIdRequest(groupedOrderTable.getId())));

            // expect
            assertThatThrownBy(() -> tableGroupService.create(invalidTableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 단체_지정을_해제할_때 {

        private OrderTable emptyOrderTable_A;
        private OrderTable emptyOrderTable_B;

        @BeforeEach
        void setUp() {
            emptyOrderTable_A = orderTableRepository.save(단체_지정_없는_빈_주문_테이블());
            emptyOrderTable_B = orderTableRepository.save(단체_지정_없는_빈_주문_테이블());
        }

        @Test
        void 정상적으로_해제한다() {
            // given
            TableGroupResponse tableGroup = tableGroupService.create(단체_지정_생성_요청(List.of(
                    new OrderTableIdRequest(emptyOrderTable_A.getId()),
                    new OrderTableIdRequest(emptyOrderTable_B.getId())
            )));

            // when
            tableGroupService.ungroup(tableGroup.getId());

            // then
            assertThat(tableGroup.getOrderTables()).usingRecursiveComparison()
                    .ignoringFields("id", "tableGroupId")
                    .isEqualTo(List.of(
                            OrderTableResponse.from(단체_지정_없는_채워진_주문_테이블()),
                            OrderTableResponse.from(단체_지정_없는_채워진_주문_테이블())
                    ));
        }

        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        void 주문_테이블_중_조리_혹은_식사_중인_주문_테이블이_있다면_예외를_던진다(String orderStatus) {
//            // given
//            TableGroupResponse tableGroup = tableGroupService.create(단체_지정_생성_요청(List.of(
//                    new OrderTableIdRequest(emptyOrderTable_A.getId()),
//                    new OrderTableIdRequest(emptyOrderTable_B.getId())
//            )));
//
//            OrderTable orderTable = OrderTableFixture.단체_지정_주문_테이블(tableGroup.getId());
//            orderTable.add(주문(OrderStatus.valueOf(orderStatus)));
//            orderTableRepository.save(orderTable);
//
//            // expect
//            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
//                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    static class TableGroupRequestFixture {

        public static TableGroupCreateRequest 단체_지정_생성_요청(List<OrderTableIdRequest> orderTableIds) {
            return new TableGroupCreateRequest(orderTableIds);
        }
    }
}

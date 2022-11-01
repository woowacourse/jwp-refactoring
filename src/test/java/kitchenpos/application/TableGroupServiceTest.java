package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.fixtures.TestFixtures.단체_지정_생성_요청;
import static kitchenpos.fixtures.TestFixtures.주문_생성;
import static kitchenpos.fixtures.TestFixtures.주문_테이블_생성;
import static kitchenpos.fixtures.TestFixtures.주문_테이블_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.TableGroupCreateRequest;
import kitchenpos.ui.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("MenuGroupService 클래스의")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TableGroupServiceTest {

    @Nested
    class create_메서드는 {

        @Nested
        class 유효한_단체_지정이_입력되면 extends ServiceTest {

            private final OrderTable orderTable1 = 주문_테이블_생성(null, 5, true);
            private final OrderTable orderTable2 = 주문_테이블_생성(null, 5, true);

            @BeforeEach
            void setUp() {
                orderTableRepository.save(orderTable1);
                orderTableRepository.save(orderTable2);
            }

            @Test
            void 해당_테이블_지정을_반환한다() {
                final TableGroupCreateRequest request = 단체_지정_생성_요청(
                        List.of(주문_테이블_요청(1L), 주문_테이블_요청(2L)));
                final TableGroupResponse response = tableGroupService.create(request);

                assertThat(response.getId()).isNotNull();
            }
        }

        @Nested
        class 주문_테이블이_비어있으면 extends ServiceTest {

            private final TableGroupCreateRequest request = 단체_지정_생성_요청(Collections.emptyList());

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.create(request))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 주문_테이블이_2개_미만이면 extends ServiceTest {

            private final OrderTable orderTable = 주문_테이블_생성(null, 5, true);

            @BeforeEach
            void setUp() {
                orderTableRepository.save(orderTable);
            }

            private final TableGroupCreateRequest request = 단체_지정_생성_요청(List.of(주문_테이블_요청(1L)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.create(request))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 입력된_주문_테이블_수가_저장된_주문_테이블_수와_다르다면 extends ServiceTest {

            private final OrderTable orderTable = 주문_테이블_생성(null, 5, true);
            private final TableGroupCreateRequest request = 단체_지정_생성_요청(
                    List.of(주문_테이블_요청(1L), 주문_테이블_요청(2L)));

            @BeforeEach
            void setUp() {
                orderTableRepository.save(orderTable);
            }

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.create(request))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 주문_테이블이_비어있지_않다면 extends ServiceTest {

            private final OrderTable orderTable1 = 주문_테이블_생성(null, 5, false);
            private final OrderTable orderTable2 = 주문_테이블_생성(null, 5, true);

            @BeforeEach
            void setUp() {
                orderTableRepository.save(orderTable1);
                orderTableRepository.save(orderTable2);
            }

            @Test
            void 예외가_발생한다() {
                final TableGroupCreateRequest request = 단체_지정_생성_요청(
                        List.of(주문_테이블_요청(1L), 주문_테이블_요청(2L)));

                assertThatThrownBy(
                        () -> tableGroupService.create(request))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        //
        @Nested
        class 단체_지정이_null이_아니라면 extends ServiceTest {

            private final OrderTable orderTable1 = 주문_테이블_생성(null, 5, true);
            private final OrderTable orderTable2 = 주문_테이블_생성(null, 5, true);

            @BeforeEach
            void setUp() {
                orderTableRepository.save(orderTable1);
                orderTableRepository.save(orderTable2);
            }

            @Test
            void 예외가_발생한다() {
                final TableGroupCreateRequest request = 단체_지정_생성_요청(
                        List.of(주문_테이블_요청(1L), 주문_테이블_요청(2L)));
                tableGroupService.create(request);

                assertThatThrownBy(
                        () -> tableGroupService.create(request))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @Nested
    class ungroup_메서드는 {

        @Nested
        class 유효한_단체_지정이_입력되면 extends ServiceTest {

            private final OrderTable orderTable1 = 주문_테이블_생성(null, 5, true);
            private final OrderTable orderTable2 = 주문_테이블_생성(null, 5, true);

            @BeforeEach
            void setUp() {
                orderTableRepository.save(orderTable1);
                orderTableRepository.save(orderTable2);
            }

            @Test
            void 단체_지정을_취소한다() {
                final TableGroupCreateRequest request = 단체_지정_생성_요청(
                        List.of(주문_테이블_요청(1L), 주문_테이블_요청(2L)));
                tableGroupService.create(request);

                assertThatCode(() -> tableGroupService.ungroup(1L))
                        .doesNotThrowAnyException();
            }
        }

        @Nested
        class 이미_주문이_들어간_단체_지정이면 extends ServiceTest {

            private final OrderTable orderTable1 = 주문_테이블_생성(null, 5, true);
            private final OrderTable orderTable2 = 주문_테이블_생성(null, 5, true);
            private final Order order = 주문_생성(orderTable1, COOKING, LocalDateTime.now(), null);

            @BeforeEach
            void setUp() {
                orderTableRepository.save(orderTable1);
                orderTableRepository.save(orderTable2);
                orderRepository.save(order);
            }

            @Test
            void 예외가_발생한다() {
                final TableGroupCreateRequest request = 단체_지정_생성_요청(
                        List.of(주문_테이블_요청(1L), 주문_테이블_요청(2L)));
                tableGroupService.create(request);

                assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }
}

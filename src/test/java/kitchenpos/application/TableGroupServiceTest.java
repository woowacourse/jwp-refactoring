package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.fixture.OrderFixture.주문;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블;
import static kitchenpos.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.dto.tablegroup.TableGroupCreateRequest;
import kitchenpos.application.dto.tablegroup.TableGroupCreateRequest.OrderTableRequest;
import kitchenpos.application.dto.tablegroup.TableGroupCreateResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.persistence.MenuGroupRepository;
import kitchenpos.persistence.MenuRepository;
import kitchenpos.persistence.OrderRepository;
import kitchenpos.persistence.OrderTableRepository;
import kitchenpos.persistence.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("classpath:truncate.sql")
@TestConstructor(autowireMode = AutowireMode.ALL)
class TableGroupServiceTest {

    private final TableGroupService tableGroupService;
    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    private OrderTable 저장된_주문_테이블1;
    private OrderTable 저장된_주문_테이블2;

    public TableGroupServiceTest(
            final TableGroupService tableGroupService,
            final ProductRepository productRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuRepository menuRepository,
            final OrderTableRepository orderTableRepository,
            final OrderRepository orderRepository
    ) {
        this.tableGroupService = tableGroupService;
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @BeforeEach
    void setUp() {
        final MenuGroup 메뉴_그룹 = 메뉴_그룹(null, "양념 반 후라이드 반");
        final MenuGroup 저장된_메뉴_그룹 = menuGroupRepository.save(메뉴_그룹);

        final Product 저장된_양념_치킨 = productRepository.save(상품(null, "양념 치킨", BigDecimal.valueOf(12000, 2)));
        final Product 저장된_후라이드_치킨 = productRepository.save(상품(null, "후라이드 치킨", BigDecimal.valueOf(10000, 2)));
        final MenuProduct 메뉴_상품_1 = 메뉴_상품(null, null, 저장된_양념_치킨, 1);
        final MenuProduct 메뉴_상품_2 = 메뉴_상품(null, null, 저장된_후라이드_치킨, 1);

        final Menu 메뉴 = 메뉴(null, "메뉴", BigDecimal.valueOf(22000, 2), 저장된_메뉴_그룹, List.of(메뉴_상품_1, 메뉴_상품_2));
        menuRepository.save(메뉴);

        저장된_주문_테이블1 = orderTableRepository.save(주문_테이블(null, null, 2, true));
        저장된_주문_테이블2 = orderTableRepository.save(주문_테이블(null, null, 3, true));
    }

    @Nested
    class 테이블_그룹_등록_시 {

        @Test
        void 테이블_그룹을_정상적으로_등록한다() {
            // given
            final TableGroupCreateRequest 테이블_그룹_요청값 = new TableGroupCreateRequest(
                    List.of(new OrderTableRequest(저장된_주문_테이블1.getId()),
                            new OrderTableRequest(저장된_주문_테이블2.getId()))
            );

            // when
            final TableGroupCreateResponse 저장된_테이블_그룹 = tableGroupService.create(테이블_그룹_요청값);

            // then
            assertThat(저장된_테이블_그룹.getOrderTables().get(0).getTableGroupId())
                    .isEqualTo(저장된_테이블_그룹.getId());
        }

        @Test
        void 주문_테이블의_개수가_2미만이면_예외가_발생한다() {
            // given
            final TableGroupCreateRequest 테이블_그룹_요청값 = new TableGroupCreateRequest(
                    List.of(new OrderTableRequest(저장된_주문_테이블1.getId()))
            );

            // expected
            assertThatThrownBy(() -> tableGroupService.create(테이블_그룹_요청값))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("2개 이상의 주문 테이블을 그룹으로 만들 수 있습니다.");
        }

        @Test
        void 입력받은_주문_테이블의_개수와_조회한_주문_테이블의_개수가_다르면_예외가_발생한다() {
            // given
            final OrderTable 저장되지_않은_주문_테이블 = 주문_테이블(null, null, 0, true);
            final TableGroupCreateRequest 테이블_그룹_요청값 = new TableGroupCreateRequest(
                    List.of(new OrderTableRequest(저장된_주문_테이블1.getId()),
                            new OrderTableRequest(저장되지_않은_주문_테이블.getId()))
            );

            // expected
            assertThatThrownBy(() -> tableGroupService.create(테이블_그룹_요청값))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("주문 테이블의 정보가 올바르지 않습니다.");
        }

        @Test
        void 입력받은_주문_테이블이_비어있지_않으면_예외가_발생한다() {
            // given
            final OrderTable 저장된_주문_테이블1 = orderTableRepository.save(주문_테이블(1L, null, 2, false));
            final TableGroupCreateRequest 테이블_그룹_요청값 = new TableGroupCreateRequest(
                    List.of(new OrderTableRequest(저장된_주문_테이블1.getId()),
                            new OrderTableRequest(저장된_주문_테이블2.getId()))
            );

            // expected
            assertThatThrownBy(() -> tableGroupService.create(테이블_그룹_요청값))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("주문 테이블이 비어있지 않습니다.");
        }

        @Test
        void 입력받은_주문_테이블이_이미_테이블_그룹에_등록되어_있으면_예외가_발생한다() {
            // given
            final OrderTable 저장된_주문_테이블1 = orderTableRepository.save(주문_테이블(null, null, 2, true));
            final TableGroupCreateRequest 테이블_그룹_요청값1 = new TableGroupCreateRequest(
                    List.of(new OrderTableRequest(저장된_주문_테이블1.getId()),
                            new OrderTableRequest(저장된_주문_테이블2.getId()))
            );

            tableGroupService.create(테이블_그룹_요청값1);

            final TableGroupCreateRequest 테이블_그룹_요청값2 = new TableGroupCreateRequest(
                    List.of(new OrderTableRequest(저장된_주문_테이블1.getId()),
                            new OrderTableRequest(저장된_주문_테이블2.getId()))
            );

            // expected
            assertThatThrownBy(() -> tableGroupService.create(테이블_그룹_요청값2))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 테이블_그룹_해제_시 {

        @Test
        void 테이블_그룹을_정상적으로_해제한다() {
            // given
            final TableGroupCreateRequest 테이블_그룹_요청값 = new TableGroupCreateRequest(
                    List.of(new OrderTableRequest(저장된_주문_테이블1.getId()),
                            new OrderTableRequest(저장된_주문_테이블2.getId()))
            );
            final TableGroupCreateResponse 저장된_테이블_그룹 = tableGroupService.create(테이블_그룹_요청값);

            // expected
            assertDoesNotThrow(() -> tableGroupService.ungroup(저장된_테이블_그룹.getId()));
        }

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        void 주문_상태가_COOKING_또는_MEAL인_경우_예외가_발생한다(final OrderStatus 주문_상태) {
            // given
            final TableGroupCreateRequest 테이블_그룹_요청값 = new TableGroupCreateRequest(
                    List.of(new OrderTableRequest(저장된_주문_테이블1.getId()),
                            new OrderTableRequest(저장된_주문_테이블2.getId()))
            );
            final TableGroupCreateResponse 저장된_테이블_그룹 = tableGroupService.create(테이블_그룹_요청값);

            final Order 주문 = 주문(null, 저장된_주문_테이블1, 주문_상태, null, Collections.emptyList());
            orderRepository.save(주문);

            // expected
            assertThatThrownBy(() -> tableGroupService.ungroup(저장된_테이블_그룹.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}

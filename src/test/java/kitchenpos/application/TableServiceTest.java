package kitchenpos.application;

import static java.lang.Long.MAX_VALUE;
import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.fixture.OrderFixture.주문;
import static kitchenpos.fixture.OrderLineItemFixture.주문_항목;
import static kitchenpos.fixture.OrderTableFixture.테이블;
import static kitchenpos.fixture.ProductFixture.상품;
import static kitchenpos.fixture.TableGroupFixture.단체_지정;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.test.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class TableServiceTest {

    @Autowired
    private TableService sut;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    private Menu menu;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹("피자"));
        Product product = productRepository.save(상품("치즈 피자", 8900L));
        MenuProduct menuProduct = 메뉴_상품(product, 1L);
        menu = menuRepository.save(메뉴("피자", 8900L, menuGroup.getId(), List.of(menuProduct)));
    }

    @Test
    void 테이블을_생성한다() {
        // given
        OrderTableRequest request = new OrderTableRequest(null, 0L, 0, true);

        // when
        OrderTableResponse result = sut.create(request);

        // then
        assertThat(orderTableRepository.findById(result.getId())).isPresent();
    }

    @Nested
    class 테이블의_상태를_변경할때 {

        private final OrderTableRequest request = new OrderTableRequest(null, 0L, 0, true);

        @Test
        void 존재하지_않는_테이블인_경우_예외를_던진다() {
            // expect
            assertThatThrownBy(() -> sut.changeEmpty(MAX_VALUE, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 테이블입니다.");
        }

        @Test
        void 단체_지정이_되어있는_테이블인_경우_예외를_던진다() {
            // given
            TableGroup tableGroup = tableGroupRepository.save(단체_지정());
            OrderTable orderTable = orderTableRepository.save(테이블(false, 0, tableGroup));

            // expect
            assertThatThrownBy(() -> sut.changeEmpty(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("단체 지정이 되어있는 경우 테이블의 상태를 변경할 수 없습니다.");
        }

        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        @ParameterizedTest(name = "주문 상태가 {0}인 경우 예외를 던진다")
        void 테이블의_주문_상태가_조리중이거나_식사중인_경우_예외를_던진다(OrderStatus orderStatus) {
            // given
            OrderTable orderTable = orderTableRepository.save(테이블(false));
            OrderLineItem orderLineItem = 주문_항목(menu.getId(), 2L);
            orderRepository.save(주문(orderTable, orderStatus, List.of(orderLineItem)));

            // expect
            assertThatThrownBy(() -> sut.changeEmpty(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블의 주문 상태가 조리중이거나 식사중인 경우 테이블의 상태를 변경할 수 없습니다.");
        }

        @Test
        void 테이블의_주문_상태가_완료인_경우_테이블의_상태를_변경한다() {
            // given
            OrderTable orderTable = orderTableRepository.save(테이블(false));
            OrderLineItem orderLineItem = 주문_항목(menu.getId(), 2L);
            orderRepository.save(주문(orderTable, COMPLETION, List.of(orderLineItem)));

            // when
            OrderTableResponse result = sut.changeEmpty(orderTable.getId(), request);

            // then
            assertThat(result.isEmpty()).isTrue();
        }
    }

    @Nested
    class 테이블의_손님수를_지정할_때 {

        private final OrderTableRequest request = new OrderTableRequest(null, 0L, 0, false);

        @Test
        void 손님수가_0명보다_적으면_예외를_던진다() {
            // given
            OrderTableRequest request = new OrderTableRequest(null, 0L, -1, false);
            OrderTable orderTable = orderTableRepository.save(테이블(false));

            // expect
            assertThatThrownBy(() -> sut.changeNumberOfGuests(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("방문한 손님 수는 음수가 될 수 없습니다.");
        }

        @Test
        void 변경하려는_테이블이_없는_경우_예외를_던진다() {
            // expect
            assertThatThrownBy(() -> sut.changeNumberOfGuests(MAX_VALUE, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블을 찾을 수 없습니다.");
        }

        @Test
        void 빈_테이블_경우_예외를_던진다() {
            // given
            OrderTable orderTable = orderTableRepository.save(테이블(true));

            // expect
            assertThatThrownBy(() -> sut.changeNumberOfGuests(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("빈 테이블에는 손님을 지정할 수 없습니다.");
        }

        @Test
        void 빈_테이블이_아닌_경우_인원이_정상적으로_변경된다() {
            // given
            OrderTableRequest request = new OrderTableRequest(null, 0L, 0, false);
            OrderTable orderTable = orderTableRepository.save(테이블(false, 1));

            // when
            OrderTableResponse result = sut.changeNumberOfGuests(orderTable.getId(), request);

            // then
            assertThat(result.getNumberOfGuests()).isZero();
        }
    }
}

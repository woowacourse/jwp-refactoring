package kitchenpos.repository;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("주문 레파지토리 테스트")
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Nested
    class 주문_테이블_아이디와_상태_목록에_따라_주문이_존재하는지_확인할_때 {

        private OrderTable orderTable;

        @BeforeEach
        void setUp() {
            final MenuGroup menuGroup = menuGroupRepository.save(MenuGroupFixture.메뉴_그룹_엔티티_생성());
            final Product product = productRepository.save(ProductFixture.상품_엔티티_생성());
            final Menu menu = menuRepository.save(MenuFixture.메뉴_엔티티_생성(menuGroup, List.of(product)));
            orderTable = orderTableRepository.save(OrderTableFixture.주문_테이블_엔티티_생성());
            orderRepository.save(OrderFixture.조리_상태의_주문_엔티티_생성(orderTable, menu));
        }

        @Test
        void 주문_테이블_아이디가_동일하고_모든_주문_상태_포함되는_주문이_존재한다면_참을_반환한다() {
            // given
            final List<String> orderStatus = List.of(COOKING.name(), MEAL.name(), COMPLETION.name());

            // when
            final boolean actual = orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), orderStatus);

            // then
            assertThat(actual).isTrue();
        }

        @Test
        void 주문_테이블_아이디가_동일하지만_주문_상태_목록에_포함되지_않는_주문은_거짓을_반환한다() {
            // given
            final List<String> orderStatus = List.of(MEAL.name(), COMPLETION.name());

            // when
            final boolean actual = orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), orderStatus);

            // then
            assertThat(actual).isFalse();
        }

        @Test
        void 주문_테이블_아이디가_동일한_주문이_존재하지_않는다면_거짓을_반환한다() {
            // given
            final List<String> orderStatus = List.of(COOKING.name(), MEAL.name(), COMPLETION.name());
            final long unsavedId = 999L;

            // when
            final boolean actual = orderRepository.existsByOrderTableIdAndOrderStatusIn(unsavedId, orderStatus);

            // then
            assertThat(actual).isFalse();
        }
    }

    @Nested
    class 주문_테이블_아이디_목록과_상태_목록에_따라_주문이_존재하는지_확인할_때 {

        private List<OrderTable> orderTables;

        @BeforeEach
        void setUp() {
            final MenuGroup menuGroup = menuGroupRepository.save(MenuGroupFixture.메뉴_그룹_엔티티_생성());
            final Product product = productRepository.save(ProductFixture.상품_엔티티_생성());
            final Menu menu = menuRepository.save(MenuFixture.메뉴_엔티티_생성(menuGroup, List.of(product)));
            orderTables = orderTableRepository.saveAll(OrderTableFixture.주문_테이블_엔티티들_생성(3));
            orderRepository.saveAll(OrderFixture.조리_상태의_주문_엔티티들_생성(orderTables, menu));
        }

        @Test
        void 주문_테이블_아이디_목록이_모두_존재하고_모든_주문_상태_포함되는_주문이_존재한다면_참을_반환한다() {
            // given
            final List<Long> orderTableIds = OrderTableFixture.주문_테이블_엔티티의_아이디들(orderTables);
            final List<String> orderStatus = List.of(COOKING.name(), MEAL.name(), COMPLETION.name());

            // when
            final boolean actual = orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatus);

            // then
            assertThat(actual).isTrue();
        }

        @Test
        void 주문_테이블_아이디는_모두_동일하지만_주문_상태_목록에_포함되지_않는_주문은_거짓을_반환한다() {
            // given
            final List<Long> orderTableIds = OrderTableFixture.주문_테이블_엔티티의_아이디들(orderTables);
            final List<String> orderStatus = List.of(MEAL.name(), COMPLETION.name());

            // when
            final boolean actual = orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatus);

            // then
            assertThat(actual).isFalse();
        }

        @Test
        void 주문_테이블_아이디가_모두_존재하지_않는다면_거짓을_반환한다() {
            // given
            final List<String> orderStatus = List.of(COOKING.name(), MEAL.name(), COMPLETION.name());
            final long unsavedId = 999L;
            final List<Long> orderTableIds = List.of(unsavedId);

            // when
            final boolean actual = orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatus);

            // then
            assertThat(actual).isFalse();
        }
    }
}

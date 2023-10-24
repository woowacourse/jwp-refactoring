package kitchenpos.application;

import kitchenpos.common.ServiceTestConfig;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.fixture.TableGroupFixture;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.repository.TableGroupRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("테이블 서비스 테스트")
class TableServiceTest extends ServiceTestConfig {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    @Nested
    class 테이블_등록 {

        @Test
        void 주문_테이블을_등록한다() {
            // given
            final OrderTable orderTable = OrderTableFixture.주문_테이블_생성();

            // when
            final OrderTable actual = tableService.create(orderTable);

            // then
            assertThat(actual.getId()).isPositive();
        }
    }

    @Nested
    class 주문_테이블_목록_조회 {

        @Test
        void 주문_테이블_목록을_조회한다() {
            // given
            final List<OrderTable> orderTables = orderTableRepository.saveAll(OrderTableFixture.주문_테이블들_생성(3));

            // when
            final List<OrderTable> actual = tableService.list();

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).hasSize(3);
                softAssertions.assertThat(actual.get(0)).isEqualTo(orderTables.get(0));
                softAssertions.assertThat(actual.get(1)).isEqualTo(orderTables.get(1));
                softAssertions.assertThat(actual.get(2)).isEqualTo(orderTables.get(2));
            });
        }
    }

    @Nested
    class 주문_테이블의_empty_변경 {

        @Test
        void 주문_테이블의_empty_값을_참에서_거짓으로_변경할_수_있다() {
            // given
            final OrderTable orderTable = orderTableRepository.save(OrderTableFixture.빈_테이블_생성());
            final OrderTable emptyIsFalseOrderTable = OrderTableFixture.비지_않은_테이블_생성();

            // when
            final OrderTable actual = tableService.changeEmpty(orderTable.getId(), emptyIsFalseOrderTable);

            // then
            assertThat(actual.isEmpty()).isFalse();
        }

        @Test
        void 주문_테이블의_empty_값을_거짓에서_참으로_변경할_수_있다() {
            // given
            final OrderTable orderTable = orderTableRepository.save(OrderTableFixture.비지_않은_테이블_생성());
            final OrderTable emptyIsTrueOrderTable = OrderTableFixture.빈_테이블_생성();

            // when
            final OrderTable actual = tableService.changeEmpty(orderTable.getId(), emptyIsTrueOrderTable);

            // then
            assertThat(actual.isEmpty()).isTrue();
        }

        @Test
        void 주문_테이블의_empty_값_변경시_단체_지정_아이디가_null이_아니라면_예외를_반환한다() {
            // given
            final OrderTable orderTable = orderTableRepository.save(OrderTableFixture.주문_테이블_생성());
            tableGroupRepository.save(TableGroupFixture.단체_지정_생성(List.of(orderTable)));
            final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), OrderTableFixture.빈_테이블_생성()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 특정_주문_테이블중_조리_혹은_식사_상태인_것이_존재한다면_예외를_반환한다() {
            // given
            final OrderTable orderTable = orderTableRepository.save(OrderTableFixture.주문_테이블_생성());
            tableGroupRepository.save(TableGroupFixture.단체_지정_생성(List.of(orderTable)));
            final MenuGroup menuGroup = menuGroupRepository.save(MenuGroupFixture.메뉴_그룹_생성());
            final List<Product> products = productRepository.saveAll(ProductFixture.상품들_생성(2));
            final Menu menu = menuRepository.save(MenuFixture.메뉴_엔티티_생성(menuGroup, products));
            orderRepository.save(OrderFixture.조리_상태의_주문_생성(orderTable, menu));

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), OrderTableFixture.빈_테이블_생성()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 주문_테이블_상태_변경 {

        @Test
        void 주문_테이블의_방문자_수의_값을_변경할_수_있다() {
            // given
            final OrderTable orderTable = orderTableRepository.save(OrderTableFixture.주문_테이블_생성(4));
            final OrderTable orderTableWithTwoGuests = OrderTableFixture.주문_테이블_생성(2);

            // when
            final OrderTable actual = tableService.changeNumberOfGuests(orderTable.getId(), orderTableWithTwoGuests);

            // then
            assertThat(actual.getNumberOfGuests()).isEqualTo(2);
        }

        @Test
        void 주문_테이블의_방문자_수를_0미만으로_변경할시_예외를_반환한다() {
            // given
            final OrderTable orderTable = orderTableRepository.save(OrderTableFixture.주문_테이블_생성(4));

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), OrderTableFixture.주문_테이블_생성(-4)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_주문_테이블의_방문자_수를_변경할시_예외를_반환한다() {
            // given
            final Long unsavedId = 999L;

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(unsavedId, OrderTableFixture.주문_테이블_생성(4)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블의_방문자_수를_변경할시_해당_주문_테이블의_empty가_참이라면_예외를_반환한다() {
            // given
            final OrderTable orderTable = orderTableRepository.save(OrderTableFixture.빈_테이블_생성());

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), OrderTableFixture.주문_테이블_생성(2)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}

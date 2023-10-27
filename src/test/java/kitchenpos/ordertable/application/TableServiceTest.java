package kitchenpos.ordertable.application;

import kitchenpos.exception.InvalidChangeOrderTableNumberOfGuests;
import kitchenpos.exception.InvalidOrderToChangeEmptyException;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.config.ServiceTestConfig;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.exception.InvalidUpdateNumberOfGuestsException;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.fixture.TableGroupFixture;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import kitchenpos.menugroup.ui.dto.ChangeOrderTableEmptyRequest;
import kitchenpos.ordertable.ui.dto.ChangeOrderTableNumberOfGuestsRequest;
import kitchenpos.ordertable.ui.dto.OrderTableRequest;
import kitchenpos.ordertable.ui.dto.OrderTableResponse;
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
            final OrderTableRequest orderTable = OrderTableFixture.주문_테이블_요청_dto_생성();

            // when
            final OrderTableResponse actual = tableService.create(orderTable);

            // then
            assertThat(actual.getId()).isPositive();
        }
    }

    @Nested
    class 주문_테이블_목록_조회 {

        @Test
        void 주문_테이블_목록을_조회한다() {
            // given
            final List<OrderTable> orderTables = orderTableRepository.saveAll(OrderTableFixture.주문_테이블_엔티티들_생성(3));

            // when
            final List<OrderTableResponse> actual = tableService.list();

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).hasSize(3);
                softAssertions.assertThat(actual.get(0).getId()).isEqualTo(orderTables.get(0).getId());
                softAssertions.assertThat(actual.get(1).getId()).isEqualTo(orderTables.get(1).getId());
                softAssertions.assertThat(actual.get(2).getId()).isEqualTo(orderTables.get(2).getId());
            });
        }
    }

    @Nested
    class 주문_테이블의_empty_변경 {

        @Test
        void 주문_테이블의_empty_값을_참에서_거짓으로_변경할_수_있다() {
            // given
            final OrderTable orderTable = orderTableRepository.save(OrderTableFixture.빈_테이블_엔티티_생성());
            final ChangeOrderTableEmptyRequest changeEmptyRequest = new ChangeOrderTableEmptyRequest(false);

            // when
            final OrderTableResponse actual = tableService.changeEmpty(orderTable.getId(), changeEmptyRequest);

            // then
            assertThat(actual.isEmpty()).isFalse();
        }

        @Test
        void 주문_테이블의_empty_값을_거짓에서_참으로_변경할_수_있다() {
            // given
            final OrderTable orderTable = orderTableRepository.save(OrderTableFixture.비지_않은_테이블_엔티티_생성());
            final ChangeOrderTableEmptyRequest changeEmptyRequest = new ChangeOrderTableEmptyRequest(true);

            // when
            final OrderTableResponse actual = tableService.changeEmpty(orderTable.getId(), changeEmptyRequest);

            // then
            assertThat(actual.isEmpty()).isTrue();
        }

        @Test
        void 주문_테이블의_empty_값_변경시_단체_지정_아이디가_null이_아니라면_예외를_반환한다() {
            // given
            final List<OrderTable> orderTables = orderTableRepository.saveAll(OrderTableFixture.빈_테이블_엔티티들_생성(2));
            final OrderTable targetOrderTable = orderTables.get(0);
            tableGroupRepository.save(TableGroupFixture.단체_지정_엔티티_생성(orderTables));
            final ChangeOrderTableEmptyRequest changeEmptyRequest = new ChangeOrderTableEmptyRequest(true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(targetOrderTable.getId(), changeEmptyRequest))
                    .isInstanceOf(InvalidOrderToChangeEmptyException.class)
                    .hasMessage("단체 지정이 정해지지 않아 상태 변경이 불가능합니다.");
        }

        @Test
        void 특정_주문_테이블중_조리_혹은_식사_상태인_것이_존재한다면_예외를_반환한다() {
            // given
            final List<OrderTable> orderTables = orderTableRepository.saveAll(OrderTableFixture.빈_테이블_엔티티들_생성(2));
            final OrderTable targetOrderTable = orderTables.get(0);
            tableGroupRepository.save(TableGroupFixture.단체_지정_엔티티_생성(orderTables));
            final MenuGroup menuGroup = menuGroupRepository.save(MenuGroupFixture.메뉴_그룹_엔티티_생성());
            final List<Product> products = productRepository.saveAll(ProductFixture.상품_엔티티들_생성(2));
            final Menu menu = menuRepository.save(MenuFixture.메뉴_엔티티_생성(menuGroup, products));
            orderRepository.save(OrderFixture.조리_상태의_주문_엔티티_생성(targetOrderTable, menu));
            final ChangeOrderTableEmptyRequest changeEmptyRequest = new ChangeOrderTableEmptyRequest(true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(targetOrderTable.getId(), changeEmptyRequest))
                    .isInstanceOf(InvalidOrderToChangeEmptyException.class)
                    .hasMessage("단체 지정이 정해지지 않아 상태 변경이 불가능합니다.");
        }
    }

    @Nested
    class 주문_테이블_방문자_수_변경 {

        @Test
        void 주문_테이블의_방문자_수의_값을_변경할_수_있다() {
            // given
            final OrderTable orderTable = orderTableRepository.save(OrderTableFixture.주문_테이블_엔티티_생성(4));
            final ChangeOrderTableNumberOfGuestsRequest changeNumberOfGuestsRequest = new ChangeOrderTableNumberOfGuestsRequest(2);

            // when
            final OrderTableResponse actual = tableService.changeNumberOfGuests(orderTable.getId(), changeNumberOfGuestsRequest);

            // then
            assertThat(actual.getNumberOfGuests()).isEqualTo(2);
        }

        @Test
        void 주문_테이블의_방문자_수를_0미만으로_변경할시_예외를_반환한다() {
            // given
            final OrderTable orderTable = orderTableRepository.save(OrderTableFixture.주문_테이블_엔티티_생성(4));
            final ChangeOrderTableNumberOfGuestsRequest changeNumberOfGuestsRequest = new ChangeOrderTableNumberOfGuestsRequest(-4);


            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), changeNumberOfGuestsRequest))
                    .isInstanceOf(InvalidUpdateNumberOfGuestsException.class)
                    .hasMessage("방문한 손님 수는 음수가 될 수 없습니다.");
        }

        @Test
        void 존재하지_않는_주문_테이블의_방문자_수를_변경할시_예외를_반환한다() {
            // given
            final Long unsavedId = 999L;
            final ChangeOrderTableNumberOfGuestsRequest changeNumberOfGuestsRequest = new ChangeOrderTableNumberOfGuestsRequest(4);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(unsavedId, changeNumberOfGuestsRequest))
                    .isInstanceOf(NotFoundOrderTableException.class)
                    .hasMessage("해당 주문 테이블이 존재하지 않습니다.");
        }

        @Test
        void 주문_테이블의_방문자_수를_변경할시_해당_주문_테이블의_empty가_참이라면_예외를_반환한다() {
            // given
            final OrderTable orderTable = orderTableRepository.save(OrderTableFixture.빈_테이블_엔티티_생성());
            final ChangeOrderTableNumberOfGuestsRequest changeNumberOfGuestsRequest = new ChangeOrderTableNumberOfGuestsRequest(2);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), changeNumberOfGuestsRequest))
                    .isInstanceOf(InvalidChangeOrderTableNumberOfGuests.class)
                    .hasMessage("주문 테이블이 빈 상태라면 사용자 수를 변경할 수 없습니다.");
        }
    }
}

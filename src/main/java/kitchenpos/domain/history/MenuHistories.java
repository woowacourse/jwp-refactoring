package kitchenpos.domain.history;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class MenuHistories {

    private final List<MenuHistory> menuHistories;

    public MenuHistories(List<MenuHistory> menuHistories) {
        this.menuHistories = menuHistories;
    }

    public Long getLatestPrice(Long menuId, LocalDateTime searchTime) {
        Optional<MenuHistory> result = filterByMenuIdLatest(menuId, searchTime);
        return result.orElseThrow(IllegalArgumentException::new).getPriceAtTime();
    }

    public String getLatestName(Long menuId, LocalDateTime searchTime) {
        Optional<MenuHistory> result = filterByMenuIdLatest(menuId, searchTime);
        return result.orElseThrow(IllegalArgumentException::new).getNameAtTime();
    }

    private Optional<MenuHistory> filterByMenuIdLatest(Long menuId, LocalDateTime searchTime) {
        List<MenuHistory> filteredHistoriesByMenuId = this.menuHistories.stream()
                .filter(each -> Objects.equals(each.getMenuId(), menuId))
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toUnmodifiableList());
        return filteredHistoriesByMenuId.stream()
                .filter(each -> each.getCreateDate().isBefore(searchTime) || each.getCreateDate().isEqual(searchTime))
                .findFirst();
    }

    public List<MenuHistory> getMenuHistories() {
        return menuHistories;
    }
}

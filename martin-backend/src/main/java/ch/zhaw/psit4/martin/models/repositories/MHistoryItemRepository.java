package ch.zhaw.psit4.martin.models.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import ch.zhaw.psit4.martin.models.MHistoryItem;

public interface MHistoryItemRepository extends BaseRepository<MHistoryItem, Integer> {
	@Query("SELECT h FROM MHistoryItem h ORDER BY h.date DESC")
	List<MHistoryItem> getLimitedHistory(Pageable pageable);
}

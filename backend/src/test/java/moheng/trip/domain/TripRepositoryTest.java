package moheng.trip.domain;

import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import moheng.config.slice.RepositoryTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TripRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private TripRepository tripRepository;

    @DisplayName("방문수 기준 최대 상위 30개의 여행지를 내림차순으로 찾는다.")
    @Test
    void 방문수_기준_최대_상위_30개의_여헹지를_찾는다() {
        tripRepository.save(new Trip("여행지1", "장소", 1L, "설명", "이미지", 1L));
        tripRepository.save(new Trip("여행지1", "장소", 4L, "설명", "이미지", 4L));
        tripRepository.save(new Trip("여행지1", "장소", 2L, "설명", "이미지", 2L));
        tripRepository.save(new Trip("여행지1", "장소", 3L, "설명", "이미지", 3L));

        List<Trip> trips = tripRepository.findTop30ByOrderByVisitedCountDesc();

        assertAll(() -> {
            long visitedCount = 4L;
            for(Trip trip : trips) {
                assertThat(trip.getVisitedCount()).isEqualTo(visitedCount);
                visitedCount--;
            }
        });
    }

    @DisplayName("contentId 에 매핑되는 여행지들을 찾는다.")
    @Test
    void contentId_에_매핑되는_여행지들을_찾는다() {
        // given
        tripRepository.save(new Trip("여행지1", "장소", 1L, "설명", "이미지", 1L));
        tripRepository.save(new Trip("여행지2", "장소", 2L, "설명", "이미지", 2L));
        tripRepository.save(new Trip("여행지3", "장소", 3L, "설명", "이미지", 3L));
        tripRepository.save(new Trip("여행지4", "장소", 4L, "설명", "이미지", 4L));
        List<Long> contentIds = List.of(1L, 2L, 3L, 4L);

        // when, then
        assertThat(tripRepository.findTripsByContentIds(contentIds)).hasSize(4);
    }
}

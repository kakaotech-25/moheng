package moheng.trip.application;

import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import groovy.transform.AutoImplement;
import moheng.config.slice.ServiceTestConfig;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.exception.NoExistMemberException;
import moheng.recommendtrip.application.RecommendTripService;
import moheng.recommendtrip.domain.RecommendTrip;
import moheng.recommendtrip.domain.RecommendTripRepository;
import moheng.trip.domain.Trip;
import moheng.trip.dto.FindTripWithSimilarTripsResponse;
import moheng.trip.dto.TripCreateRequest;
import moheng.trip.exception.NoExistRecommendTripException;
import moheng.trip.exception.NoExistTripException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TripServiceTest extends ServiceTestConfig {
    private static final long HIGHEST_PRIORITY_RANK = 1L;
    private static final long LOWEST_PRIORITY_RANK = 10L;

    @Autowired
    private TripService tripService;

    @Autowired
    private RecommendTripRepository recommendTripRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("여행지를 생성한다.")
    @Test
    void 여행지를_생성한다() {
        // given
        assertDoesNotThrow(() -> tripService.createTrip(new TripCreateRequest("롯데월드2", "서울특별시 송파구2", 1000000L,
                "서울 롯데월드는 신나는 여가와 엔터테인먼트를 꿈꾸는 사람들과 갈수록 늘어나는 외국인 관광 활성화를 위해 운영하는 테마파크예요.2",
                "https://lotte-world-image2.png")));
    }

    @DisplayName("여행지 정보를 저장한다.")
    @Test
    void 여행지_정보를_저장한다() {
        // given
        assertDoesNotThrow(() -> tripService.save(new Trip("롯데월드2", "서울특별시 송파구2", 1000000L,
                "서울 롯데월드는 신나는 여가와 엔터테인먼트를 꿈꾸는 사람들과 갈수록 늘어나는 외국인 관광 활성화를 위해 운영하는 테마파크예요.2",
                "https://lotte-world-image2.png")));
    }

    @DisplayName("여행지 정보를 가져온다.")
    @Test
    void 여행지_정보를_가져온다() {
        // given
        tripService.save(new Trip("롯데월드", "서울특별시 송파구", 1000000L,
                "서울 롯데월드는 신나는 여가와 엔터테인먼트를 꿈꾸는 사람들과 갈수록 늘어나는 외국인 관광 활성화를 위해 운영하는 테마파크예요.",
                "https://lotte-world-image.png"));

        // when, then
        assertDoesNotThrow(() -> tripService.findById(1L));
    }

    @DisplayName("여행지 정보가 없으면 예외가 발생한다.")
    @Test
    void 여행지_정보가_없으면_예외가_발생한다() {
        // given, when, then
        assertThatThrownBy(() -> tripService.findById(-1L)).
                isInstanceOf(NoExistTripException.class);
    }

    @DisplayName("contentId 로 여행지 정보를 가져온다.")
    @Test
    void contentId_로_여행지_정보를_가져온다() {
        // given
        tripService.save(new Trip("롯데월드", "서울특별시 송파구", 1000000L,
                "서울 롯데월드는 신나는 여가와 엔터테인먼트를 꿈꾸는 사람들과 갈수록 늘어나는 외국인 관광 활성화를 위해 운영하는 테마파크예요.",
                "https://lotte-world-image.png"));

        // when, then
        assertDoesNotThrow(() -> tripService.findByContentId(1000000L));
    }

    @DisplayName("contentId 에 해당하는 여행지 정보가 없으면 예외가 발생한다.")
    @Test
    void contentId_에_해당하는여행지_정보가_없으면_예외가_발생한다() {
        // given, when, then
        assertThatThrownBy(() -> tripService.findByContentId(-1L)).
                isInstanceOf(NoExistTripException.class);
    }

    @DisplayName("방문 수 기준 상위 여행지들을 조회한다.")
    @Test
    void 방문_수_기준_상위_여행지들을_조회한다() {
         // given
        tripService.save(new Trip("여행지1", "서울", 1L, "설명", "https://image.png", 0L));
        tripService.save(new Trip("여행지2", "서울", 2L, "설명", "https://image.png", 2L));
        tripService.save(new Trip("여행지3", "서울", 3L, "설명", "https://image.png", 1L));

        // when, then
        assertThat(tripService.findTop30OrderByVisitedCountDesc()
                .getFindTripResponses().size()).isEqualTo(3);
    }

    @DisplayName("방문 수 기준 상위 여행지들을 오름차순으로 조회한다.")
    @Test
    void 방문_수_기준_상위_여행지들을_오름차순으로_조회한다() {
        // given
        tripService.save(new Trip("여행지1", "서울", 1L, "설명", "https://image.png", 0L));
        tripService.save(new Trip("여행지2", "서울", 2L, "설명", "https://image.png", 2L));
        tripService.save(new Trip("여행지3", "서울", 3L, "설명", "https://image.png", 1L));

        // when, then
        assertAll(() -> {
            assertThat(tripService.findTop30OrderByVisitedCountDesc()
                    .getFindTripResponses().get(0).getName()).isEqualTo("여행지2");

            assertThat(tripService.findTop30OrderByVisitedCountDesc()
                    .getFindTripResponses().get(1).getName()).isEqualTo("여행지3");

            assertThat(tripService.findTop30OrderByVisitedCountDesc()
                    .getFindTripResponses().get(2).getName()).isEqualTo("여행지1");
        });
    }

    @DisplayName("현재 여행지를 비슷한 여행지와 함께 조회한다.")
    @Test
    void 현재_여행지를_비슷한_여행지와_함께_조회한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        tripService.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        tripService.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        tripService.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        tripService.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        Trip trip = tripService.findById(1L);
        recommendTripRepository.save(new RecommendTrip(trip, member, 1L));

        // when
        FindTripWithSimilarTripsResponse response = tripService.findWithSimilarOtherTrips(trip.getId(), member.getId());

        // then
        assertAll(() -> {
            assertThat(response.getFindTripResponse()).isNotNull();
            assertThat(response.getSimilarTripResponses().getFindTripResponses().size()).isEqualTo(3);
        });
    }


    @DisplayName("현재 여행지를 조회하면 모든 유저에 대한 총 방문 횟수가 증가한다.")
    @Test
    void 현재_여행지를_조회하면_모든_유저에_대한_총_방문_횟수가_증가한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        tripService.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        tripService.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        tripService.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        tripService.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        Trip trip = tripService.findById(1L);
        recommendTripRepository.save(new RecommendTrip(trip, member, 1L));

        // when
        tripService.findWithSimilarOtherTrips(trip.getId(), member.getId());
        long expected = tripService.findById(1L).getId();

        // then
        assertThat(expected).isEqualTo(1L);
    }

    @DisplayName("현재 여행지를 조회하면 각 유저에 대한 방문 횟수가 증가한다.")
    @Test
    void 현재_여행지를_조회하면_각_유저에_대한_방문_횟수가_증가한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        tripService.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        tripService.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        tripService.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        tripService.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        Trip trip = tripService.findById(1L);
        recommendTripRepository.save(new RecommendTrip(trip, member, 1L));

        // when
        tripService.findWithSimilarOtherTrips(trip.getId(), member.getId());

        // then
        assertThat(recommendTripRepository.findById(1L).get().getRank()).isEqualTo(1L);
    }

    @DisplayName("동시간대에 여러 유저가 여행지를 조회하면 방문 횟수에 동시성 이슈가 발생한다.")
    @Test
    void 동시간대에_여러_유저가_여행지를_조회하면_방문_횟수에_동시성_이슈가_발생한다() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(100);

        // given
        long currentTripId = 4L;
        tripService.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        tripService.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        tripService.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        tripService.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));

        // when
        for (long memberId = 0; memberId < 100; memberId++) {
            long finalMemberId = memberId;
            executorService.submit(() -> {
                try {
                    tripService.findWithSimilarOtherTrips(currentTripId, finalMemberId);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        // then
        Trip trip = tripService.findById(currentTripId);
        assertThat(trip.getVisitedCount()).isNotEqualTo(104L);
    }

    @DisplayName("존재하지 않는 회원이 여행지를 조회하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_회원이_여행지를_조회하면_예외가_발생한다() {
        // given
        long invalidMemberId = -1L;
        tripService.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        tripService.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        tripService.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        tripService.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        Trip trip = tripService.findById(1L);

        // when, then
        assertThatThrownBy(() -> tripService.findWithSimilarOtherTrips(trip.getId(), invalidMemberId))
                .isInstanceOf(NoExistMemberException.class);
    }

    @DisplayName("존재하지 않는 여행지를 찾으면 예외가 발생한다.")
    @Test
    void 존재하지_않는_여행지를_찾으면_예외가_발생한다() {
        // given
        long invalidTripId = -1L;
        Member member = memberRepository.save(하온_기존());
        tripService.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        tripService.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        tripService.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        tripService.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));

        // when, then
        assertThatThrownBy(() -> tripService.findWithSimilarOtherTrips(invalidTripId, member.getId()))
                .isInstanceOf(NoExistTripException.class);
    }


    @DisplayName("최근 클릭한 여행지가 10개 미만이라면 가장 높은 rank 에 1을 더한 선호 여행지 정보를 생성한다.")
    @Test
    void 최근_클릭한_여행지가_10개_미만이라면_가장_높은_rank_에_1을_더한_선호_여행지_정보를_생성한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        tripService.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        tripService.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        tripService.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        tripService.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        Trip trip1 = tripService.findById(1L);
        Trip trip2 = tripService.findById(2L);
        Trip trip3 = tripService.findById(3L);
        recommendTripRepository.save(new RecommendTrip(trip1, member, 1L));
        recommendTripRepository.save(new RecommendTrip(trip2, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 3L));

        // when
        Trip trip4 = tripService.findById(4L);
        tripService.findWithSimilarOtherTrips(trip4.getId(), member.getId());

        // then
        RecommendTrip actual = recommendTripRepository.findById(trip4.getId()).get();
        assertThat(actual.getRank()).isEqualTo(4L);
    }

    @DisplayName("최근 클릭한 여행지가 10개라면 기존의 rank를 1씩 감소시키고, rank가 10인 새로운 선호 여행지를 생성한다.")
    @Test
    void 최근_클릭한_여행지가_10개라면_기존의_rank를_1씩_감소시키고_rank가_10인_새로운_선호_여행지를_생성한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        tripService.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        tripService.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        tripService.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        tripService.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        tripService.save(new Trip("여행지5", "서울", 5L, "설명5", "https://image.png", 0L));
        tripService.save(new Trip("여행지6", "서울", 6L, "설명6", "https://image.png", 0L));
        tripService.save(new Trip("여행지7", "서울", 7L, "설명7", "https://image.png", 0L));
        tripService.save(new Trip("여행지8", "서울", 8L, "설명8", "https://image.png", 0L));
        tripService.save(new Trip("여행지9", "서울", 9L, "설명9", "https://image.png", 0L));
        tripService.save(new Trip("여행지10", "서울", 10L, "설명10", "https://image.png", 0L));
        Trip trip1 = tripService.findById(1L); Trip trip2 = tripService.findById(2L);
        Trip trip3 = tripService.findById(3L);

        recommendTripRepository.save(new RecommendTrip(trip1, member, 1L)); recommendTripRepository.save(new RecommendTrip(trip2, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip2, member, 3L)); recommendTripRepository.save(new RecommendTrip(trip3, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 5L)); recommendTripRepository.save(new RecommendTrip(trip3, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 7L)); recommendTripRepository.save(new RecommendTrip(trip3, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 9L)); recommendTripRepository.save(new RecommendTrip(trip3, member, 10L));

        // when
        Trip trip = tripService.findById(10L);
        tripService.findWithSimilarOtherTrips(trip.getId(), member.getId());

        // then
        assertAll(() -> {
            assertThat(recommendTripRepository.findById(11L).get().getRank()).isEqualTo(10L);
        });
    }

    @DisplayName("현재 여행지를 조회하고 선호 여행지가 10개라면 랭크가 1 감소한다.")
    @Test
    void 현재_여행지를_조회하고_선호_여행지가_10개라면_랭크가_1_감소한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        tripService.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        tripService.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        tripService.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        tripService.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        tripService.save(new Trip("여행지5", "서울", 5L, "설명5", "https://image.png", 0L));
        tripService.save(new Trip("여행지6", "서울", 6L, "설명6", "https://image.png", 0L));
        tripService.save(new Trip("여행지7", "서울", 7L, "설명7", "https://image.png", 0L));
        tripService.save(new Trip("여행지8", "서울", 8L, "설명8", "https://image.png", 0L));
        tripService.save(new Trip("여행지9", "서울", 9L, "설명9", "https://image.png", 0L));
        tripService.save(new Trip("여행지10", "서울", 10L, "설명10", "https://image.png", 0L));
        Trip trip1 = tripService.findById(1L); Trip trip2 = tripService.findById(2L);
        Trip trip3 = tripService.findById(3L); Trip trip4 = tripService.findById(4L);
        Trip trip5 = tripService.findById(5L); Trip trip6 = tripService.findById(6L);
        Trip trip7 = tripService.findById(7L); Trip trip8 = tripService.findById(8L);
        Trip trip9 = tripService.findById(9L); Trip trip10 = tripService.findById(10L);

        recommendTripRepository.save(new RecommendTrip(trip1, member, 1L)); recommendTripRepository.save(new RecommendTrip(trip2, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 3L)); recommendTripRepository.save(new RecommendTrip(trip4, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 5L)); recommendTripRepository.save(new RecommendTrip(trip6, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip7, member, 7L)); recommendTripRepository.save(new RecommendTrip(trip8, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip9, member, 9L)); recommendTripRepository.save(new RecommendTrip(trip10, member, 10L));

        // when
        Trip trip = tripService.findById(3L);
        tripService.findWithSimilarOtherTrips(trip.getId(), member.getId());

        // then
        assertAll(() -> {
            for(long id=2; id<=11; id++) {
                assertThat(recommendTripRepository.findById(id).get().getRank()).isEqualTo(id-1);
            }
        });
    }

    @DisplayName("현재 여행지를 조회하고 선호 여행지가 10개라면 기존의 랭크가 1등이었던 선호 여행지의 랭크 우선순위가 가장 밀려난다.")
    @Test
    void 현재_여행지를_조회하고_선호_여행지가_10개라면_기존의_랭크가_1등이었던_선호_여행지의_랭크_우선순위가_가장_밀려난다() {
        // given
        Member member = memberRepository.save(하온_기존());
        tripService.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        tripService.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        tripService.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        tripService.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        tripService.save(new Trip("여행지5", "서울", 5L, "설명5", "https://image.png", 0L));
        tripService.save(new Trip("여행지6", "서울", 6L, "설명6", "https://image.png", 0L));
        tripService.save(new Trip("여행지7", "서울", 7L, "설명7", "https://image.png", 0L));
        tripService.save(new Trip("여행지8", "서울", 8L, "설명8", "https://image.png", 0L));
        tripService.save(new Trip("여행지9", "서울", 9L, "설명9", "https://image.png", 0L));
        tripService.save(new Trip("여행지10", "서울", 10L, "설명10", "https://image.png", 0L));
        Trip trip1 = tripService.findById(1L); Trip trip2 = tripService.findById(2L);
        Trip trip3 = tripService.findById(3L); Trip trip4 = tripService.findById(4L);
        Trip trip5 = tripService.findById(5L); Trip trip6 = tripService.findById(6L);
        Trip trip7 = tripService.findById(7L); Trip trip8 = tripService.findById(8L);
        Trip trip9 = tripService.findById(9L); Trip trip10 = tripService.findById(10L);

        recommendTripRepository.save(new RecommendTrip(trip1, member, 1L)); recommendTripRepository.save(new RecommendTrip(trip2, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 3L)); recommendTripRepository.save(new RecommendTrip(trip4, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 5L)); recommendTripRepository.save(new RecommendTrip(trip6, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip7, member, 7L)); recommendTripRepository.save(new RecommendTrip(trip8, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip9, member, 9L)); recommendTripRepository.save(new RecommendTrip(trip10, member, 10L));

        // when
        Trip trip = tripService.findById(10L);
        tripService.findWithSimilarOtherTrips(trip.getId(), member.getId());

        // then
        assertAll(() -> {
            assertThat(recommendTripRepository.findById(1L).get().getRank()).isEqualTo(LOWEST_PRIORITY_RANK);
        });
    }

    @DisplayName("가장 높았던 랭크를 보유한 선호 여행지를 제거한다.")
    @Test
    void 가장_높았던_랭크를_보유한_선호_여행지를_제거한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        tripService.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        tripService.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        tripService.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        tripService.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        tripService.save(new Trip("여행지5", "서울", 5L, "설명5", "https://image.png", 0L));
        tripService.save(new Trip("여행지6", "서울", 6L, "설명6", "https://image.png", 0L));
        tripService.save(new Trip("여행지7", "서울", 7L, "설명7", "https://image.png", 0L));
        tripService.save(new Trip("여행지8", "서울", 8L, "설명8", "https://image.png", 0L));
        tripService.save(new Trip("여행지9", "서울", 9L, "설명9", "https://image.png", 0L));
        tripService.save(new Trip("여행지10", "서울", 10L, "설명10", "https://image.png", 0L));
        Trip trip1 = tripService.findById(1L); Trip trip2 = tripService.findById(2L);
        Trip trip3 = tripService.findById(3L); Trip trip4 = tripService.findById(4L);
        Trip trip5 = tripService.findById(5L); Trip trip6 = tripService.findById(6L);
        Trip trip7 = tripService.findById(7L); Trip trip8 = tripService.findById(8L);
        Trip trip9 = tripService.findById(9L); Trip trip10 = tripService.findById(10L);

        recommendTripRepository.save(new RecommendTrip(trip1, member, 1L)); recommendTripRepository.save(new RecommendTrip(trip2, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 3L)); recommendTripRepository.save(new RecommendTrip(trip4, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 5L)); recommendTripRepository.save(new RecommendTrip(trip6, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip7, member, 7L)); recommendTripRepository.save(new RecommendTrip(trip8, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip9, member, 9L)); recommendTripRepository.save(new RecommendTrip(trip10, member, 10L));

        // when
        Trip trip = tripService.findById(10L);
        tripService.findWithSimilarOtherTrips(trip.getId(), member.getId());

        // then
        assertAll(() -> {
            assertThat(recommendTripRepository.findAllByMemberId(member.getId()).size()).isEqualTo(10);
            assertThat(recommendTripRepository.existsByMemberAndTrip(member, trip1)).isFalse();
            assertThat(recommendTripRepository.findById(2L).get().getRank()).isEqualTo(HIGHEST_PRIORITY_RANK);
        });
    }


    @DisplayName("기존에 rank가 1등인 선호 여행지가 존재하지 않으면 예외가 발생한다.")
    @Test
    void 기존에_rank가_1등인_선호_여행지가_존재하지_않으면_예외가_발생한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        tripService.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        tripService.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        tripService.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        tripService.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        tripService.save(new Trip("여행지5", "서울", 5L, "설명5", "https://image.png", 0L));
        tripService.save(new Trip("여행지6", "서울", 6L, "설명6", "https://image.png", 0L));
        tripService.save(new Trip("여행지7", "서울", 7L, "설명7", "https://image.png", 0L));
        tripService.save(new Trip("여행지8", "서울", 8L, "설명8", "https://image.png", 0L));
        tripService.save(new Trip("여행지9", "서울", 9L, "설명9", "https://image.png", 0L));
        tripService.save(new Trip("여행지10", "서울", 10L, "설명10", "https://image.png", 0L));
        Trip trip1 = tripService.findById(1L); Trip trip2 = tripService.findById(2L);
        Trip trip3 = tripService.findById(3L); Trip trip4 = tripService.findById(4L);
        Trip trip5 = tripService.findById(5L); Trip trip6 = tripService.findById(6L);
        Trip trip7 = tripService.findById(7L); Trip trip8 = tripService.findById(8L);
        Trip trip9 = tripService.findById(9L); Trip trip10 = tripService.findById(10L);

        recommendTripRepository.save(new RecommendTrip(trip1, member, 0L)); recommendTripRepository.save(new RecommendTrip(trip2, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 3L)); recommendTripRepository.save(new RecommendTrip(trip4, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 5L)); recommendTripRepository.save(new RecommendTrip(trip6, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip7, member, 7L)); recommendTripRepository.save(new RecommendTrip(trip8, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip9, member, 9L)); recommendTripRepository.save(new RecommendTrip(trip10, member, 10L));

        // when
        Trip trip = tripService.findById(10L);

        // then
        assertThatThrownBy(() -> tripService.findWithSimilarOtherTrips(trip.getId(), member.getId()))
                .isInstanceOf(NoExistRecommendTripException.class);
    }

    @DisplayName("가장 낮은 우선순위를 가진 랭크가 10인 신규 선호 여행지를 생성한다.")
    @Test
    void 가장_낮은_우선순위를_가진_랭크가_10인_신규_선호_여행지를_생성한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        tripService.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        tripService.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        tripService.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        tripService.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        tripService.save(new Trip("여행지5", "서울", 5L, "설명5", "https://image.png", 0L));
        tripService.save(new Trip("여행지6", "서울", 6L, "설명6", "https://image.png", 0L));
        tripService.save(new Trip("여행지7", "서울", 7L, "설명7", "https://image.png", 0L));
        tripService.save(new Trip("여행지8", "서울", 8L, "설명8", "https://image.png", 0L));
        tripService.save(new Trip("여행지9", "서울", 9L, "설명9", "https://image.png", 0L));
        tripService.save(new Trip("여행지10", "서울", 10L, "설명10", "https://image.png", 0L));
        tripService.save(new Trip("여행지11", "서울", 11L, "설명11", "https://image.png", 0L));
        Trip trip1 = tripService.findById(1L); Trip trip2 = tripService.findById(2L);
        Trip trip3 = tripService.findById(3L); Trip trip4 = tripService.findById(4L);
        Trip trip5 = tripService.findById(5L); Trip trip6 = tripService.findById(6L);
        Trip trip7 = tripService.findById(7L); Trip trip8 = tripService.findById(8L);
        Trip trip9 = tripService.findById(9L); Trip trip10 = tripService.findById(10L);

        recommendTripRepository.save(new RecommendTrip(trip1, member, 1L)); recommendTripRepository.save(new RecommendTrip(trip2, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 3L)); recommendTripRepository.save(new RecommendTrip(trip4, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 5L)); recommendTripRepository.save(new RecommendTrip(trip6, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip7, member, 7L)); recommendTripRepository.save(new RecommendTrip(trip8, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip9, member, 9L)); recommendTripRepository.save(new RecommendTrip(trip10, member, 10L));

        // when
        Trip trip11 = tripService.findById(11L);
        tripService.findWithSimilarOtherTrips(trip11.getId(), member.getId());
        RecommendTrip recommendTrip = recommendTripRepository.findById(11L).get();

        // then
        assertThat(recommendTrip.getRank()).isEqualTo(LOWEST_PRIORITY_RANK);
    }
}

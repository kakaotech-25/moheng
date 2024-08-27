package moheng.planner.application;

import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.exception.NoExistMemberException;
import moheng.planner.domain.TripSchedule;
import moheng.planner.domain.TripScheduleRegistry;
import moheng.planner.domain.TripScheduleRegistryRepository;
import moheng.planner.domain.TripScheduleRepository;
import moheng.planner.dto.CreateTripScheduleRequest;
import moheng.planner.exception.AlreadyExistTripScheduleException;
import moheng.planner.exception.NoExistTripScheduleException;
import moheng.trip.domain.Trip;
import moheng.trip.domain.TripRepository;
import moheng.trip.exception.NoExistTripException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class TripScheduleService {
    private final MemberRepository memberRepository;
    private final TripScheduleRepository tripScheduleRepository;
    private final TripRepository tripRepository;
    private final TripScheduleRegistryRepository tripScheduleRegistryRepository;

    public TripScheduleService(final MemberRepository memberRepository,
                               final TripScheduleRepository tripScheduleRepository,
                               final TripRepository tripRepository,
                               final TripScheduleRegistryRepository tripScheduleRegistryRepository) {
        this.memberRepository = memberRepository;
        this.tripScheduleRepository = tripScheduleRepository;
        this.tripRepository = tripRepository;
        this.tripScheduleRegistryRepository = tripScheduleRegistryRepository;
    }

    @Transactional
    public void createTripSchedule(final long memberId, final CreateTripScheduleRequest createTripScheduleRequest) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoExistMemberException("존재하지 않는 유저입니다."));

        if(tripScheduleRepository.existsByMemberAndName(member, createTripScheduleRequest.getScheduleName())) {
            throw new AlreadyExistTripScheduleException("동일한 이름의 여행 일정이 이미 존재합니다.");
        }

        final TripSchedule tripSchedule = new TripSchedule(
                createTripScheduleRequest.getScheduleName(),
                createTripScheduleRequest.getStartDate(),
                createTripScheduleRequest.getEndDate(),
                member
        );
        tripScheduleRepository.save(tripSchedule);
    }

    @Transactional
    public void addCurrentTripOnPlannerSchedule(final long tripId, final long scheduleId) {
        final Trip trip = tripRepository.findById(tripId)
                .orElseThrow(NoExistTripException::new);

        final TripSchedule tripSchedule = tripScheduleRepository.findById(scheduleId)
                .orElseThrow(NoExistTripScheduleException::new);

        tripScheduleRegistryRepository.save(new TripScheduleRegistry(trip, tripSchedule));
    }

    public List<Trip> findTripsOnSchedule(final long scheduleId) {
        return tripRepository.findTripsByScheduleId(scheduleId);
    }
}

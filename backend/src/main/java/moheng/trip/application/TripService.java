package moheng.trip.application;

import moheng.trip.domain.ExternalSimilarTripModelClient;
import moheng.trip.domain.Trip;
import moheng.trip.dto.FindTripWithSimilarTripsResponse;
import moheng.trip.dto.FindTripsResponse;
import moheng.trip.dto.SimilarTripResponses;
import moheng.trip.dto.TripCreateRequest;
import moheng.trip.exception.NoExistTripException;
import moheng.trip.repository.TripRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class TripService {
    private final TripRepository tripRepository;
    private final ExternalSimilarTripModelClient externalSimilarTripModelClient;

    public TripService(final TripRepository tripRepository, final ExternalSimilarTripModelClient externalSimilarTripModelClient) {
        this.tripRepository = tripRepository;
        this.externalSimilarTripModelClient = externalSimilarTripModelClient;
    }

    public FindTripWithSimilarTripsResponse findWithSimilarOtherTrips(final long tripId) {
        final Trip trip = findById(tripId);
        final SimilarTripResponses similarTripResponses = externalSimilarTripModelClient.findSimilarTrips(tripId);
        return new FindTripWithSimilarTripsResponse(trip, similarTripResponses);
    }

    public Trip findByContentId(final Long contentId) {
        final Trip trip = tripRepository.findByContentId(contentId)
                .orElseThrow(NoExistTripException::new);
        return trip;
    }

    public Trip findById(final Long tripId) {
        final Trip trip = tripRepository.findById(tripId)
                .orElseThrow(NoExistTripException::new);
        return trip;
    }

    @Transactional
    public void createTrip(final TripCreateRequest tripCreateRequest) {
        final Trip trip = new Trip(
                tripCreateRequest.getName(),
                tripCreateRequest.getPlaceName(),
                tripCreateRequest.getContentId(),
                tripCreateRequest.getDescription(),
                tripCreateRequest.getTripImageUrl()
        );
        tripRepository.save(trip);
    }

    @Transactional
    public void save(final Trip trip) {
        tripRepository.save(trip);
    }

    public FindTripsResponse findTop30OrderByVisitedCountDesc() {
        return new FindTripsResponse(tripRepository.findTop30ByOrderByVisitedCountDesc());
    }
}

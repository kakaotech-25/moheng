package moheng.keyword.service;

import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.KeywordRepository;
import moheng.keyword.domain.TripKeyword;
import moheng.keyword.domain.TripKeywordRepository;
import moheng.keyword.dto.KeywordCreateRequest;
import moheng.keyword.dto.RecommendTripResponse;
import moheng.keyword.dto.TripContentIdsByKeywordResponse;
import moheng.keyword.dto.TripsByKeyWordsRequest;
import moheng.keyword.dto.TripRecommendByKeywordRequest;
import moheng.keyword.exception.NoExistKeywordException;
import moheng.recommendtrip.domain.RecommendTripRepository;
import moheng.trip.domain.Trip;
import moheng.trip.dto.FindTripsResponse;
import moheng.trip.dto.TripKeywordCreateRequest;
import moheng.trip.exception.NoExistTripException;
import moheng.trip.domain.TripRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class KeywordService {
    private final KeywordRepository keywordRepository;
    private final TripRepository tripRepository;
    private final TripKeywordRepository tripKeywordRepository;

    public KeywordService(final KeywordRepository keywordRepository,
                          final TripRepository tripRepository,
                          final TripKeywordRepository tripKeywordRepository) {
        this.keywordRepository = keywordRepository;
        this.tripRepository = tripRepository;
        this.tripKeywordRepository = tripKeywordRepository;
    }

    @Transactional
    public void createKeyword(final KeywordCreateRequest request) {
        keywordRepository.save(new Keyword(request.getKeyword()));
    }

    public List<String> findNamesByIds(final TripsByKeyWordsRequest request) {
        return keywordRepository.findNamesByIds(request.getKeywordIds());
    }

    public FindTripsResponse findRecommendTripsByKeywords(final TripsByKeyWordsRequest request) {
        final List<TripKeyword> trips = tripKeywordRepository.findTripKeywordsByKeywordIds(request.getKeywordIds());
        return new FindTripsResponse(trips);
    }

    @Transactional
    public void createTripKeyword(final TripKeywordCreateRequest request) {
        final Trip trip = tripRepository.findById(request.getTripId())
                        .orElseThrow(NoExistTripException::new);
        final Keyword keyword = keywordRepository.findById(request.getKeywordId())
                        .orElseThrow(NoExistKeywordException::new);
        tripKeywordRepository.save(new TripKeyword(trip, keyword));
    }
}

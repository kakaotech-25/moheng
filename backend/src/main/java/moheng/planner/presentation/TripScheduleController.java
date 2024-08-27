package moheng.planner.presentation;

import moheng.auth.dto.Accessor;
import moheng.auth.presentation.authentication.Authentication;
import moheng.planner.application.TripScheduleService;
import moheng.planner.dto.CreateTripScheduleRequest;
import moheng.planner.dto.FindTripsOnSchedule;
import moheng.planner.dto.UpdateTripOrdersRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/schedule")
@RestController
public class TripScheduleController {
    private final TripScheduleService tripScheduleService;

    public TripScheduleController(final TripScheduleService tripScheduleService) {
        this.tripScheduleService = tripScheduleService;
    }

    @PostMapping
    public ResponseEntity<Void> createTripSchedule(@Authentication final Accessor accessor,
                                                   @RequestBody final CreateTripScheduleRequest createTripScheduleRequest) {
        tripScheduleService.createTripSchedule(accessor.getId(), createTripScheduleRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/trip/{tripId}/{scheduleId}")
    public ResponseEntity<Void> addTripOnSchedule(@Authentication final Accessor accessor,
                                                  @PathVariable("tripId") final Long tripId,
                                                  @PathVariable("scheduleId") final Long scheduleId) {
        tripScheduleService.addCurrentTripOnPlannerSchedule(tripId, scheduleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/trips/{scheduleId}")
    public ResponseEntity<FindTripsOnSchedule> findTripsOnSchedule(@Authentication final Accessor accessor,
                                                                   @PathVariable("scheduleId") final Long scheduleId) {
        return ResponseEntity.ok(tripScheduleService.findTripsOnSchedule(scheduleId));
    }

    @PostMapping("/trips/orders/{scheduleId}")
    public ResponseEntity<Void> updateTripOrdersOnSchedule(@Authentication final Accessor accessor,
                                                           @PathVariable("scheduleId") final Long scheduleId,
                                                           @RequestBody UpdateTripOrdersRequest updateTripOrdersRequest) {
        tripScheduleService.updateTripOrdersOnSchedule(scheduleId, updateTripOrdersRequest);
        return ResponseEntity.noContent().build();
    }
}

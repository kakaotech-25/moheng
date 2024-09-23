package moheng.applicationrunner.warmup.member;

import moheng.planner.application.PlannerService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class PlannerWarmUpRunner implements ApplicationRunner {
    private final PlannerService plannerService;

    public PlannerWarmUpRunner(final PlannerService plannerService) {
        this.plannerService = plannerService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            plannerService.findPlannerOrderByRecent(1L);
        } catch (Exception e) {

        }
    }
}
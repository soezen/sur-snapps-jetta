package sur.snapps.jetta.metadata.module;

import org.junit.runner.Description;
import sur.snapps.jetta.core.TestResult;
import sur.snapps.jetta.core.config.JettaConfigurations;
import sur.snapps.jetta.core.logger.JettaLogger;
import sur.snapps.jetta.core.rules.JettaRuleModule;
import sur.snapps.jetta.metadata.annotations.Deprecate;
import sur.snapps.jetta.metadata.annotations.Pending;
import sur.snapps.jetta.metadata.report.MetaDataReportFactory;
import sur.snapps.jetta.metadata.xml.Scenario;
import sur.snapps.jetta.metadata.xml.Status;
import sur.snapps.jetta.metadata.xml.UseCase;

import java.util.Calendar;

/**
 * User: SUR
 * Date: 4/05/14
 * Time: 15:53
 */
public class MetaDataModule extends JettaRuleModule {

    private MetaDataConfiguration configuration = JettaConfigurations.get(MetaDataConfiguration.class);
    private UseCase useCase;
    private Scenario scenario;
    private boolean pending;
    private boolean deprecate;

    private static MetaDataModule instance;

    private MetaDataModule() { }

    public static MetaDataModule getInstance() {
        if (instance == null) {
            instance = new MetaDataModule();
        }
        return instance;
    }

    public MetaDataConfiguration configuration() {
        return configuration;
    }

    @Override
    public boolean init(Object target, Description description) {
        JettaLogger.info(this.getClass(), "ACTIVATED");
        pending = false;
        deprecate = false;

        useCase = getUseCase(target);

        if (useCase != null) {
            scenario = initializeScenario(description);

            if (canRunTest(description)) {
                logTestRun();
                return true;
            }
            logTestSkip();
            return false;
        }
        return true;
    }

    @Override
    public void quit(TestResult result) {
        if (useCase != null && !result.testSkipped()) {
            logTestFinish(result);
        }
    }

    private void logTestFinish(TestResult result) {
        if (scenario == null) {
            return;
        }
        if (result.testSucceeded()) {
            scenario.setStatus(Status.SUCCESS);
        } else if (result.testFailed()) {
            scenario.setStatus(Status.FAILURE);
            scenario.setFailureCause(result.failureCause());
        }
        scenario.setDuration(result.duration());
    }

    private void logTestSkip() {
        if (scenario == null) {
            return;
        }

        scenario.setStatus(pending ? Status.PENDING : (deprecate ? Status.DEPRECATE : Status.SKIPPED));
    }

    private void logTestRun() {
        if (scenario == null) {
            return;
        }
        scenario.setExecutionTime(Calendar.getInstance());
    }

    private Scenario initializeScenario(Description description) {
        Scenario xmlScenario = null;
        sur.snapps.jetta.metadata.annotations.Scenario scenarioAnnotation = description.getAnnotation(sur.snapps.jetta.metadata.annotations.Scenario.class);
        if (scenarioAnnotation != null) {
            xmlScenario = MetaDataReportFactory.createScenario(description.getMethodName(), null, scenarioAnnotation.type(), scenarioAnnotation.failureImpact());
            useCase.getScenarios().add(xmlScenario);

            if (description.getAnnotation(Pending.class) != null) {
                pending = true;
            } else if (description.getAnnotation(Deprecate.class) != null) {
                deprecate = true;
            }
        }
        return xmlScenario;
    }

    private UseCase getUseCase(Object target) {
        Class<?> targetClass = target.getClass();
        sur.snapps.jetta.metadata.annotations.UseCase useCaseAnnotation = targetClass.isAnnotationPresent(sur.snapps.jetta.metadata.annotations.UseCase.class)
                ? targetClass.getAnnotation(sur.snapps.jetta.metadata.annotations.UseCase.class)
                : null;
        if (useCaseAnnotation != null) {
            // TODO get description of use case from somewhere
            return MetaDataReportFactory.getUseCase(useCaseAnnotation.value(), null);
        }
        return null;
    }

    private boolean canRunTest(Description description) {
        return description.getAnnotation(sur.snapps.jetta.metadata.annotations.Scenario.class) == null
                || (!pending && !deprecate);
    }
}

package sur.snapps.jetta.metadata.report;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import sur.snapps.jetta.metadata.xml.FailureImpact;
import sur.snapps.jetta.metadata.xml.JettaMetaDataReport;
import sur.snapps.jetta.metadata.xml.ObjectFactory;
import sur.snapps.jetta.metadata.xml.Scenario;
import sur.snapps.jetta.metadata.xml.ScenarioType;
import sur.snapps.jetta.metadata.xml.UseCase;

/**
 * User: SUR
 * Date: 24/08/14
 * Time: 10:37
 */
public class MetaDataReportFactory {


    private static final ObjectFactory FACTORY = new ObjectFactory();
    private static JettaMetaDataReport report = FACTORY.createJettaMetaDataReport();

    private MetaDataReportFactory() {}

    public static UseCase getUseCase(String name, String description) {
        UseCase existingUseCase = getExistingUseCase(name);
        return existingUseCase != null
                ? existingUseCase
                : createUseCase(name, description);
    }

    public static Scenario createScenario(String name, String description, ScenarioType type, FailureImpact failureImpact) {
        Scenario scenario = FACTORY.createScenario();
        // TODO allow to overwrite name with scenario annotation?
        scenario.setName(name);
        // TODO get description from somewhere
        scenario.setDescription(description);
        scenario.setFailureImpact(failureImpact);
        // TODO set properties of the scenario
        scenario.setType(type);
        return scenario;
    }

    private static UseCase getExistingUseCase(final String name) {
        return FluentIterable.from(report.getUseCase())
                .firstMatch(new Predicate<UseCase>() {
                    @Override
                    public boolean apply(UseCase input) {
                        return input.getName().equals(name);
                    }
                }).orNull();
    }

    private static UseCase createUseCase(String name, String description) {
        UseCase useCase = FACTORY.createUseCase();
        useCase.setName(name);
        useCase.setDescription(description);
        report.getUseCase().add(useCase);
        return useCase;
    }

    public static JettaMetaDataReport getReport() {
        return report;
    }

    // TODO confirm that configuration files only get loaded once (for all tests)
}

package sur.snapps.jetta.metadata;

import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import sur.snapps.jetta.metadata.module.MetaataConfiguration;
import sur.snapps.jetta.metadata.module.MetaDataModule;
import sur.snapps.jetta.metadata.report.MetaDataReportFactory;
import sur.snapps.jetta.metadata.xml.JettaMetaDataReport;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

/**
 * User: SUR
 * Date: 25/08/14
 * Time: 19:02
 */
public class MetaDataRunListener extends RunListener {

    private MetaataConfiguration configuration = MetaDataModule.getInstance().configuration();

    @Override
    public void testRunFinished(Result result) throws Exception {

        // TODO put this in factory object
        // TODO if you do, rename class
        try {
            JAXBContext context = JAXBContext.newInstance(JettaMetaDataReport.class);
            Marshaller m = context.createMarshaller();
            //for pretty-print XML in JAXB
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            File directory = new File(configuration.directory());
            boolean ok = directory.exists();
            if (!ok) {
                ok = directory.mkdirs();
            }
            if (ok) {
                File reportFile = new File(directory, configuration.filename());
                m.marshal(MetaDataReportFactory.getReport(), reportFile);
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }
}

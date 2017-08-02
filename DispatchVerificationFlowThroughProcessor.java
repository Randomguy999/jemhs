package com.verizon.delphi.hyperion.core.jms;

import com.verizon.delphi.hyperion.core.ejb.client.CircuitManagerLocal;
import com.verizon.delphi.hyperion.core.ejb.client.FlowCalcLocal;
import com.verizon.delphi.hyperion.core.ejb.client.HyperionClient;
import com.verizon.delphi.hyperion.core.network.measurements.Measurement;
import com.verizon.delphi.hyperion.core.network.measurements.ReadingGroup;
import com.verizon.delphi.hyperion.core.network.model.Circuit;
import com.verizon.delphi.hyperion.core.network.model.NetworkDesign;
import com.verizon.delphi.hyperion.core.network.signal.Diagnosis;
import com.verizon.delphi.hyperion.core.network.signal.DispatchVerificationDiagnosis;
import com.verizon.delphi.hyperion.core.network.signal.ProbeDiagnosis;
import com.verizon.delphi.hyperion.core.network.signal.ProbeRemarks;
import com.verizon.delphi.hyperion.core.network.signal.flow.AtomicInterFlow;
import com.verizon.delphi.hyperion.core.network.signal.flow.IntraNodeFlow;
import com.verizon.delphi.hyperion.core.network.signal.flow.SignalFlow;
import com.verizon.delphi.hyperion.core.network.signal.flow.critiques.Critique;
import com.verizon.delphi.hyperion.dip.ComparisonSourceInfo;
import com.verizon.delphi.hyperion.dip.ValidationCheck;
import com.verizon.delphi.hyperion.dip.ValidationInfo;
import com.verizon.delphi.hyperion.dip.ValidationResultType;
import com.verizon.delphi.hyperion.util.StringUtil;
import java.util.ArrayList;
import java.util.List;
import javax.jms.Connection;
import javax.jms.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author @author Swagatika
 */
public class DispatchVerificationFlowThroughProcessor extends CircuitFlowThrowProcessor {

    private static final transient Logger logger = LoggerFactory.getLogger(DispatchVerificationFlowThroughProcessor.class);

    NetworkDesign design = null;
    FlowCalcLocal fc = null;
    CircuitManagerLocal cm = null;
    List<Diagnosis> diagnosisList = null;

    String shortSummary = "";
    String longSummary = "";
    String sumCD = "";

    StringBuilder remarks = new StringBuilder();

    public DispatchVerificationFlowThroughProcessor(HyperionClient ejbClient, Connection connection, Queue queue, FlowthroughRequest request) {
        super(ejbClient, connection, queue, request);
    }

    @Override
    ProbeResponse process(NetworkDesign design, List<Diagnosis> diagnosisList) throws Exception {

        this.design = design;
        fc = getFlowCalcLocal();
        cm = getCircuitManagerLocal();
        SignalFlow sf = null;

        try {  
            this.diagnosisList = diagnosisList;
            fc.init(request.getSvcId(), design);
             sf = fc.getFlow();

            ProbeDiagnosis d = new ProbeDiagnosis(new ArrayList<ReadingGroup>(), new ArrayList<Measurement>(), "Operation Completed", 
                    "DISPATCH VERIFICATION", design);

            // Design Critique Validation
            d.checkConsecutiveSTSCritique(sf);

            d.compose();
            diagnosisList.add(d);
            return new ProbeResponse(diagnosisList, request, null);
        } catch (Exception ex) {
            logger.error("Exception occured in DispatchVerificationFlowThroughProcessor", ex);
            sumCD = "RVW";
            shortSummary = "Failed to run APC Design Validation due to system issues";
            longSummary = "Failed to run APC Design Validation due to system issues";
            diagnosisList.add(generateDiagnosis());
            diagnosisList.add(generateDispatchVerificationDiagnosis());
            return new ProbeResponse(diagnosisList, request, null);
        }
    }

    public Diagnosis generateDiagnosis() {

        Diagnosis d = new Diagnosis(new ArrayList<ReadingGroup>(), new ArrayList<Measurement>(), "Operation Completed", "DISPATCH VERIFICATION");

        ProbeRemarks pRemarks = new ProbeRemarks();
        pRemarks.setAnalysisType(ProbeRemarks.AnalysisType.DEFAULT);
        longSummary = (StringUtil.isEmpty(longSummary)) ? shortSummary : longSummary;
        pRemarks.setLongSummary(longSummary);
        pRemarks.setShortSummary(shortSummary);
        pRemarks.setSummaryCode("RVW");
        pRemarks.setDetails(remarks.toString());

        d.addpRemark(pRemarks);

        return d;

    }

    public ProbeDiagnosis generateDispatchVerificationDiagnosis() {

        ProbeDiagnosis d = new ProbeDiagnosis(new ArrayList<ReadingGroup>(), new ArrayList<Measurement>(), "Operation Completed", 
                                                "DISPATCH VERIFICATION",design);

        ValidationInfo validationInfo = new ValidationInfo();
        ComparisonSourceInfo compInfo = new ComparisonSourceInfo();
        validationInfo.setVALIDATIONCATEGORY("CFA_VALIDATION");
        validationInfo.setVALIDATIONCHECK(ValidationCheck.CONSECUTIVE_STS);
        compInfo.setINFOSOURCE("TIRKS");
        validationInfo.setCOMPARISONSOURCE(compInfo);
        validationInfo.setTARGETCLLI("US");
        validationInfo.setRESULTTYPE(ValidationResultType.RVW);
        validationInfo.setSHORTDESC("Failed to run APC Design Validation due to system issues");
        validationInfo.setLONGDESC("Failed to run APC Design Validation due to system issues");

        d.addValidationInfo(validationInfo);

        return d;

    }
   
}

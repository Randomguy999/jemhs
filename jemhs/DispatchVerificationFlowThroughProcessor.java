package com.verizon.delphi.hyperion.core.jms;

import com.verizon.delphi.hyperion.core.ejb.client.CircuitManagerLocal;
import com.verizon.delphi.hyperion.core.ejb.client.FlowCalcLocal;
import com.verizon.delphi.hyperion.core.ejb.client.HyperionClient;
import com.verizon.delphi.hyperion.core.network.measurements.Measurement;
import com.verizon.delphi.hyperion.core.network.measurements.ReadingGroup;
import com.verizon.delphi.hyperion.core.network.model.Channel;
import com.verizon.delphi.hyperion.core.network.model.Circuit;
import com.verizon.delphi.hyperion.core.network.model.InterOfficeFacility;
import com.verizon.delphi.hyperion.core.network.model.NetworkDesign;
import com.verizon.delphi.hyperion.core.network.model.NetworkElement;
import com.verizon.delphi.hyperion.core.network.model.NetworkInterface;
import com.verizon.delphi.hyperion.core.network.model.Node;
import com.verizon.delphi.hyperion.core.network.signal.Diagnosis;
import com.verizon.delphi.hyperion.core.network.signal.DispatchVerificationDiagnosis;
import com.verizon.delphi.hyperion.core.network.signal.ProbeDiagnosis;
import com.verizon.delphi.hyperion.core.network.signal.ProbeRemarks;
import com.verizon.delphi.hyperion.core.network.signal.flow.AtomicInterFlow;
import com.verizon.delphi.hyperion.core.network.signal.flow.InterFlow;
import com.verizon.delphi.hyperion.core.network.signal.flow.IntraNodeFlow;
import com.verizon.delphi.hyperion.core.network.signal.flow.SerialFlow;
import com.verizon.delphi.hyperion.core.network.signal.flow.SignalFlow;
import com.verizon.delphi.hyperion.core.network.signal.flow.critiques.Critique;
import com.verizon.delphi.hyperion.dip.ComparisonSourceInfo;
import com.verizon.delphi.hyperion.dip.ValidationCheck;
import com.verizon.delphi.hyperion.dip.ValidationInfo;
import com.verizon.delphi.hyperion.dip.ValidationResultType;
import com.verizon.delphi.hyperion.util.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.jms.Connection;
import javax.jms.Queue;
import org.apache.commons.lang.StringUtils;
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
    
    List<String> faultsList = new ArrayList<String>();
    List<String> notesList = new ArrayList<String>();
    List<String> verifiedList = new ArrayList<String>();

    boolean AendPOI = false;
    boolean ZendPOI = false;

    List<Integer> missingChannelList = null;

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
                    "DISPATCH VERIFICATION");
            
            // Consecutive STS Critique Check
            checkConsecutiveSTSCritique(sf);
            
            d.init(faultsList, notesList, verifiedList, sumCD, shortSummary, longSummary);
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
    
    private void setSumCD(String newSumCD, String newShortSummary, String newLongSummary) {
           sumCD = newSumCD;
           shortSummary =newShortSummary;
           longSummary = newLongSummary;
    }

    public void checkConsecutiveSTSCritique(SignalFlow sf) {

        Circuit ckt = (Circuit) design;
        List<Critique> critiqueList = ckt.getCritique().getItems();
        boolean isConsecutive = false;
        try {
            if (null != critiqueList && critiqueList.size() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("Check  Consecutive STSs");
                for (Critique c : critiqueList) {
                    if (c.getCode().equals("7541")) {
                        sb.append("Check  Consecutive STSs");
                        sb.append(StringUtil.LINE_SEPARATOR);
                        sb.append("    ");
                        sb.append(c.getDescription());
                        sb.append("for design ");
                        sb.append(ckt.getId());
                        checkEndPointsForPOI(sf, isConsecutive);
                        faultsList.add(sb.toString());
                    } else {
                        checkEndPointsForPOI(sf, true);
                        if (AendPOI || ZendPOI) {
                            setSumCD("TOK", "Consecutive STS", "Consecutive STS Found");
                        }
                    }
                }
                if (AendPOI || ZendPOI) {
                    setSumCD("TOK", "Non-Consecutive STS", "Non-Consecutive STS Found");
                }
            }
        } catch (Exception e) {

        }
    }

    public void checkEndPointsForPOI(SignalFlow sf, boolean isConsecutive) {

        Map<String, String> validationMap = new HashMap<String, String>();
        try {
            if (sf instanceof SerialFlow) {
                SerialFlow sef = (SerialFlow) sf;
                Node leftNode = (Node) sef.getLeft().getRoot();
                IntraNodeFlow leftIntraNode = (IntraNodeFlow) sef.getLeft();
                IntraNodeFlow rightIntraNode = (IntraNodeFlow) sef.getRight();
                Node rightNode = (Node) sef.getRight().getRoot();

                if ("POI".equals(rightNode.getRole()) || "POT".equals(rightNode.getRole())) {
                    ZendPOI = true;
                    Set<InterFlow> allFlows = rightIntraNode.getLeftFlows();
                    validationMap = getValidationMap(allFlows, rightNode, true);
                    if (isConsecutive) {
                        String[] channels = validationMap.get("CHANNELS").split(",");
                        generateValidation("Consecutive STS", "Consecutive STS channels from " + channels[0] + " to "
                                + channels[channels.length - 1] + " at handoff (" + rightNode.getTid() + ")", ValidationResultType.TOK, ValidationCheck.CONSECUTIVE_STS, validationMap);
                        verifiedList.add("Verified Consecutive STS");
                    } else {
                        nonConsecutiveValidation(validationMap, rightNode);
                    }

                } else if ("POI".equals(leftNode.getRole()) || "POT".equals(leftNode.getRole())) {
                    AendPOI = true;
                    Set<InterFlow> allFlows = leftIntraNode.getRightFlows();
                    validationMap = getValidationMap(allFlows, leftNode, false);

                    if (isConsecutive) {
                        String[] channels = validationMap.get("CHANNELS").split(",");
                        generateValidation("Consecutive STS", "Consecutive STS channels from " + channels[0] + " to "
                                + channels[channels.length - 1] + " at handoff (" + leftNode.getTid() + ")", ValidationResultType.TOK, ValidationCheck.CONSECUTIVE_STS, validationMap);
                        verifiedList.add("Verified Consecutive STS");
                    } else {
                        nonConsecutiveValidation(validationMap, leftNode);
                    }
                }

            }
        } catch (Exception ex) {
            logger.error("Exception in checking the endpoints of the circuit" + ex);
        }
    }

    public Map<String, String> getValidationMap(Set<InterFlow> allFlows, Node node, boolean ZendPOI) {

        Map<String, String> validationMap = new HashMap<String, String>();
        NetworkElement ne = null;
        AtomicInterFlow aif = null;
        NetworkInterface ni = null;
        try {
            for (InterFlow flow : allFlows) {

                if (flow instanceof AtomicInterFlow) {
                    aif = (AtomicInterFlow) flow;
                    if (ZendPOI) {
                        ne = aif.getHop().getLeft();
                    } else {
                        ne = aif.getHop().getRight();
                    }
                    ni = (NetworkInterface) ne;
                    if ("Main".equals(aif.getPrivateDesc()) && aif.getHop().getLink() instanceof InterOfficeFacility) {
                        Set<Channel> channels = ni.getChannels();
                        StringBuilder sb = new StringBuilder();
                        int i = 1;
                        for (Channel channel : channels) {
                            sb.append(channel.getId());
                            if (channels.size() - i > 0) {
                                sb.append(StringUtil.COMMA);
                                i++;
                            }
                        }
                        validationMap.put("AID", ni.getPort().getAID());
                        validationMap.put("TID", node.getTid());
                        validationMap.put("PORT", ni.getPort().getId());
                        validationMap.put("EQPTTYPE", ni.getPort().getSrc());
                        validationMap.put("CHANNELS", sb.toString());
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("Exception in getting the validation map" + ex);
        }
        return validationMap;
    }

    public void generateValidation(String shortDes, String longDesc, ValidationResultType resultType, ValidationCheck validationType,
            Map<String, String> validationMap) {
        
        ValidationInfo validationInfo = new ValidationInfo();
        ComparisonSourceInfo compSourceInfo = new ComparisonSourceInfo();

        DispatchVerificationDiagnosis d = new DispatchVerificationDiagnosis(new ArrayList<ReadingGroup>(), new ArrayList<Measurement>(), "Operation Completed",
                    "DISPATCH VERIFICATION");
        logger.info("Generating Validation ...");
        try {
            validationInfo.setSHORTDESC(shortDes);
            validationInfo.setLONGDESC(longDesc);
            if (resultType == ValidationResultType.ERR) {
                validationInfo.setRESULTTYPE(ValidationResultType.ERR);
            } else if (resultType == ValidationResultType.TOK) {
                validationInfo.setRESULTTYPE(ValidationResultType.TOK);
            } else {
                validationInfo.setRESULTTYPE(ValidationResultType.RVW);
            }

            validationInfo.setVALIDATIONCATEGORY("CFA_VALIDATION");
            if (validationType == ValidationCheck.CONSECUTIVE_STS) {
                validationInfo.setVALIDATIONCHECK(ValidationCheck.CONSECUTIVE_STS);
            }
            compSourceInfo.setINFOSOURCE("TIRKS");
            if (null != validationMap) {
                compSourceInfo.setAID(validationMap.get("AID"));
                compSourceInfo.setCHANNELS(validationMap.get("CHANNELS"));
                compSourceInfo.setEQPTTYPE(validationMap.get("EQPTTYPE"));
                compSourceInfo.setPORT(validationMap.get("PORT"));
                compSourceInfo.setTID(validationMap.get("TID"));
            }
            validationInfo.setCOMPARISONSOURCE(compSourceInfo);
            validationInfo.setTARGETCLLI("US");

            d.addValidationInfo(validationInfo);
        } catch (Exception e) {

        }
    }

    private int getMinChannel(int arr[]) {
        int min = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        return min;
    }

    private int getMaxChannel(int arr[]) {
        int max = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    private List<Integer> findMissingChannels(int arr[]) {
        missingChannelList = new ArrayList<Integer>();
        try {
            for (int i = 1; i < arr.length; i++) {
                for (int j = 1 + arr[i - 1]; j < arr[i]; j++) {
                    missingChannelList.add(j);
                }
            }
        } catch (Exception ex) {
            logger.error("Exception in finding the missing channels" + ex);
        }

        return missingChannelList;
    }

    private void nonConsecutiveValidation(Map<String, String> validationMap, Node node) {

        String[] channels = validationMap.get("CHANNELS").split(",");
        int[] channel = new int[channels.length];
        for (int i = 0; i < channels.length; i++) {
            try {
                channel[i] = Integer.parseInt(channels[i]);
            } catch (NumberFormatException nfe) {
                logger.error("Error in getting channels " + nfe);
            }
        }
        missingChannelList = findMissingChannels(channel);

        StringBuilder sb = new StringBuilder();
        
            sb.append(channel.length);
            sb.append(" STS channels");
            sb.append(" from ");
            sb.append(getMinChannel(channel));
            sb.append(" to ");
            sb.append(getMaxChannel(channel));
        
        if (channels.length > 10) {
            sb.append(missingChannelList.size() > 4 ? " with several gaps including " : " with gaps ");
            sb.append(StringUtils.join(missingChannelList.subList(0, 4).toArray(), ","));
            sb.append(missingChannelList.size() > 4 ? " and more" : "");
        } 
        else {
            sb.append(validationMap.get("CHANNELS"));
        }
        sb.append(" found at handoff (");
        sb.append(node.getTid());
        sb.append(")");
        //         getFaultsList().add("Check  Consecutive STS "+StringUtil.LINE_SEPARATOR+sb.toString());
        generateValidation("Non-Consecutive STS", sb.toString(), ValidationResultType.ERR, ValidationCheck.CONSECUTIVE_STS, validationMap);

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

  //      ProbeDiagnosis d = new ProbeDiagnosis(new ArrayList<ReadingGroup>(), new ArrayList<Measurement>(), "Operation Completed",
   //             "DISPATCH VERIFICATION");

        DispatchVerificationDiagnosis d = new DispatchVerificationDiagnosis(new ArrayList<ReadingGroup>(), new ArrayList<Measurement>(), "Operation Completed",
                    "DISPATCH VERIFICATION");
        
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

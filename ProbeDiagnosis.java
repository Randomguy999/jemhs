package com.verizon.delphi.hyperion.core.network.signal;

import com.verizon.delphi.hyperion.core.network.measurements.Measurement;
import com.verizon.delphi.hyperion.core.network.measurements.ReadingGroup;
import com.verizon.delphi.hyperion.core.network.model.CarrierRoute;
import com.verizon.delphi.hyperion.core.network.model.Channel;
import com.verizon.delphi.hyperion.core.network.model.Circuit;
import com.verizon.delphi.hyperion.core.network.model.InterOfficeFacility;
import com.verizon.delphi.hyperion.core.network.model.NetworkDesign;
import com.verizon.delphi.hyperion.core.network.model.NetworkElement;
import com.verizon.delphi.hyperion.core.network.model.NetworkInterface;
import com.verizon.delphi.hyperion.core.network.model.NidValidation;
import com.verizon.delphi.hyperion.core.network.model.Node;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author KADIYVE
 */
public class ProbeDiagnosis extends Diagnosis {

    private static final transient Logger logger = LoggerFactory.getLogger(ProbeDiagnosis.class);

    private List<ValidationInfo> verificationList;
    NetworkDesign design = null;
    private List<String> faultsList;
    private List<String> notesList;
    private List<String> verifiedList;
    private List<String> summaryList;
    private List<String> detailsList;

    private String sumCD;
    private String shortSummary;
    private String longSummary;
    private String details;

    boolean AendPOI = false;
    boolean ZendPOI = false;

    List<Integer> missingChannelList = null;

    public ProbeDiagnosis(List<ReadingGroup> readingGroups, List<Measurement> measurements, String summary, String probeId, NetworkDesign design) {
        super(readingGroups, measurements, summary, probeId);
        this.verificationList = new LinkedList<ValidationInfo>();
        this.faultsList = new LinkedList<String>();
        this.notesList = new LinkedList<String>();
        this.verifiedList = new LinkedList<String>();
        this.summaryList = new LinkedList<String>();
        this.detailsList = new LinkedList<String>();
        this.design = design;
    }

    public List<ValidationInfo> getVerificationList() {
        return verificationList;
    }

    /*   public void setVerificationList(List<ValidationInfo> verificationList) {
     this.verificationList = verificationList;
     } */
    public ValidationInfo getValidationInfo() {
        if (verificationList.isEmpty()) {
            return null;
        }
        return verificationList.get(0);
    }

    public void addValidationInfo(ValidationInfo rawVerification) {
        if (verificationList == null) {
            verificationList = new LinkedList<ValidationInfo>();
        }
        this.verificationList.add(rawVerification);
    }

    public List<String> getFaultsList() {
        if (faultsList == null) {
            faultsList = new LinkedList<String>();
        }
        return faultsList;
    }

    public List<String> getVerifiedList() {
        if (verifiedList == null) {
            verifiedList = new LinkedList<String>();
        }
        return verifiedList;
    }

    public void compose() {
        StringBuilder remarks = new StringBuilder();
        try {
            composeHeader(remarks);
            composeFaults(faultsList, remarks);
            if (notesList.size() > 0) {
                composeNotes(notesList, remarks);
            }
            composeVerified(verifiedList, remarks);
            if (summaryList.size() > 0) {
                composeSummary(summaryList, remarks);
            }
            if (detailsList.size() > 0) {
                composeDetails(detailsList, remarks);
            }
            composeFooterSection(remarks);
            ProbeRemarks pr = new ProbeRemarks();
            pr.setSummaryCode(sumCD);
            pr.setShortSummary(shortSummary);
            pr.setLongSummary(longSummary);
            pr.setDetails(remarks.toString());
            addpRemark(pr);
        } catch (Exception ex) {
            logger.error("Exception occured in composing the sections", ex);
        }

    }

    private void setSumCD(String newSumCD, String newShortSummary, String newLongSummary) {
        this.sumCD = newSumCD;
        this.shortSummary = newShortSummary;
        this.longSummary = newLongSummary;

    }

    public void composeHeader(StringBuilder remarks) {

        logger.info("Generating Header section");
        StringBuilder hSection = new StringBuilder();
        hSection.append(StringUtil.LINE_SEPARATOR);
        StringUtil.createSeparatorLine(hSection, 78, '*', '*', '*', " DETAILS ");
        hSection.append(StringUtil.LINE_SEPARATOR);
        hSection.append(StringUtil.LINE_SEPARATOR);

        hSection.append("TEST START TIME :");
        hSection.append("");
        hSection.append("         ");

        hSection.append("TEST FINISH TIME :");
        hSection.append("");
        hSection.append(StringUtil.LINE_SEPARATOR);

        hSection.append("SPCL SERVICE TYPE :");
        hSection.append("");
        hSection.append("       ");

        hSection.append("ACCESS TYPE :");
        hSection.append("");
        hSection.append(StringUtil.LINE_SEPARATOR);

        hSection.append("SYMPTOM CODE :");
        hSection.append("");
        hSection.append("            ");

        hSection.append("PRIORITY  :");
        hSection.append("");
        hSection.append(StringUtil.LINE_SEPARATOR);

        hSection.append("PRIMARY NE ID :");
        hSection.append(design.getId());
        hSection.append(StringUtil.LINE_SEPARATOR);

        remarks.append(hSection);

    }

    protected void composeFaults(List<String> faultsList, StringBuilder remarks) {
        int i = 1;
        logger.info("Generating Faults section");
        remarks.append(StringUtil.LINE_SEPARATOR);
        StringUtil.createSeparatorLine(remarks, 78, '*', '*', '*', " FAULTS ");
        remarks.append(StringUtil.LINE_SEPARATOR);
        if (faultsList != null) {
            for (String fault : faultsList) {
                remarks.append("(").append(i).append(") ");
                remarks.append(fault);
                if (faultsList.size() - i > 0) {
                    remarks.append(StringUtil.LINE_SEPARATOR);
                    i++;
                }
            }
        }
    }

    protected void composeNotes(List<String> notesList, StringBuilder remarks) {
        int i = 1;
        logger.info("Generating Notes section");
        remarks.append(StringUtil.LINE_SEPARATOR);
        StringUtil.createSeparatorLine(remarks, 78, '*', '*', '*', " NOTES ");
        remarks.append(StringUtil.LINE_SEPARATOR);
        if (notesList != null) {
            for (String note : notesList) {
                remarks.append("(").append(i).append(") ");
                remarks.append(note);
                if (notesList.size() - i > 0) {
                    remarks.append(StringUtil.LINE_SEPARATOR);
                    i++;
                }
            }
        }
    }

    protected void composeVerified(List<String> verifiedList, StringBuilder remarks) {
        int i = 1;
        logger.info("Generating Verified section");
        remarks.append(StringUtil.LINE_SEPARATOR);
        StringUtil.createSeparatorLine(remarks, 78, '*', '*', '*', " VERIFIED ");
        remarks.append(StringUtil.LINE_SEPARATOR);
        if (verifiedList != null) {
            for (String verified : verifiedList) {
                remarks.append("(").append(i).append(") ");
                remarks.append(verified);
                if (verifiedList.size() - i > 0) {
                    remarks.append(StringUtil.LINE_SEPARATOR);
                    i++;
                }
            }
        }
    }

    protected void composeSummary(List<String> summaryList, StringBuilder remarks) {
        int i = 1;
        logger.info("Generating Summary section");
        remarks.append(StringUtil.LINE_SEPARATOR);
        StringUtil.createSeparatorLine(remarks, 78, '*', '*', '*', " SUMMARY ");
        remarks.append(StringUtil.LINE_SEPARATOR);
        /*      if (verifiedList != null) {
         for (String verified : verifiedList) {
         remarks.append("(").append(++count).append(") ");
         remarks.append(verified);
         if (verifiedList.size() - i > 0) {
         remarks.append(StringUtil.LINE_SEPARATOR);
         i++;
         }
         }
         }*/
    }

    protected void composeDetails(List<String> detailsList, StringBuilder remarks) {
        int i = 1;
        logger.info("Generating Details section");
        remarks.append(StringUtil.LINE_SEPARATOR);
        StringUtil.createSeparatorLine(remarks, 78, '*', '*', '*', " DETAILS ");
        remarks.append(StringUtil.LINE_SEPARATOR);
        /*      if (verifiedList != null) {
         for (String verified : verifiedList) {
         remarks.append("(").append(++count).append(") ");
         remarks.append(verified);
         if (verifiedList.size() - i > 0) {
         remarks.append(StringUtil.LINE_SEPARATOR);
         i++;
         }
         }
         }*/
    }

    public void composeFooterSection(final StringBuilder remarks) {
        StringBuilder hSection = new StringBuilder();
        hSection.append(StringUtil.LINE_SEPARATOR);
        StringUtil.createSeparatorLine(hSection, 78, '*', '*', '*', " DETAILS ");
        hSection.append(StringUtil.LINE_SEPARATOR);
        remarks.append(hSection);
    }

    public void generateValidation(String shortDes, String longDesc, ValidationResultType resultType, ValidationCheck validationType,
            Map<String, String> validationMap) {
        ValidationInfo validationInfo = new ValidationInfo();
        ComparisonSourceInfo compSourceInfo = new ComparisonSourceInfo();

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

            addValidationInfo(validationInfo);
        } catch (Exception e) {

        }
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
                    sb.append(StringUtil.LINE_SEPARATOR);
                    sb.append("    ");
                    sb.append(c.getDescription());
                    sb.append("for design ");
                    sb.append(ckt.getId());
                    if (c.getCode().equals("7541")) {
                        checkEndPointsForPOI(sf, isConsecutive);
                        getFaultsList().add(sb.toString());
                    }
//          generateValidation("Non-Consecutive STS", c.getDescription(), ValidationResultType.ERR,
                    //                 ValidationCheck.CONSECUTIVE_STS, null);
                }
                if (AendPOI || ZendPOI) {
                    setSumCD("TOK", "Non-Consecutive STS", "Non-Consecutive STS Found");
                }
            } else {
                checkEndPointsForPOI(sf, true);
                if (AendPOI || ZendPOI) {
                    setSumCD("TOK", "Consecutive STS", "Consecutive STS Found");
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
                        getVerifiedList().add("Verified Consecutive STS");
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
                        getVerifiedList().add("Verified Consecutive STS");
                    } else {
                        nonConsecutiveValidation(validationMap, leftNode);
                    }
                }

            }
        } catch (Exception ex) {
        logger.error("Exception in checking the endpoints of the circuit"+ex);
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
         logger.error("Exception in getting the validation map"+ex);
        }
        return validationMap;
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
        logger.error("Exception in finding the missing channels"+ex);
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
        if (channels.length > 10) {
            sb.append(channel.length);
            sb.append(" STS channels");
            sb.append(" from ");
            sb.append(getMinChannel(channel));
            sb.append(" to ");
            sb.append(getMaxChannel(channel));
            sb.append(missingChannelList.size() > 4 ? " with several gaps including " : " with gaps ");
//                    List<Integer>  missingChannel = missingChannelList.subList(0, 4);
//                    int i=1;
//                    for (Integer channl : missingChannel) {
//                        sb.append(channl);
//                        if (missingChannel.size() - i > 0) {
//                            sb.append(StringUtil.COMMA);
//                            i++;
//                        }
//                    } 

            sb.append(StringUtils.join(missingChannelList.subList(0, 4).toArray(), ","));
            sb.append(missingChannelList.size() > 4 ? " and more" : "");
        } else {
            sb.append("Non-Consecutive STS channels ");
            sb.append(validationMap.get("CHANNELS"));
        }
        sb.append(" found at handoff (");
        sb.append(node.getTid());
        sb.append(")");
        //         getFaultsList().add("Check  Consecutive STS "+StringUtil.LINE_SEPARATOR+sb.toString());
        generateValidation("Non-Consecutive STS", sb.toString(), ValidationResultType.ERR, ValidationCheck.CONSECUTIVE_STS, validationMap);

    }
}

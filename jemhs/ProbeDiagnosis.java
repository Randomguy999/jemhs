package com.verizon.delphi.hyperion.core.network.signal;

import com.verizon.delphi.hyperion.core.network.measurements.Measurement;
import com.verizon.delphi.hyperion.core.network.measurements.ReadingGroup;
import com.verizon.delphi.hyperion.core.network.model.NetworkDesign;
import com.verizon.delphi.hyperion.dip.ValidationInfo;
import com.verizon.delphi.hyperion.util.StringUtil;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author KADIYVE
 */
public class ProbeDiagnosis extends Diagnosis {

    private static final transient Logger logger = LoggerFactory.getLogger(ProbeDiagnosis.class);

    NetworkDesign design = null;
    private List<String> faultsList=null;
    private List<String> notesList = null;
    private List<String> verifiedList = null;
    private List<String> summaryList = null;
    private List<String> detailsList= null;

    private String sumCD;
    private String shortSummary;
    private String longSummary;
    private String details;

    public ProbeDiagnosis(List<ReadingGroup> readingGroups, List<Measurement> measurements, String summary, String probeId) {
        super(readingGroups, measurements, summary, probeId);
    }

/*    public ProbeDiagnosis(List<ReadingGroup> readingGroups, List<Measurement> measurements, String summary, String probeId) {
        super(readingGroups, measurements, summary, probeId);
    } */
    
    public void init(List<String> faultsList, List<String> notesList, List<String> verifiedList, String sumCD, String shortSummary, String longSummary) {
        this.faultsList = faultsList;
        this.verifiedList = verifiedList;
        this.notesList = notesList;
        this.sumCD = sumCD;
        this.shortSummary = shortSummary;
        this.longSummary = longSummary;
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
      //      addpRemark(pr);
        } catch (Exception ex) {
            logger.error("Exception occured in composing the sections", ex);
        }

    }

 /*   private void setSumCD(String newSumCD, String newShortSummary, String newLongSummary) {
        this.sumCD = newSumCD;
        this.shortSummary = newShortSummary;
        this.longSummary = newLongSummary;

    }  */

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
}

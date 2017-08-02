package com.verizon.delphi.hyperion.core.network.signal;

import com.verizon.delphi.hyperion.core.network.measurements.Measurement;
import com.verizon.delphi.hyperion.core.network.measurements.ReadingGroup;
import com.verizon.delphi.hyperion.dip.ValidationInfo;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author @author Swagatika
 */


public class DispatchVerificationDiagnosis extends Diagnosis {
    
    private  List<ValidationInfo> verificationList;
    
    public DispatchVerificationDiagnosis(List<ReadingGroup> readingGroups, List<Measurement> measurements, String summary, String probeId) {
        super(readingGroups,measurements,summary,probeId);
        this.verificationList = new LinkedList<ValidationInfo>();
    }
    
     public List<ValidationInfo> getVerificationList() {
         return verificationList;
      }
      
    public void setVerificationList(List<ValidationInfo> verificationList) {
        this.verificationList = verificationList;
    }
    
     public ValidationInfo getValidationInfo(){
         if(verificationList.isEmpty()) return null;
         return verificationList.get(0);
     }
       
    public void addValidationInfo(ValidationInfo rawVerification) {
        if (verificationList == null) verificationList = new LinkedList<ValidationInfo>();
        this.verificationList.add(rawVerification);
    }
    
}

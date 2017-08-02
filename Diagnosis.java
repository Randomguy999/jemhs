package com.verizon.delphi.hyperion.core.network.signal;

import com.verizon.delphi.hyperion.core.network.measurements.*;
import com.verizon.delphi.hyperion.core.network.model.*;
import java.util.*;


public class Diagnosis implements java.io.Serializable {
    
    private static final long serialVersionUID = 1L;
  
    final private List<ReadingGroup> readingGroups;
 
    final private List<Measurement> measurements;
    final private String summary;
   
    private final String probeId;
    
    private List<ProbeRemarks> pRemarks;
    
    public Diagnosis(List<ReadingGroup> readingGroups, List<Measurement> measurements, String summary, String probeId) {
        this.readingGroups = readingGroups;
        this.measurements = measurements;
        this.summary = summary;
        this.probeId = probeId;
        this.pRemarks = new LinkedList<ProbeRemarks>();
    }

     public List<ProbeRemarks> getAllRemarks() {
        return pRemarks;
    }
     
      
     public ProbeRemarks getpRemarks(){
         if(pRemarks.isEmpty()) return null;
         return pRemarks.get(0);
     }

     public List<ProbeRemarks> getpRemarksList(){
         if(pRemarks.isEmpty()) return null;
         return pRemarks;
     }
     
    public void setpRemarks(List<ProbeRemarks> pRemarks) {
        this.pRemarks = pRemarks;
    }
    
    public void addpRemark(ProbeRemarks pRemark) {
        if (pRemarks == null) pRemarks = new LinkedList<ProbeRemarks>();
        this.pRemarks.add(pRemark);
    }
    


    public String getProbeId() {
        return probeId;
    }


    public String getSummary() {
        return summary;
    }

    

    public List<ReadingGroup> getReadingGroups() {
        return readingGroups;
    }

    
    public void addReadingGroup(ReadingGroup rg) {
        //readingGroups.add(rg);
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void addMeasurement(Measurement m) {
        //this.measurements.add(m);
    }

    public Reading[] getFaults() {
        List<Reading> faults = new LinkedList<Reading>();
        for (ReadingGroup rg : readingGroups) {
            if (rg.getInterpretation().getCategory().equals("Fault")) {
                faults.addAll(rg.getReadings());
            }
        }
        Reading[] a = faults.toArray(new Reading[0]);
        Arrays.sort(a);
        return a;
    }

 
    /*
    public Set<Node> getNodes(String category) {
        Set<Node> nSet = new LinkedHashSet<Node>();
        for (ReadingGroup rg : readingGroups) {
            if (rg.getInterpretation().getCategory().equals(category)) {
                nSet.add(rg.getMeasurement().getNode());
            }
        }
        return nSet;
    }
     * 
     */

  

    public String print() {
        StringBuilder s = new StringBuilder();

        s.append("Summary: ").append(getSummary()).append("\n");


        s.append("total measurements = ").append(getMeasurements().size()).append("\n");
        s.append("faults:\n");
        for (Reading r : getFaults()) {
            String print = r.print();
            //if( print != null && !print.contains("TL1 command succeded but nothing found in response"))
            s.append(r.getInterpretation().name()).append("-").append(print).append("\n");
        }

        for (Measurement.Status status : Measurement.Status.values()) {
            s.append("\n****** ").append(status.name()).append(" measurements:\n");

            List<Measurement> ml = getMeasurements();
            
            Collections.sort(ml, new Comparator<Measurement>() {

                public int compare(Measurement o1, Measurement o2) {
                    return o1.print().compareTo(o2.print());
                }
            });
            
            for (Measurement m : ml) {
                if (m.getStatus() == status) {
                    s.append(m.print()).append("\n");
                    String resp = null;
                    if (m.getTl1Responses() != null && m.getTl1Responses().isEmpty() == false) {
                        resp = m.getTl1Responses().get(0);
                    }
                    if (status == Measurement.Status.INCOMPLETE && resp != null) {
                        s.append(resp).append("error: ").append(m.getError() == null ? "no error message!" : m.getError());
                        s.append(resp).append("\n");
                    }
                }
            }

        }
        
        s.append("\n\nRemarks:"); // missing remarks
        
        return s.toString();
    }

    
    
    public String printReadingGroups() {
        StringBuilder buf = new StringBuilder();
        
        List<ReadingGroup> l = getReadingGroups();
        
        buf.append(String.format("%s\t%s\t%s\t%s\n", "TID", "AID", "TYPE", "Interpretation"));
        
        Collections.sort(l, new Comparator<ReadingGroup> () {

            public int compare(ReadingGroup o1, ReadingGroup o2) {
                Node n1 = o1.getMeasurement().getNode();
                Node n2 = o1.getMeasurement().getNode();
                int nc = n1.getTid().compareTo(n2.getTid());
                if (nc != 0) return nc;
                return o1.getNetworkObject().getAID().compareTo(o2.getNetworkObject().getAID());                
            }
            
        });
        
        for(ReadingGroup r : l) {
            

            
            String tid, aid, type, interp;
            NetworkObject obj = r.getNetworkObject();
            interp = r.getInterpretation().name();
            aid = obj.getAID();
            tid = r.getMeasurement().getNode().getTid();
            type = r.getMeasurement().getCommand().name();            
            buf.append(String.format("%s\t%s\t%s\t%s\n", tid, aid, type, interp));
            
                        
        }
        
        return buf.toString();
    }
}

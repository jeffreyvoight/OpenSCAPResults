package org.voight.openscapresults.objects;

import java.util.Hashtable;

public class Results {

    protected Hashtable<String, CVE> resultTable;

    public Results() {
        resultTable = new Hashtable<>();
    }

    public void putCVE(CVE theCVE){
        resultTable.put(theCVE.getRefId(), theCVE);
    }
}

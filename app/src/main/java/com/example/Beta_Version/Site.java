package com.example.Beta_Version;

public class Site {
    public String SiteName,ManagerName,SiteId;

    /**
     * @author		Dvir Dadon <dd2640@bs.amalnet.k12.il
     * @version	4.2.1
     * This class organize all the information for the Site that is going to register
     */

    public Site(){}

    public Site(String ManagerName , String SiteName, String SiteId){
        this.ManagerName = ManagerName;
        this.SiteName = SiteName;
        this.SiteId = SiteId;
    }

    public String getSiteName() {
        return SiteName;
    }

    public void setSiteName(String siteName) {
        SiteName = siteName;
    }

    public String getManagerName() {
        return ManagerName;
    }

    public void setManagerName(String managerName) {
        ManagerName = managerName;
    }

    

}

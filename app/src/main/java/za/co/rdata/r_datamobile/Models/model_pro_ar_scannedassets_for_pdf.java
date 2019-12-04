package za.co.rdata.r_datamobile.Models;

/**
 * Created by James de Scande on 18/10/2017 at 09:20.
 */

public class model_pro_ar_scannedassets_for_pdf {



    public String getLocationassetwasscanned() {
        return locationassetwasscanned;
    }

    public void setLocationassetwasscanned(String locationassetwasscanned) {
        this.locationassetwasscanned = locationassetwasscanned;
    }

    public String getNameofthelocation() {
        return nameofthelocation;
    }

    public void setNameofthelocation(String nameofthelocation) {
        this.nameofthelocation = nameofthelocation;
    }

    public String getBarcodeoftheasset() {
        return barcodeoftheasset;
    }

    public void setBarcodeoftheasset(String barcodeoftheasset) {
        this.barcodeoftheasset = barcodeoftheasset;
    }

    public String getDescriptionofasset() {
        return descriptionofasset;
    }

    public void setDescriptionofasset(String descriptionofasset) {
        this.descriptionofasset = descriptionofasset;
    }

    public String getUseridofscanner() {
        return useridofscanner;
    }

    public void setUseridofscanner(String useridofscanner) {
        this.useridofscanner = useridofscanner;
    }

    public String getDateassetwasscanned() {
        return dateassetwasscanned;
    }

    public void setDateassetwasscanned(String dateassetwasscanned) {
        this.dateassetwasscanned = dateassetwasscanned;
    }

    private String barcodeoftheasset;
    private String descriptionofasset;
    private String useridofscanner;
    private String locationassetwasscanned;
    private String nameofthelocation;
    private String dateassetwasscanned;

    public model_pro_ar_scannedassets_for_pdf(String locationassetwasscanned, String nameofthelocation, String barcodeoftheasset, String descriptionofasset, String useridofscanner, String dateassetwasscanned) {
        this.locationassetwasscanned = locationassetwasscanned;
        this.nameofthelocation = nameofthelocation;
        this.barcodeoftheasset = barcodeoftheasset;
        this.descriptionofasset = descriptionofasset;
        this.useridofscanner = useridofscanner;
        this.dateassetwasscanned = dateassetwasscanned;
    }



}

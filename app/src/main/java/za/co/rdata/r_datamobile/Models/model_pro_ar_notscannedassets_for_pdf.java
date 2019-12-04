package za.co.rdata.r_datamobile.Models;

/**
 * Created by James de Scande on 18/10/2017 at 09:22.
 */

public class model_pro_ar_notscannedassets_for_pdf {

    private String barcodeoftheasset;
    private String descriptionofasset;
    private String locationassetshouldbe;
    private String nameofthelocation;

    public model_pro_ar_notscannedassets_for_pdf(String barcodeoftheasset, String descriptionofasset, String locationassetshouldbe, String nameofthelocation) {
        this.barcodeoftheasset = barcodeoftheasset;
        this.descriptionofasset = descriptionofasset;
        this.locationassetshouldbe = locationassetshouldbe;
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

    public String getLocationassetshouldbe() {
        return locationassetshouldbe;
    }

    public void setLocationassetshouldbe(String locationassetshouldbe) {
        this.locationassetshouldbe = locationassetshouldbe;
    }

    public String getNameofthelocation() {
        return nameofthelocation;
    }

    public void setNameofthelocation(String nameofthelocation) {
        this.nameofthelocation = nameofthelocation;
    }
}

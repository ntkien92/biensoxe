package kien.objects;

/**
 * Created by KIEN on 07/05/2015.
 */
public class License {
    private String numberProvince;
    private String province;
    private int id;

    public License(String numberProvince, String province, int id){
        this.numberProvince = numberProvince;
        this.province = province;
        this.id = id;
    }

    public String getNumberProvince() {
        return numberProvince;
    }

    public void setNumberProvince(String numberProvince) {
        this.numberProvince = numberProvince;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

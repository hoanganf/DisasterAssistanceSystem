package an.it.controls.databases.datas;

/**
 * Created by hoang on 6/5/2016.
 */
public class Field {
    private String key;
    private String value;

    public Field(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

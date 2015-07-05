import com.sidemash.redson.Json;

/**
 * Created by Serge Martial on 27/06/2015.
 */
public class Charac<T> {
    private String name;
    private T value;
    private String desc;

    public Charac(String name, T value, String desc) {
        this.name = name;
        this.value = value;
        this.desc = desc;
    }

    static {
        Json.registerReader(Charac.class, (jsVal, charac) -> new Charac(jsVal.get("name").asString(), jsVal.get("value").asInt(), jsVal.get("desc").asString()));
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Charac<?> charac = (Charac<?>) o;

        if (name != null ? !name.equals(charac.name) : charac.name != null) return false;
        if (value != null ? !value.equals(charac.value) : charac.value != null)
            return false;
        return !(desc != null ? !desc.equals(charac.desc) : charac.desc != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

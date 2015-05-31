import com.sidemash.redson.*;

public class B extends A {
    public byte b = 7;
    static {
        Json.registerWriter(B.class, (B b, JsonValue js) -> {
            return JsonObject.EMPTY;
        });
    }
}

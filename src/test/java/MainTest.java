import com.sidemash.redson.JsonValue;

public class MainTest {
    public static void main(String[] args) {
        C Cinst = new C();
        JsonValue.of(Cinst).prettyStringify();
    }
}

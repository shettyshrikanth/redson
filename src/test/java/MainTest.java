import com.sidemash.redson.JsonValue;

public class MainTest {
    public static void main(String[] args) {

        System.out.println(JsonValue.of(new Person()).prettyStringify());
    }
}

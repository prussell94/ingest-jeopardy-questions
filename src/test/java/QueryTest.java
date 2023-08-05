import com.jeopardy.trivia.Query;
import org.junit.jupiter.api.Test;

public class QueryTest {
    Query.Builder builder = new Query.Builder("clues")
            .setCategory("Presidents")
            .setLimit(10)
            .setOffset(20)
            .setOrder("cats")
            .setSort("asc")
            .setDifficulty(2);

    @Test
    public void testBUilder() {
        System.out.println(builder.build().getQueryStatement());
    }
}

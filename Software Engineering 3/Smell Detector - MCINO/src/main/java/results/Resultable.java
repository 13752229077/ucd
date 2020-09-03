package results;

import java.util.List;

public interface Resultable {
    List<Occurrence> getOccurrences();
    int getSeverity();
}

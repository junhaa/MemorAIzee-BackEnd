package memoraize.domain.photo.service;

import java.util.List;

public interface GeminiApiService {

    public String generateTitle(List<String> colors, List<String> labels);
    String generateComment(List<String> colors, List<String> labels, String place);

}

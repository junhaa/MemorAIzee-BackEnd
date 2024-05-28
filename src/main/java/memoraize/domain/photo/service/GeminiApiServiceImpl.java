package memoraize.domain.photo.service;

import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeminiApiServiceImpl implements GeminiApiService {
    private static final Logger log = LogManager.getLogger(GeminiApiServiceImpl.class);
    private Gson gson = new Gson();
    private HttpHeaders headers = new HttpHeaders();

    @Value("${cloud.google.gemini.api-key}")
    private String apiKey;

    public String generateTitle(List<String> colors, List<String> labels, String place) {
        String url = "https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent?key=" + apiKey;
        headers.setContentType(MediaType.APPLICATION_JSON);

        // JSON 데이터 구성
        String jsonBody;
        if (place == null) {
            jsonBody =
                "{\"contents\":[{\"parts\":[{\"text\":\"이 글은 여행 사진에 대한 정보야. 사진의 label은 " + joinStrings(labels) + ". " +
                    "사진의 dominant color는" + joinStrings(colors) + ". " +
                    "사진의 제목을 30자 내외로 써 줘.\"}]}]}";

        } else {
            // JSON 데이터 구성
            jsonBody =
                "{\"contents\":[{\"parts\":[{\"text\":\"이 글은 여행 사진에 대한 정보야. 사진의 label은 " + joinStrings(labels) + ". " +
                    "사진의 dominant color는" + joinStrings(colors) + ". " +
                    "위치는" + place + ". 위치 정보는 모호하게 표현해." +
                    "사진의 제목을 30자 내외로 써 줘.\"}]}]}";

        }

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
            String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return extractTextFromJson(responseEntity.getBody());
        } else {
            return "Error: " + responseEntity.getStatusCodeValue();
        }

    }

    public String generateComment(List<String> colors, List<String> labels, String place) {
        String url = "https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent?key=" + apiKey;
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonBody;
        if (place == null) {
            jsonBody =
                "{\"contents\":[{\"parts\":[{\"text\":\"이 글은 여행 사진에 대한 정보야. 사진의 label은 " + joinStrings(labels) + ". " +
                    "사진의 dominant color는" + joinStrings(colors) + ". " +
                    "사진에 대한 글을 150자 내외로 써 줘. 완결된 문장으로 써. 최대한 추상적으로 작성해. " +
                    "주어진 정보만 활용해. 두루뭉술하게 작성해. 시간을 유추할 수 있는 단어를 넣지 마. 일기 형식으로 작성 해.\"}]}]}";
        } else {
            // JSON 데이터 구성
            jsonBody =
                "{\"contents\":[{\"parts\":[{\"text\":\"이건 여행 사진에 대한 정보야. 사진의 레이블은 " + joinStrings(labels) + ". " +
                    "사진의 dominant colors는" + joinStrings(colors) + ". " +
                    "위치는" + place + ". " +
                    "사진에 대한 글을 150자 내외로 써 줘. 완결된 문장으로 써. 최대한 추상적으로 작성해. " +
                    "주어진 정보만 활용해. 두루뭉술하게 작성해. 시간을 유추할 수 있는 단어를 넣지 마. 일기 형식으로 작성 해.\"}]}]}";

        }

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
            String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return extractTextFromJson(responseEntity.getBody());
        } else {
            return "Error: " + responseEntity.getStatusCodeValue();
        }
    }

    private String joinStrings(List<String> list) {
        //중간에는 쉼표, 마지막에는 마침표 넣도록하자.
        StringBuilder result = new StringBuilder();
        Iterator<String> iterator = list.iterator();

        //list가 빈 경우 예외처리 필요<
        if (iterator.hasNext()) {
            while (true) {
                result.append(iterator.next());
                if (!iterator.hasNext())
                    break;
                result.append(", ");
            }
        } else {

        }

        return result.toString();
    }

    private String extractTextFromJson(String jsonBody) {
        ResponseData responseData = gson.fromJson(jsonBody, ResponseData.class);

        return responseData.getCandidates().get(0).getContent().getParts().get(0).getText();
    }

    //앨범용, 사진용
    //결과 파싱?용

    public class ResponseData {
        private List<Candidate> candidates;
        private UsageMetadata usageMetadata;

        public List<Candidate> getCandidates() {
            return candidates;
        }

        public UsageMetadata getUsageMetadata() {
            return usageMetadata;
        }
    }

    class Candidate {
        private Content content;
        private String finishReason;
        private int index;
        private List<SafetyRating> safetyRatings;

        public Content getContent() {
            return content;
        }

        public String getFinishReason() {
            return finishReason;
        }

        public int getIndex() {
            return index;
        }

        public List<SafetyRating> getSafetyRatings() {
            return safetyRatings;
        }
    }

    class Content {
        private List<Part> parts;
        private String role;

        public List<Part> getParts() {
            return parts;
        }

        public String getRole() {
            return role;
        }
    }

    class Part {
        private String text;

        public String getText() {
            return text;
        }
    }

    class SafetyRating {
        private String category;
        private String probability;

        public String getCategory() {
            return category;
        }

        public String getProbability() {
            return probability;
        }
    }

    class UsageMetadata {
        private int promptTokenCount;
        private int candidatesTokenCount;
        private int totalTokenCount;

        public int getPromptTokenCount() {
            return promptTokenCount;
        }

        public int getCandidatesTokenCount() {
            return candidatesTokenCount;
        }

        public int getTotalTokenCount() {
            return totalTokenCount;
        }
    }
}

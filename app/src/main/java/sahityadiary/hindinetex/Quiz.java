package sahityadiary.hindinetex;

/**
 * Created by Abdelrahman Hesham on 12/11/2017.
 */

public class Quiz  {
    private String topicId;
    private String topicName;
    private String quizName;
    private String quizId;

    public Quiz(String topicId, String topicName, String quizName, String quizId) {
        this.topicId = topicId;
        this.topicName = topicName;
        this.quizName = quizName;
        this.quizId = quizId;
    }

    public String getTopicId() {
        return topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getQuizName() {
        return quizName;
    }

    public String getQuizId() {
        return quizId;
    }
}

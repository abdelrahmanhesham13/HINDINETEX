package sahityadiary.hindinetex;

/**
 * Created by Abdelrahman Hesham on 12/11/2017.
 */

public class Subject {
    private int subjectId;
    private String subjectName;

    public Subject(int subjectId, String subjectName) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }
}
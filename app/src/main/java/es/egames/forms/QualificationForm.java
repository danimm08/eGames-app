package es.egames.forms;

/**
 * Created by daniel on 3/04/17.
 */
public class QualificationForm {

    private String text;
    private Double mark;

    public QualificationForm() {
        super();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Double getMark() {
        return mark;
    }

    public void setMark(Double mark) {
        this.mark = mark;
    }
}

package edu.nus.adproject.models;

public class Tag {
    private int id;
    private String tags;
    private PCMsg pcmsg;
    private Label label;
    private String remark;

    public Tag() {}

    public Tag(String tag, String remark) {
        this.tags = tag;
        this.remark = remark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public PCMsg getPcmsg() {
        return pcmsg;
    }

    public void setPcmsg(PCMsg pcmsg) {
        this.pcmsg = pcmsg;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

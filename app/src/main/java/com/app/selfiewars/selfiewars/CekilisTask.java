package com.app.selfiewars.selfiewars;

class CekilisTask {
    private String desc;
    private String bracketsdesc;
    private String buttonText;
    private int id;

    public CekilisTask() {
    }

    public CekilisTask(String desc, String bracketsdesc, String buttonText, int id) {
        this.desc = desc;
        this.bracketsdesc = bracketsdesc;
        this.buttonText = buttonText;
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBracketsdesc() {
        return bracketsdesc;
    }

    public void setBracketsdesc(String bracketsdesc) {
        this.bracketsdesc = bracketsdesc;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

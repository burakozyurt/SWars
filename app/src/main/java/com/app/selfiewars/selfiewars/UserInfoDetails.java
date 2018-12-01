package com.app.selfiewars.selfiewars;

public class UserInfoDetails {
    private String display_name;
    private Long display_telephone;
    private String display_gender;
    private String display_dateofbirth;
    private String display_adress;

    public UserInfoDetails() {
        
    }

    public UserInfoDetails(String display_name, Long display_telephone, String display_gender, String display_dateofbirth, String display_adress) {
        this.display_name = display_name;
        this.display_telephone = display_telephone;
        this.display_gender = display_gender;
        this.display_dateofbirth = display_dateofbirth;
        this.display_adress = display_adress;
        
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }





    public Long getDisplay_telephone() {
        return display_telephone;
    }

    public void setDisplay_telephone(Long display_telephone) {
        this.display_telephone = display_telephone;
    }

    public String getDisplay_gender() {
        return display_gender;
    }

    public void setDisplay_gender(String display_gender) {
        this.display_gender = display_gender;
    }

    public String getDisplay_dateofbirth() {
        return display_dateofbirth;
    }

    public void setDisplay_dateofbirth(String display_dateofbirth) {
        this.display_dateofbirth = display_dateofbirth;
    }

    public String getDisplay_adress() {
        return display_adress;
    }

    public void setDisplay_adress(String display_adress) {
        this.display_adress = display_adress;
    }
}

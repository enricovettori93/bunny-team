package it.unive.dais.bunnyteam.unfinitaly.app.entities;

/**
 * Created by Enrico on 02/03/2018.
 */

public class User {
    private String name;
    private String email;
    private static User me = new User();

    private User(){
        name = "";
        email = "";
    }

    public void userLogOut(){
        name = "";
        email = "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static User getIstance(){
        return me;
    }

}

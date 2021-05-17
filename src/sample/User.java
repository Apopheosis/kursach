package sample;

public class User {
    private String firstName;
    private String lastName;
    private String userName;
    private String userPass;
    private String occupation;
    private String id;

    public User() {

    }

    public User(String firstName, String lastName, String userName, String userPass, String occupation, String id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.userPass = userPass;
        this.occupation = occupation;
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public String getId() {
        return id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }
}

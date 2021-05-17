package sample;

public class PlannedManual extends Manual {
    private String date;

public PlannedManual(String title, String author, String date) {
    this.title = title;
    this.author = author;
    this.date = date;
}

public String getDate() {
    return date;
}

public void setDate(String date) {
    this.date = date;
}

}



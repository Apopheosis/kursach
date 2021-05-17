package sample;

public class Manual {
    String title;
    private String year;
    String author;
    private String ready;
    private String Id;

    public Manual() {

    }

    public Manual(String id, String title, String year, String author, String ready) {
        this.title = title;
        this.Id = id;
        this.year = year;
        this.author = author;
        this.ready = ready;
    }

    public Manual(String title, String year, String author, String ready) {
        this.title = title;
        this.year = year;
        this.author = author;
        this.ready = ready;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getId() {
        return Id;
    }

    public String getReady() {
        return ready;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }
    public String getAuthor() {
        return author;
    }

    public void setYear(String Year) {
        this.year = Year;
    }

    public void setAuthor(String Author) {
        this.author = Author;
    }

}

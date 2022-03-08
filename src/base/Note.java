package base;

import java.util.Date;
import java.util.Objects;

public class Note implements Comparable<Note> {
  private Date date;
  private String title;

  public Note(String title) {
    this.title = title;
    date = new Date();
  }

  public String getTitle() {
    return this.title;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
    if (!(o instanceof Note)) {
      return false;
    }
    Note note = (Note) o;
    return Objects.equals(title, note.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title);
  }

  @Override
  public int compareTo(Note o) {
    return date.compareTo(o.date);
  }

  public String toString() {
    return date.toString() + "\t" + title;
  }

}

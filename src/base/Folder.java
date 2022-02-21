package base;

import java.util.ArrayList;

public class Folder {
  private ArrayList<Note> notes;
  private String name;

  public Folder(String name) {
    this.name = name;
    notes = new ArrayList<Note>();
  }

  public void addNote(Note note) {
    notes.add(note);
  }

  public String getName() {
    return this.name;
  }

  public ArrayList<Note> getNotes() {
    return this.notes;
  }

  public String toString() {
    long nText = notes.stream().filter(n -> n instanceof TextNote).count();
    long nImage = notes.stream().filter(n -> n instanceof ImageNote).count();
    return name + ":" + nText + ":" + nImage;
  }

  public boolean equals(Folder folder) {
    return this.name == folder.getName();
  }

}

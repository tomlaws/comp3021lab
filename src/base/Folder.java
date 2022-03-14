package base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Folder implements Comparable<Folder>, Serializable {
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

  @Override
  public int compareTo(Folder o) {
    return name.compareTo(o.name);
  }

  public void sortNotes() {
    Collections.sort(notes);
  }

  public List<Note> searchNotes(String keywords) {
    String[] splitted = keywords.toLowerCase().split(" ");
    // Stores and conditions in the array list, each element is a list of string
    // storing the or conditions
    List<List<String>> ands = new ArrayList<>();
    for (int i = 0; i < splitted.length; i++) {
      if (splitted[i].equals("or")) {
        if (i + 1 < splitted.length) {
          ands.get(ands.size() - 1).add(splitted[++i]);
        }
      } else {
        ands.add(new ArrayList<>(Arrays.asList(splitted[i])));
      }
    }
    List<Note> results = new ArrayList<>();
    for (Note note : notes) {
      boolean allMatch = true;
      for (List<String> ors : ands) {
        // Check if at least one match among the "or" conditions
        boolean anyMatch = ors.stream().anyMatch(s -> {
          if (note instanceof ImageNote)
            return note.getTitle().toLowerCase().contains(s);
          else if (note instanceof TextNote)
            return note.getTitle().toLowerCase().contains(s)
                || ((TextNote) note).getContent().toLowerCase().contains(s);
          return false;
        });
        // Early break since it failed to fulfill one of the "and" conditions
        if (!anyMatch) {
          allMatch = false;
          break;
        }
      }
      // If allMatch is true, it means the note fulfilled all "and" conditions
      if (allMatch) {
        results.add(note);
      }
    }
    return results;
  }
}

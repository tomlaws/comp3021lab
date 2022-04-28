package base;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class NoteBook implements Serializable {
  private ArrayList<Folder> folders;

  public NoteBook() {
    this.folders = new ArrayList<Folder>();
  }

  public NoteBook(String file) {
    try {
      FileInputStream fis = new FileInputStream(file);
      ObjectInputStream in = new ObjectInputStream(fis);
      NoteBook n = (NoteBook) in.readObject();
      this.folders = n.folders;
      in.close();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public boolean createTextNote(String folderName, String title) {
    TextNote note = new TextNote(title);
    return insertNote(folderName, note);
  }

  public boolean createTextNote(String folderName, String title, String content) {
    TextNote note = new TextNote(title, content);
    return insertNote(folderName, note);
  }

  public boolean createImageNote(String folderName, String title) {
    ImageNote note = new ImageNote(title);
    return insertNote(folderName, note);
  }

  public ArrayList<Folder> getFolders() {
    return this.folders;
  }

  public boolean insertNote(String folderName, Note note) {
    Folder folder = folders.stream().filter(f -> f.getName() == folderName).findFirst().orElse(null);
    if (folder == null) {
      // Create a new folder if not exists
      folder = new Folder(folderName);
      folders.add(folder);
    } else {
      // Check if note already exists in the existing folder
      boolean exists = folder.getNotes().stream().anyMatch(n -> n.getTitle() == note.getTitle());
      if (exists) {
        System.out.println("Creating note " + note.getTitle() + " under folder " + folderName + " failed");
        return false;
      }
    }
    folder.addNote(note);
    return true;
  }

  public void sortFolders() {
    for (Folder folder : folders) {
      folder.sortNotes();
    }
    Collections.sort(folders);
  }

  public List<Note> searchNotes(String keywords) {
    List<Note> results = new ArrayList<>();
    for (Folder folder : folders) {
      results.addAll(folder.searchNotes(keywords));
    }
    return results;
  }

  // For code interview
  public SortedSet<String> mostFrequentWords() {
    HashMap<String, Integer> occurences = new HashMap<>();
    for (Folder folder : folders) {
      for (Note note : folder.getNotes()) {
        String[] words = note.getTitle().split("\\W+");
        for (String word : words) {
          occurences.put(word.toLowerCase(), occurences.getOrDefault(word.toLowerCase(), 0) + 1);
        }
      }
    }
    TreeSet<String> sorted = new TreeSet<>(new Comparator<String>() {
      @Override
      public int compare(String s1, String s2) {
        int res = occurences.get(s2) - occurences.get(s1);
        if (res == 0)
          return s1.compareTo(s2);
        return res;
      }
    });
    for (String w : occurences.keySet()) {
      sorted.add(w);
    }
    return sorted;
  }

  // lab4
  public boolean save(String file) {
    try {
      FileOutputStream fos = new FileOutputStream(file);
      ObjectOutputStream out = new ObjectOutputStream(fos);
      out.writeObject(this);
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
}

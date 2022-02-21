package base;

import java.util.ArrayList;
import java.util.Optional;

public class NoteBook {
  private ArrayList<Folder> folders;

  public NoteBook() {
    this.folders = new ArrayList<Folder>();
  }

  public boolean createTextNote(String folderName, String title) {
    TextNote note = new TextNote(title);
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

}

package base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class TextNote extends Note {
  private String content;

  public TextNote(String title) {
    super(title);
  }

  public TextNote(String title, String content) {
    super(title);
    this.content = content;
  }

  public TextNote(File f) {
    super(f.getName().replaceAll("_", " "));
    this.content = getTextFromFile(f.getAbsolutePath());
  }

  public String getTextFromFile(String absolutePath) {
    String result = "";
    try {
      FileInputStream fis = new FileInputStream(absolutePath);
      ObjectInputStream in = new ObjectInputStream(fis);
      result = (String) in.readObject();
      in.close();
    } catch (Exception ex) {
    }
    return result;
  }

  public void exportTextToFile(String pathFolder) {
    if (pathFolder == "") {
      pathFolder = ".";
    }
    try {
      File file = new File(pathFolder + File.separator + this.getTitle().replaceAll(" ", "_") + ".txt");
      FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
      ObjectOutputStream out = new ObjectOutputStream(fos);
      out.writeObject(this.content);
      out.close();
    } catch (Exception ex) {

    }
  }

  public String getContent() {
    return this.content;
  }
  public void setContent(String content) { this.content = content; }
}

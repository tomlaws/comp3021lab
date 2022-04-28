package base;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import base.Folder;
import base.Note;
import base.NoteBook;
import base.TextNote;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * 
 * NoteBook GUI with JAVAFX
 * 
 * COMP 3021
 * 
 * 
 * @author valerio
 *
 */
public class NoteBookWindow extends Application {

  /**
   * TextArea containing the note
   */
  final TextArea textAreaNote = new TextArea("");
  /**
   * list view showing the titles of the current folder
   */
  final ListView<String> titleslistView = new ListView<String>();
  /**
   * 
   * Combobox for selecting the folder
   * 
   */
  final ComboBox<String> foldersComboBox = new ComboBox<String>();
  /**
   * This is our Notebook object
   */
  NoteBook noteBook = null;
  /**
   * current folder selected by the user
   */
  String currentFolder = "";
  /**
   * current search string
   */
  String currentSearch = "";

  String currentNote = "";

  Stage stage;

  File file;

  Button buttonSave;

  public static void main(String[] args) {
    launch(NoteBookWindow.class, args);
  }

  @Override
  public void start(Stage stage) {
    // loadNoteBook();
    this.stage = stage;
    // Use a border pane as the root for scene
    BorderPane border = new BorderPane();
    // add top, left and center
    border.setTop(addHBox());
    border.setLeft(addVBox());
    border.setCenter(addGridPane());

    Scene scene = new Scene(border);
    stage.setScene(scene);
    stage.setTitle("NoteBook COMP 3021");
    stage.show();
  }

  /**
   * This create the top section
   * 
   * @return
   */
  private HBox addHBox() {

    HBox hbox = new HBox();
    hbox.setPadding(new Insets(15, 12, 15, 12));
    hbox.setSpacing(10); // Gap between nodes

    Button buttonLoad = new Button("Load from File");
    buttonLoad.setPrefSize(100, 20);
    buttonLoad.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setTitle("Please Choose A File Which Contains a NoteBook Object!");

        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Serialized Object File (*.ser)", "*.ser");
        fileChooser.getExtensionFilters().add(extensionFilter);

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
          loadNoteBook(file);
        }
      }
    });
    //buttonLoad.setDisable(true);
    buttonSave = new Button("Save");
    buttonSave.setPrefSize(100, 20);
    buttonSave.setDisable(true);
    buttonSave.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent actionEvent) {
        noteBook.save(file.getName());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successfully saved");
        alert.setContentText("You file has been saved to file "
                + file.getName());
        alert.showAndWait().ifPresent(rs -> {
          if (rs == ButtonType.OK) {
            System.out.println("Pressed OK.");
          }
        });
      }
    });

    Label label = new Label("Search :");

    TextField textSearch = new TextField();

    Button buttonSearch = new Button("Search");
    buttonSearch.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        currentSearch = textSearch.getText();
        textAreaNote.setText("");
        updateListView();
      }
    });
    Button buttonRemove = new Button("Clear Search");
    buttonRemove.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        currentSearch = "";
        textSearch.setText("");
        textAreaNote.setText("");
        updateListView();
      }
    });
    hbox.getChildren().addAll(buttonLoad, buttonSave, label, textSearch, buttonSearch, buttonRemove);

    return hbox;
  }

  /**
   * this create the section on the left
   * 
   * @return
   */
  private VBox addVBox() {

    VBox vbox = new VBox();
    vbox.setPadding(new Insets(10)); // Set all sides to 10
    vbox.setSpacing(8); // Gap between nodes

    // TODO: This line is a fake folder list. We should display the folders in
    // noteBook variable! Replace this with your implementation
    // foldersComboBox.getItems().addAll(noteBook.getFolders().stream().map(f -> f.getName()).collect(Collectors.toList()));

    foldersComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
      @Override
      public void changed(ObservableValue ov, Object t, Object t1) {
        currentFolder = t1 == null ? "" : t1.toString();
        // this contains the name of the folder selected
        // TODO update listview
        updateListView();

      }

    });

    foldersComboBox.setValue("");
    foldersComboBox.setPrefWidth(114);

    titleslistView.setPrefHeight(100);

    titleslistView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
      @Override
      public void changed(ObservableValue ov, Object t, Object t1) {
        if (t1 == null)
          return;
        String title = t1.toString();
        // This is the selected title
        // TODO load the content of the selected note in
        // textAreNote
        Folder folder = noteBook.getFolders().stream().filter(f -> f.getName().equals(currentFolder)).findFirst().orElse(null);
        if (folder == null)
          return;
        Note note = folder.getNotes().stream().filter(n -> n.getTitle().equals(title)).findFirst().orElse(null);
        currentNote = note == null ? "" : note.getTitle();
        if (note == null || !(note instanceof TextNote))
          return;
        String content = ((TextNote) note).getContent();
        textAreaNote.setText(content);

      }
    });
    Button button_addFolder = new Button("Add a Folder");
    Button button_addNote = new Button("Add a Note");
    HBox hBox = new HBox();
    hBox.setSpacing(10);
    hBox.getChildren().addAll(foldersComboBox, button_addFolder);
    vbox.getChildren().add(new Label("Choose folder: "));
    vbox.getChildren().add(hBox);
    vbox.getChildren().add(new Label("Choose note title"));
    vbox.getChildren().add(titleslistView);
    vbox.getChildren().add(button_addNote);
    button_addFolder.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog("Add a Folder");
        dialog.setTitle("Input");
        dialog.setHeaderText("Add a new folder for your notebook:");
        dialog.setContentText("Please enter the name you want to create:");
        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && result.get().length() > 0) {
          String folderName = result.get();
          addFolder(folderName);
        } else {
          Alert alert = new Alert(Alert.AlertType.WARNING);
          alert.setTitle("Warning");
          alert.setContentText("Please input an valid folder name");
          alert.showAndWait();
        }
      }
    });
    button_addNote.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent actionEvent) {
        if (currentFolder.equals("")) {
          Alert alert = new Alert(Alert.AlertType.WARNING);
          alert.setTitle("Warning");
          alert.setContentText("Please choose a folder first!");
          alert.showAndWait();
          return;
        }
        TextInputDialog dialog = new TextInputDialog("Add a Note");
        dialog.setTitle("Input");
        dialog.setHeaderText("Add a new note to current folder");
        dialog.setContentText("Please enter the name of your note:");
        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && result.get().length() > 0) {
          noteBook.createTextNote(currentFolder, result.get());
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setTitle("Successful!");
          alert.setContentText("Insert note " + result.get() + " to folder " + currentFolder + " successfully!");
          alert.showAndWait();
          updateListView();
        } else {
          Alert alert = new Alert(Alert.AlertType.WARNING);
          alert.setTitle("Warning");
          alert.setContentText("Please input an valid folder name");
          alert.showAndWait();
        }
      }
    });
    return vbox;
  }

  private void updateListView() {
    if (noteBook == null) return;
    ArrayList<String> list = new ArrayList<String>();

    // TODO populate the list object with all the TextNote titles of the
    // currentFolder
    Folder folder = noteBook.getFolders().stream().filter(f -> f.getName().equals(currentFolder)).findFirst().orElse(null);
    if (folder == null) {
      return;
    }
    if (currentSearch.isEmpty()) {
      list.addAll(folder.getNotes().stream().map(n -> n.getTitle()).collect(Collectors.toList()));
    } else {
      List<Note> notes = folder.searchNotes(currentSearch);
      list.addAll(notes.stream().map(n -> n.getTitle()).collect(Collectors.toList()));
    }
    ObservableList<String> combox2 = FXCollections.observableArrayList(list);
    titleslistView.setItems(combox2);
    textAreaNote.setText("");
  }

  /*
   * Creates a grid for the center region with four columns and three rows
   */
  private GridPane addGridPane() {

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(10, 10, 10, 10));
    textAreaNote.setEditable(true);
    textAreaNote.setMaxSize(450, 400);
    textAreaNote.setWrapText(true);
    textAreaNote.setPrefWidth(450);
    textAreaNote.setPrefHeight(400);
    // 0 0 is the position in the grid
    ImageView saveView = new ImageView(new Image(new File("save.png").toURI().toString()));
    saveView.setFitHeight(18);
    saveView.setFitWidth(18);
    saveView.setPreserveRatio(true);
    Button button_saveNote = new Button("Save Note");
    button_saveNote.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent actionEvent) {
        if (currentFolder.equals("") || currentNote.equals("")) {
          Alert alert = new Alert(Alert.AlertType.WARNING);
          alert.setTitle("Warning");
          alert.setContentText("Please select a folder and a note");
          alert.showAndWait();
          return;
        }
        Folder folder = noteBook.getFolders().stream().filter(f->f.getName().equals(currentFolder)).findFirst().orElse(null);
        Note note = folder.getNotes().stream().filter(n -> n.getTitle().equals(currentNote)).findFirst().orElse(null);
        if (note instanceof TextNote) {
          ((TextNote) note).setContent(textAreaNote.getText());
        }
      }
    });
    ImageView deleteView = new ImageView(new Image(new File("delete.png").toURI().toString()));
    deleteView.setFitHeight(18);
    deleteView.setFitWidth(18);
    deleteView.setPreserveRatio(true);
    Button button_deleteNote = new Button("Delete Note");
    button_deleteNote.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent actionEvent) {
        removeNotes(currentNote);
      }
    });
    HBox hBox = new HBox();
    hBox.setSpacing(10);
    hBox.getChildren().addAll(saveView, button_saveNote, deleteView, button_deleteNote);

    grid.add(hBox, 0, 0);
    grid.add(textAreaNote, 0, 1);

    return grid;
  }

  private void loadNoteBook() {
    NoteBook nb = new NoteBook();
    nb.createTextNote("COMP3021", "COMP3021 syllabus", "Be able to implement object-oriented concepts in Java.");
    nb.createTextNote("COMP3021", "course information",
        "Introduction to Java Programming. Fundamentals include language syntax, object-oriented programming, inheritance, interface, polymorphism, exception handling, multithreading and lambdas.");
    nb.createTextNote("COMP3021", "Lab requirement",
        "Each lab has 2 credits, 1 for attendence and the other is based the completeness of your lab.");

    nb.createTextNote("Books", "The Throwback Special: A Novel",
        "Here is the absorbing story of twenty-two men who gather every fall to painstakingly reenact what ESPN called “the most shocking play in NFL history” and the Washington Redskins dubbed the “Throwback Special”: the November 1985 play in which the Redskins’ Joe Theismann had his leg horribly broken by Lawrence Taylor of the New York Giants live on Monday Night Football. With wit and great empathy, Chris Bachelder introduces us to Charles, a psychologist whose expertise is in high demand; George, a garrulous public librarian; Fat Michael, envied and despised by the others for being exquisitely fit; Jeff, a recently divorced man who has become a theorist of marriage; and many more. Over the course of a weekend, the men reveal their secret hopes, fears, and passions as they choose roles, spend a long night of the soul preparing for the play, and finally enact their bizarre ritual for what may be the last time. Along the way, mishaps, misunderstandings, and grievances pile up, and the comforting traditions holding the group together threaten to give way. The Throwback Special is a moving and comic tale filled with pitch-perfect observations about manhood, marriage, middle age, and the rituals we all enact as part of being alive.");
    nb.createTextNote("Books", "Another Brooklyn: A Novel",
        "The acclaimed New York Times bestselling and National Book Award–winning author of Brown Girl Dreaming delivers her first adult novel in twenty years. Running into a long-ago friend sets memory from the 1970s in motion for August, transporting her to a time and a place where friendship was everything—until it wasn’t. For August and her girls, sharing confidences as they ambled through neighborhood streets, Brooklyn was a place where they believed that they were beautiful, talented, brilliant—a part of a future that belonged to them. But beneath the hopeful veneer, there was another Brooklyn, a dangerous place where grown men reached for innocent girls in dark hallways, where ghosts haunted the night, where mothers disappeared. A world where madness was just a sunset away and fathers found hope in religion. Like Louise Meriwether’s Daddy Was a Number Runner and Dorothy Allison’s Bastard Out of Carolina, Jacqueline Woodson’s Another Brooklyn heartbreakingly illuminates the formative time when childhood gives way to adulthood—the promise and peril of growing up—and exquisitely renders a powerful, indelible, and fleeting friendship that united four young lives.");

    nb.createTextNote("Holiday", "Vietnam",
        "What I should Bring? When I should go? Ask Romina if she wants to come");
    nb.createTextNote("Holiday", "Los Angeles", "Peter said he wants to go next Agugust");
    nb.createTextNote("Holiday", "Christmas", "Possible destinations : Home, New York or Rome");
    noteBook = nb;

  }
  private void loadNoteBook(File file) {
    try {
      noteBook = new NoteBook(file.getName());
      this.file = file;
      buttonSave.setDisable(false);
      foldersComboBox.getItems().clear();
      foldersComboBox.getItems().addAll(noteBook.getFolders().stream().map(f -> f.getName()).collect(Collectors.toList()));
    } catch (Exception ex) {
      this.file = null;
      noteBook = null;
      buttonSave.setDisable(true);
      foldersComboBox.getItems().clear();
      throw ex;
    }
  }


  public void addFolder(String folderName) {
    Optional<Folder> folder = noteBook.getFolders().stream().filter(f -> f.getName().equals(folderName)).findFirst();
    if (folder.isPresent()) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Warning");
      alert.setContentText("You already have a folder named with " + folderName.toString());
      alert.showAndWait();
    } else {
      noteBook.getFolders().add(new Folder(folderName));
      foldersComboBox.getItems().clear();
      foldersComboBox.getItems().addAll(noteBook.getFolders().stream().map(f -> f.getName()).collect(Collectors.toList()));
      foldersComboBox.setValue(folderName);
    }
  }

  public boolean removeNotes(String title) {
    if (currentFolder.equals("") || currentNote.equals("")) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Warning");
      alert.setContentText("Please select a folder and a note");
      alert.showAndWait();
      return false;
    }
    Folder folder = noteBook.getFolders().stream().filter(f->f.getName().equals(currentFolder)).findFirst().orElse(null);
    folder.getNotes().removeIf(n -> n.getTitle().equals(currentNote));
    updateListView();
    textAreaNote.setText("");
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Succeed!");
    alert.setHeaderText("Confirmation");
    alert.setContentText("Your note has been successfully removed");
    alert.showAndWait();
    return true;
  }
}

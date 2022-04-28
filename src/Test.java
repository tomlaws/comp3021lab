import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class Test {

  static class Item {
    double price;

    public Item(double price) {
    }

    public Item() {
    }

    public double getPrice() {
      return price;
    }

    public double getPrice(double newcap) {
      return price;
    }
  }

  static class Box {
    double weight;
    Item item;

    public Box(Item i) {
      item = i;
    }

    public Box(double weigtht) {
    }

    public double getWeight() {
      return weight;
    }

    public Item getItem() {
      return item;
    }

  }

  public static <T> double reduce(List<T> list, Function<T, Double> mapper) {
    double sum = 0;
    for (T entry : list) {
      sum += mapper.apply(entry);
    }
    return sum;
  }

  public static <T> List<T> make(Supplier<T> mapper, int number) {
    List<T> result = new ArrayList<>();
    for (int i = 0; i < number; ++i) {
      result.add(mapper.get());
    }
    return result;
  }

  public static <T, U> List<T> make(Function<U, T> mapper, U arg, int number) {
    List<T> result = new ArrayList<>();
    for (int i = 0; i < number; ++i) {
      result.add(mapper.apply(arg));
    }
    return result;
  }

  public static void main(String[] args) {
    List<Item> ilist = make(Item::new, 100);
    List<Item> ilist2 = make(Item::new, 3.4, 100);
    List<Box> mlist = make(Box::new, new Item(), 100);

  }

}
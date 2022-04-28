package lab9;

import javafx.concurrent.Task;

/**
 * 
 * COMP 3021
 * 
This is a class that prints the maximum value of a given array of 90 elements

This is a single threaded version.

Create a multi-thread version with 3 threads:

one thread finds the max among the cells [0,29] 
another thread the max among the cells [30,59] 
another thread the max among the cells [60,89]

Compare the results of the three threads and print at console the max value.

 * 
 * @author valerio
 *
 */
public class FindMax {
	// this is an array of 90 elements
	// the max value of this array is 9999
	static int[] array = { 1, 34, 5, 6, 343, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678, 543, 45, 67, 43, 2, 3, 4543,
			234, 3, 454, 1, 2, 3, 1, 9999, 34, 5, 6, 343, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678, 543, 45, 67, 43, 2,
			3, 4543, 234, 3, 454, 1, 2, 3, 1, 34, 5, 6, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678, 543, 45, 67, 43, 2,
			3, 4543, 234, 3, 454, 1, 2, 3 };

	public static void main(String[] args) {
		new FindMax().printMax();
	}

	public class MyRunnable implements Runnable {
		private int[] range;
		private int output;
		public MyRunnable(int[] range) {
			this.range = range;
		}

		@Override
		public void run() {
			int max = findMax(range[0], range[1]);
			this.output = max;
		}

		public int getOutput() {
			return this.output;
		}
	}

	public void printMax() {
		try {
			MyRunnable task1 = new MyRunnable(new int[]{0, 29});
			MyRunnable task2 = new MyRunnable(new int[]{30, 59});
			MyRunnable task3 = new MyRunnable(new int[]{60, 89});
			Thread t1 = new Thread(task1);
			Thread t2 = new Thread(task2);
			Thread t3 = new Thread(task3);
			t1.start();
			t2.start();
			t3.start();
			t1.join();
			t2.join();
			t3.join();
			int o1 = task1.getOutput();
			int o2 = task2.getOutput();
			int o3 = task3.getOutput();
			int max = o1 > o2 ? (o1 > o3 ? o1 : o3) : (o2 > o3 ? o2: o3);
			System.out.println("the max value is " + max);
		} catch (Exception ex) {

		}
	}

	/**
	 * returns the max value in the array within a give range [begin,range]
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	private int findMax(int begin, int end) {
		// you should NOT change this function
		int max = array[begin];
		for (int i = begin + 1; i <= end; i++) {
			if (array[i] > max) {
				max = array[i];
			}
		}
		return max;
	}
}

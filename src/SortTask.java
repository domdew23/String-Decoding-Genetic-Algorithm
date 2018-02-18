import java.util.concurrent.RecursiveAction;
import java.util.Arrays;

public class SortTask extends RecursiveAction {
	private int lo,hi;
	private Person[] array;
	private Person[] mergedArrary;

	public SortTask(Person[] array, int lo, int hi){
		this.array = array;
		this.lo = lo;
		this.hi = hi;
		this.mergedArrary = new Person[hi];
	}

	public void compute(){
		if ((hi - lo) <= Settings.THRESHOLD){
			Arrays.sort(array, lo, hi);
		} else {
			int mid = (hi + lo) / 2;
			SortTask left = new SortTask(array, lo, mid);
			SortTask right = new SortTask(array, mid, hi);
			invokeAll(left, right);
			merge(lo, mid, hi);
		}
	}

	private void merge(int lo, int mid, int hi){
		/* copy your section into "mergedArray" */
		for (int i = lo; i < hi; i++){
			mergedArrary[i] = array[i];
		}

		int i = lo;
		int j = mid;
		int k = lo;

		while (i < mid && j < hi){
			if (mergedArrary[i].compareTo(mergedArrary[j]) == -1){
				array[k] = mergedArrary[i++];
			} else {
				array[k] = mergedArrary[j++];
			}
			k++;
		}

		while (i < mid){
			array[k++] = mergedArrary[i++];
		}

		while (j < hi){
			array[k++] = mergedArrary[j++];
		}
	}
}
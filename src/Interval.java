/////////////////////////////////////////////////////////////////////////////
// Semester:         CS367 Spring 2017
// PROJECT:          p4
// FILE:             Interval
//
// TEAM:    Team 61a
// Author1: McKinley Sconiers-Hasan, msconiershas@wisc.edu, 9071244371, lec2
// Author2: Grant Darin, gdarin@wisc.edu, 9072590921, lec2
// 
//////////////////////////// 80 columns wide //////////////////////////////////
public class Interval<T extends Comparable<T>> implements IntervalADT<T> {

	//declared data members
	private T start;
	private T end;
	private String label;

	/** Interval constructor*/
	public Interval(T start, T end, String label) {
		this.start = start;
		this.end = end;
		this.label = label;
	}

	/** Returns the start value (must be Comparable<T>) of the interval. */
	public T getStart() {
		return this.start;
	}

	/** Returns the end value (must be Comparable<T>) of the interval. */
	public T getEnd() {
		return this.end;
	}

	/** Returns the label for the interval. */
	public String getLabel() {
		return this.label;
	}

	/** Returns a boolean depending on if the starts and ends of respective
	 *  intervals overlap with each other.
	 */
	public boolean overlaps(IntervalADT<T> other) {
		int check = other.getStart().compareTo(this.end);
		int m = other.getEnd().compareTo(this.start);
		if(m < 0 || check < 0) {
			return false; //false if intervals don't overlap
		}
		return true; ///true if intervals overlap
	}

	/** Returns a boolean depending on if point is within the interval.
	 */
	public boolean contains(T point) {
		int a = point.compareTo(this.start);
		int b = point.compareTo(this.end);
		if(a < 0 || b > 0) {
			return false; //false if point is not in interval
		}
		return true; //true if point is in interval
	}

	/** Returns result of compareTo for b or c depending on the starting points
	 *  of intervals "this" and "other"
	 */
	public int compareTo(IntervalADT<T> other) {
		int b = this.start.compareTo(other.getStart());
		int c = 0;
		if(b == 0) {
			c = this.end.compareTo(other.getEnd());
			return c; //if intervals "this" and "other" have the same
					  //starting point, returns result of comparing their ends
		}
		return b; //returns if "this" and "other" start at different points
	}
	/**
	 * Returns a specific string representation of the interval. It must return
	 * the interval in this form.
	 * 
	 *  <p>For example: If the interval's label is p1 and the start is 24 and 
	 *  the end is 45, then the string returned is:</p>
	 *  
	 *  <pre>p1 [24, 45]</pre>
	 */
	@Override
	public String toString() {

		return this.label + " [" + this.start + "," + this.end + "] " ;
	}
}

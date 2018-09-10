
public class Interval<T extends Comparable<T>> implements IntervalADT<T> {

    // TODO declare any needed data members
	private T start;
	private T end;
	private String label;

    public Interval(T start, T end, String label) {
        // TODO Auto-generated constructor stub
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

    public boolean overlaps(IntervalADT<T> other) {
        // TODO Auto-generated method stub
    	int check = other.getStart().compareTo(this.end);
    	int m = other.getEnd().compareTo(this.start);
    	if(m < 0 || check < 0) {
    		return false;
    	}
    	return true;
    }

    public boolean contains(T point) {
        // TODO Auto-generated method stub
    	int a = point.compareTo(this.start);
    	int b = point.compareTo(this.end);
    	if(a < 0 || b > 0) {
    		return false;
    	}
    	return true;
    }

   
    public int compareTo(IntervalADT<T> other) {
        // TODO Auto-generated method stub
    	int b = this.start.compareTo(other.getStart());
    	int c = 0;
    	if(b == 0) {
    		c = this.end.compareTo(other.getEnd());
    		return c;
    	}
    	return b;
    }
	/**
	 * Returns a specific string representation of the interval. It must return
	 * the interval in this form.
	 * 
	 *  <p>For example: If the interval's label is p1 and the start is 24 and the end is 45,
	 *  then the string returned is:</p>
	 *  
	 *  <pre>p1 [24, 45]</pre>
	 */
	@Override
	public String toString() {
	
		return this.label + " [" + this.start + "," + this.end + "] " ;
	}

}

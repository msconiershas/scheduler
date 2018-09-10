/////////////////////////////////////////////////////////////////////////////
// Semester:         CS367 Spring 2017
// PROJECT:          p4
// FILE:             IntervalTree
//
// TEAM:    Team 61a
// Author1: McKinley Sconiers-Hasan, msconiershas@wisc.edu, 9071244371, lec2
// Author2: Grant Darin, gdarin@wisc.edu, 9072590921, lec2
// 
//////////////////////////// 80 columns wide //////////////////////////////////
import java.util.ArrayList;
import java.util.List;

public class IntervalTree<T extends Comparable<T>> implements 
IntervalTreeADT<T> {

	//declared data member
	private IntervalNode<T> root = null;

	/** Returns the root node of this IntervalTree. */
	public IntervalNode<T> getRoot() {
		return this.root;
	}

	/**
	 * Inserts an <code>Interval</code> in the tree.
	 * 
	 * <p>Each <code>Interval</code> is stored as the data item of an
	 * <code>IntervalNode</code>.  The position of the new IntervalNode 
	 * will be the position found using the binary search algorithm.
	 * This is the same algorithm presented in BST readings and lecture
	 * examples. 
	 * 
	 * <p>Tip: Call a recursive helper function with root node.
	 * In that call, traverse the tree using the binary search algorithm.
	 * Use the comparator defined in <code>Interval</code> and create a new
	 * IntervalNode to store the new <i>interval</i> item when you reach 
	 * the end of the tree.</p>
	 * 
	 * <p>This method must also check and possibly update the maxEnd 
	 * in the IntervalNode. Recall, that <b>maxEnd</b> of a node represents 
	 * the maximum end of current node and all descendant nodes.</p>
	 * 
	 * <p>Note: the key for comparison here will be the compareTo method defined
	 *  in interval class. You will use this for the interval stored in the 
	 *  node to compare it with the input interval.s</p>
	 * 
	 * <p>If the start and end of the given interval match an existing 
	 * interval, throw an IllegalArgumentException.</p>
	 *  
	 * @param interval the interval (item) to insert in the tree.
	 * @throws IllegalArgumentException if interval is null or is found 
	 * to be a duplicate of an existing interval in this tree.            
	 */
	public void insert(IntervalADT<T> interval)
			throws IllegalArgumentException {
		if(interval == null) {
			throw new IllegalArgumentException();
		}
		if(this.root == null) { //if there is no root yet, inserted interval
			//will become new root
			root = new IntervalNode<T>(interval);
			root.setMaxEnd(root.getInterval().getEnd());
			root.setInterval(interval);
			return;
		}
		else 
			this.insert(interval, this.root); //if there is a root, use BST
		//to find position for new interval
	}
	
	/** Companion method for insert 
	 * Recursively traverses the tree to insert node
	 * */
	private void insert(IntervalADT<T> interval, IntervalNode<T> root) {

		//if the start in the root node is larger than the start of the 
		//incoming interval
		if(root.getInterval().getStart().compareTo(interval.getStart()) > 0) {
			if(root.getLeftNode() != null) {
				//if the left node does not equal null, go left
				insert(interval, root.getLeftNode());//go left
			}
			else {//if the left node is null, make new node and add it as 
				//the left node
				root.setLeftNode(new IntervalNode<T>(interval));
			}
		}
		//if start in the root node is the same as start of new interval, 
		//this case will check the left node
		else if(root.getInterval().getStart().compareTo(interval.getStart()) 
				== 0) {
			if(root.getInterval().getEnd().compareTo(interval.getEnd()) > 0) {
				if(root.getLeftNode() != null) {//if the left node does not 
					//equal null, go left
					insert(interval, root.getLeftNode());//go left
				}
				else {//if the left node is null, make new node and add it 
					//as the left node
					root.setLeftNode(new IntervalNode<T>(interval));
				}
			}
			else if(root.getInterval().getEnd().compareTo(interval.getEnd()) 
					== 0) {
				throw new IllegalArgumentException();//both intervals are same
			}
		}

		//if the start in the root node is smaller than the start of the 
		//incoming interval
		if(root.getInterval().getStart().compareTo(interval.getStart()) 
				< 0) {
			//if right node is not null, go right
			if(root.getRightNode() != null) {
				insert(interval, root.getRightNode());//go right
			}
			//if the right node is null, set the right node to the new interval
			else {
				root.setRightNode(new IntervalNode<T>(interval));
			}
		}
		//if start in the root node is the same as start of new interval, 
		//this case will check the right node
		else if(root.getInterval().getStart().compareTo(interval.getStart()) 
				== 0) {
			if(root.getInterval().getEnd().compareTo(interval.getEnd()) < 0) {
				//if right node is not null, go right
				if(root.getRightNode() != null) {
					insert(interval, root.getRightNode());//go right
				}
				//if the right node is null, set right node to new interval
				else {
					root.setRightNode(new IntervalNode<T>(interval));
				}
			}
			//exception thrown if end of root is equal to end of new interval
			else if(root.getInterval().getEnd().compareTo(interval.getEnd()) 
					== 0) {
				throw new IllegalArgumentException();
			}
		}
		root.setMaxEnd(recalculateMaxEnd(root));//update MaxEnd
	}

	/**
	 * Delete the node containing the specified interval in the tree.
	 * Delete operations must also update the maxEnd of interval nodes
	 * that change as a result of deletion.  
	 *  
	 * <p>Tip: call <code>deleteHelper(root)</code> with the root node.</p>
	 * 
	 * @throws IllegalArgumentException if interval is null
	 * @throws IntervalNotFoundException if the interval does not exist.
	 */
	public void delete(IntervalADT<T> interval)
			throws IntervalNotFoundException, IllegalArgumentException {
		deleteHelper(this.root, interval);
	}

	/** 
	 * Recursive helper method for the delete operation.  
	 * 
	 * <p>Note: the maxEnd of some interval nodes may also need to change
	 * as a result of an interval's deletion.</p>
	 * 
	 * <p>Note: the key for comparison here is the start of the interval
	 * stored at each IntervalNode.</p>
	 * 
	 * <p>Tip: write a non-recursive helper method that recalculates maxEnd for 
	 * any node based on the maxEnd of its child nodes</p>
	 * 
	 * <pre>      private T recalculateMaxEnd(IntervalNode&lt;T&gt; 
	 * nodeToRecalculate)</pre>
	 * 
	 * <h3>Pseudo-code for this deleteHelper method:</h3>
	 *
	 * <ul>
	 * <li>If node is null, throw IntervalNotFoundException</li>
	 * <li>If interval is found in this node, delete it and replace it 
	 * with leftMost in right subtree.  There are two cases:
	 * 
	 * <ol><li>If right child exists
	 *     <ol><li>Replace the node's interval with the in-order successor 
	 *     interval. 
	 *     <br />Tip: Be sure to code the and use the 
	 *     <code>getSuccessor</code> method for <code>IntervalNode</code> 
	 *     class.</li>
	 *         <li>Call deleteHelper() on the in-order successor node of the
	 *          right subtree.</li>
	 *         <li>Update the new maxEnd.</li>
	 *         <li>Return the node.</li>
	 *     </ol>
	 *     </li>
	 *     
	 *     <li>If right child doesn't exist, return the left child</li>
	 * </ol>
	 * 
	 * <li>If interval is in the right subtree,
	 *      <ol>
	 *	    <li>Set right child to result of calling deleteHelper on 
	 *		right child.</li>
	 *	    <li>Update the maxEnd if necessary. </li>
	 *      <li>Return the node.</li>
	 *      </ol>
	 *      </li>
	 *
	 * <li>If interval is in the left subtree.
	 *      <ol>
	 *	    <li>Set left child to result of calling deleteHelper on left child.
	 *		</li>
	 *	    <li>Update the maxEnd if necessary. </li>
	 *      <li>Return the node.</li>
	 *      </ol>
	 *      </li>
	 *  </ul>
	 *
	 * @param node the interval node that is currently being checked.
	 * 
	 * @param interval the interval to delete.
	 * 
	 * @throws IllegalArgumentException if the interval is null.
	 * 
	 * @throws IntervalNotFoundException
	 *             if the interval is not null, but is not found in the tree.
	 * 
	 * @return Root of the tree after deleting the specified interval.
	 */
	public IntervalNode<T> deleteHelper(IntervalNode<T> node,
			IntervalADT<T> interval)
				throws IntervalNotFoundException, IllegalArgumentException {
		//If node is null, throw IntervalNotFoundException
		//If interval is found, delete and replace with in-order successor
		if(node == null) {
			throw new IllegalArgumentException();
		}
		if(this.contains(interval) == false) {//if interval not in the tree
			throw new IntervalNotFoundException(interval.toString());
		}

		//if node is the interval to delete
		if(node.getInterval().compareTo(interval) == 0) {

			//if right node of of target interval is not empty, must 
			//find successor
			if(node.getRightNode() != null) {
				IntervalNode<T> successor = node.getSuccessor();
				node.setInterval(successor.getInterval()); //set target node
				//to it's successor in order to delete
				IntervalNode<T> x = deleteHelper(node.getRightNode(), 
						successor.getInterval());
				node.setRightNode(x);
				//update maxEnd
				node.setMaxEnd(recalculateMaxEnd(node));
				return node;
			}
			else if(node.getRightNode() == null) {
				return node.getLeftNode();
			}
		}
		//if node comes before the position of the interval to delete
		else if (node.getInterval().compareTo(interval) < 0) {
			//Set right child to result of calling deleteHelper on right child.
			node.setRightNode(deleteHelper(node.getRightNode(), interval));
			//Update the maxEnd if necessary
			node.setMaxEnd(recalculateMaxEnd(node));
			//Return the node
			return node;
		}
		//if nodes comes after the position of the interval to delete
		else {
			//Set left child to result of calling deleteHelper on left child.
			node.setLeftNode(deleteHelper(node.getLeftNode(), interval));
			//Update the maxEnd if necessary
			node.setMaxEnd(recalculateMaxEnd(node));
			//Return the node
			return node;
		}
		return this.root;
	}

	/**Recalculates and returns maximum end present in the subtree rooted 
	 * at "node". Helps reduce the search complexity of the program.
	 * @param node : the root of the tree being tested
	 * @return maxEnd : maximum end present in the subtree rooted at "node"
	 */
	private T recalculateMaxEnd(IntervalNode<T> node) {
		T maxEnd = node.getMaxEnd();
		//if node has a right node but not a left node
		if(node.getRightNode() != null && node.getLeftNode() == null) {
			if(node.getMaxEnd().compareTo(node.getRightNode().getMaxEnd()) 
					< 0) {
				maxEnd = node.getRightNode().getMaxEnd(); //return maxEnd
				//of right node
			}
		}
		//if node has a left node but not a right node
		else if(node.getLeftNode() != null && node.getRightNode() == null) {
			if(node.getMaxEnd().compareTo(node.getLeftNode().getMaxEnd()) 
					< 0) {
				maxEnd = node.getLeftNode().getMaxEnd(); //return maxEnd
				//of left node
			}
		}
		//if node has both left and right nodes
		else if(node.getLeftNode() != null && node.getRightNode() != null) {
			//if maxEnd of node is less than maxEnd of its children
			if(node.getMaxEnd().compareTo(node.getRightNode().getMaxEnd()) 
					< 0 || 
					node.getMaxEnd().compareTo(node.getLeftNode().getMaxEnd())
					< 0) {
				if(node.getLeftNode().getMaxEnd().compareTo
						(node.getRightNode().getMaxEnd())
						< 0) {
					maxEnd = node.getRightNode().getMaxEnd(); //return maxEnd
					//of rightNode is it is greater than left node's
				}
				else if(node.getLeftNode().getMaxEnd().
						compareTo(node.getRightNode().getMaxEnd())
						> 0) {
					maxEnd = node.getLeftNode().getMaxEnd(); //return maxEnd
					//of leftNode is it is greater than right node's
				}
				else 
					maxEnd = node.getRightNode().getMaxEnd();
			} 
		}
		return maxEnd; //if node has no children, return its maxEnd
	}

	/**
	 * Find and return a list of all intervals that overlap with the 
	 * given interval. 
	 * 
	 * <p>Tip: Define a helper method for the recursive call and call it 
	 * with root, the interval, and an empty list.  
	 * Then, return the list that has been built.</p>
	 * 
	 * <pre>   private void findOverlappingHelper(IntervalNode node, 
	 * IntervalADT interval, List<IntervalADT<T>> result)</pre>
	 * 
	 * <p>Pseudo-code for such a recursive findingOverlappingHelper method.</p>
	 * 
	 * <ol>
	 * <li>if node is null, return</li>
	 * <li>if node interval overlaps with the given input interval, 
	 * add it to the result.</li>
	 * <li>if left subtree's max is greater than the interval's start, 
	 * call findOverlappingHelper in the left subtree.</li>
	 * <li>if right subtree's max is greater than the interval's start, 
	 * call call findOverlappingHelper in the rightSubtree.</li>
	 * </ol>
	 *  
	 * @param interval the interval to search for overlapping
	 * 
	 * @return list of intervals that overlap with the input interval.
	 */
	public List<IntervalADT<T>> findOverlapping(
			IntervalADT<T> interval) {
		List<IntervalADT<T>> list = new ArrayList<IntervalADT<T>>();
		findOverlappingHelper(this.root, interval, list);
		return list;
	}
	/**Companion method for findOverlapping */
	private void findOverlappingHelper(IntervalNode<T> node, IntervalADT<T> 
	interval, List<IntervalADT<T>> result) {
		if(node == null) {
			return;
		}
		//if node start if less than or equal to interval start and node end
		//is greater than or equal to interval start
		if(node.getInterval().getStart().compareTo(interval.getStart()) <= 0 && 
				node.getInterval().getEnd().compareTo(interval.getStart())
				>= 0) {
			result.add(node.getInterval());//add interval to list
		}
		//if node start is less than or equal to interval end and node end is
		//greater than or equal to interval end
		else if(node.getInterval().getStart().compareTo(interval.getEnd())
				<= 0 && 
				node.getInterval().getEnd().compareTo(interval.getEnd()) >= 0) {
			result.add(node.getInterval());//add interval to list
		}
		//if node start and end are both within or equal to range of interval
		else if(node.getInterval().getStart().compareTo(interval.getStart()) 
				>= 0 &&
				node.getInterval().getEnd().compareTo(interval.getEnd()) <= 0) {
			result.add(node.getInterval());//add interval to list
		}
		//pass in left node and then right node as new node parameter
		findOverlappingHelper(node.getLeftNode(), interval, result);
		findOverlappingHelper(node.getRightNode(), interval, result);
	}


	/**
	 * Search and return a list of all intervals containing a given point. 
	 * This method may return an empty list. 
	 * 
	 * <p>For example: if the intervals stored in the tree are:</p>
	 * <pre>
	 * p1 [5, 10]
	 * p2 [2, 18]
	 * p3 [12, 30]</pre>
	 * 
	 * <p>and the input point is 16, it will return a list containing
	 *  the intervals:</p>
	 * <pre>
	 * p2 [2, 18]
	 * p3 [12, 30]</pre>
	 * 
	 * @throws IllegalArgumentException if point is null
	 * 
	 * @param point
	 *            input point to search for.
	 * @return List of intervals containing the point.
	 */
	public List<IntervalADT<T>> searchPoint(T point) {
		List<IntervalADT<T>> list = new ArrayList<IntervalADT<T>>();
		
		//if point is within start and end of interval
		if(root.getInterval().getStart().compareTo(point) <= 0 &&
				root.getInterval().getEnd().compareTo(point) >= 0) {
			list.add(root.getInterval()); //add interval to list
		}
		List<IntervalADT<T>> answer = searchPoint(root, point, list);

		return answer;
	}
	/** Companion method of searchPoint.
	 * Recursively searches the tree for the point.*/
	private List<IntervalADT<T>> searchPoint(IntervalNode<T> root, 
			T point, List<IntervalADT<T>> list) {
		if(root == null) {
			return list;
		}
		IntervalNode<T> right = root.getRightNode();
		IntervalNode<T> left = root.getLeftNode();
		
		//if left node is not empty, compare its start and end to those of 
		//the point and add it to list if point is within range
		if(left != null) {
			if(left.getInterval().getStart().compareTo(point) <= 0 && 
					left.getInterval().getEnd().compareTo(point) >= 0) {
				list.add(left.getInterval()); //add interval to list
			}
			searchPoint(left, point, list);
		}
		//if right node is not empty, compare its start and end to those of 
		//the point and add it to list if point is within range
		if(right != null) {
			if(right.getInterval().getStart().compareTo(point) <= 0 && 
					right.getInterval().getEnd().compareTo(point) >= 0) {
				list.add(right.getInterval()); //add interval to list
			}
			searchPoint(right, point, list);
		}
		return list;
	}

	/**
	 * Get the size of the interval tree. The size is the total number of
	 * nodes present in the tree. 
	 * 
	 * <p>Tip: Define and call a recursive helper function to 
	 * calculate this.</p>
	 * 
	 * @return int number of nodes in the tree.
	 */
	public int getSize() {
		return getSize(root);
	}
	/**Companion method for getSize */
	private int getSize(IntervalNode<T> root) {
		if (root == null) {
			return 0;
		}
		//if root is not empty, add 1 to size and run getSize with left
		//and right nodes of root using same approach
		else { 
			return 1 + getSize(root.getLeftNode()) + 
					getSize(root.getRightNode()); 
		} 
	}
	/**
	 * Return the height of the interval tree at the root of the tree. 
	 * 
	 * <p>Tip: Define and call a recursive helper function to calculate this 
	 * for a given node.</p>
	 * 
	 * @return the height of the interval tree
	 */
	public int getHeight() {
		return getHeight(root);
	}
	/**Companion method for getHeight */
	private int getHeight(IntervalNode<T> interval) {
		if (interval == null) {
			return 0;
		}
		//if interval is not empty, add 1 to the height and then keep running
		//getHeight for children until null, and then add the larger height
		//between the left and right node
		else {
			return 1 +  Math.max(getHeight(interval.getLeftNode()), 
					getHeight(interval.getRightNode()));
		}
	}

	/**
	 * Returns true if the tree contains an exact match for the start and 
	 * end of the given interval.
	 * The label is not considered for this operation.
	 *  
	 * <p>Tip: Define and call a recursive helper function to call with 
	 * root node and the target interval.</p>
	 * 
	 * @param interval
	 * 				target interval for which to search the tree for. 
	 * @return boolean 
	 * 			   	representing if the tree contains the interval.
	 *
	 * @throws IllegalArgumentException
	 *             	if interval is null.
	 * 
	 */
	public boolean contains(IntervalADT<T> interval) {
		if(interval == null) {
			throw new IllegalArgumentException();
		}
		return contains(interval, root);
	}
	/**Companion method for contains.
	 * Recursively find an interval.
	 */
	private boolean contains(IntervalADT<T> interval, IntervalNode<T> root) {
		if(root == null) {
			return false;
		}
		//if the incoming node == the root
		if(interval.compareTo(root.getInterval()) == 0) {
			return true;
		}
		//if the root is smaller than the incoming node
		if(root.getInterval().compareTo(interval) < 0) {
			//check the right subtree
			return contains(interval, root.getRightNode());
		}
		//if the root is larger than the incoming node
		else if(root.getInterval().compareTo(interval) > 0) {
			//check the left subtree
			return contains(interval, root.getLeftNode());
		}
		return false;
	}

	/**
	 * Print the statistics of the tree in the below format
	 * <pre>
	 *	-----------------------------------------
	 *	Height: 2
	 *	Size: 3
	 *	-----------------------------------------
	 *	</pre> 
	 */
	public void printStats() {
		System.out.println("-----------------------------------------");
		System.out.println("Height: " + this.getHeight());
		System.out.println("Size: " + this.getSize());
		System.out.println("-----------------------------------------");
	}
}

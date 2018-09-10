import java.util.ArrayList;
import java.util.List;

public class IntervalTree<T extends Comparable<T>> implements IntervalTreeADT<T> {
	
	private IntervalNode<T> root = null; 


	/** Returns the root node of this IntervalTree. */
	public IntervalNode<T> getRoot() {
		return this.root;
	}


	/**
	 * @param interval the interval (item) to insert in the tree.
	 * @throws IllegalArgumentException if interval is null or is found 
	 * to be a duplicate of an existing interval in this tree.            
	 */
	public void insert(IntervalADT<T> interval)
					throws IllegalArgumentException {
		if(interval == null) {
			throw new IllegalArgumentException();
		}
		if(this.root == null) {//if there is not root yet
			root = new IntervalNode<T>(interval);
			root.setMaxEnd(root.getInterval().getEnd());
			root.setInterval(interval);
			return;//done 
		}
		else 
			this.insert(interval, this.root);
	}
	private void insert(IntervalADT<T> interval, IntervalNode<T> root) {

			//if the start in the root node is larger than the start of the incoming interval
			if(root.getInterval().getStart().compareTo(interval.getStart()) > 0) {
				if(root.getLeftNode() != null) {//if the left node does not equal null, go left
				insert(interval, root.getLeftNode());//go left
				}
				else {//if the left node is null, make new node and add it as the left node
					IntervalNode<T> temp = new IntervalNode<T>(interval);
					//temp.setMaxEnd(recalculateMaxEnd(temp));
					System.out.println(interval.toString());
					root.setLeftNode(temp);
				}
			}
			else if(root.getInterval().getStart().compareTo(interval.getStart()) == 0) {
				if(root.getInterval().getEnd().compareTo(interval.getEnd()) > 0) {
					if(root.getLeftNode() != null) {//if the left node does not equal null, go left
					insert(interval, root.getLeftNode());//go left
					}
					else {//if the left node is null, make new node and add it as the left node
						IntervalNode<T> temp = new IntervalNode<T>(interval);
						//temp.setMaxEnd(recalculateMaxEnd(temp));
						System.out.println(interval.toString());
						root.setLeftNode(temp);
					}
				}
				else if(root.getInterval().getEnd().compareTo(interval.getEnd()) == 0) {
					throw new IllegalArgumentException();//both intervals are the same
				}
			}
			
			//if the start in the root node is smaller than the start of the incoming interval
			if(root.getInterval().getStart().compareTo(interval.getStart()) < 0) {
				if(root.getRightNode() != null) {//if the right node is not null, go right
				insert(interval, root.getRightNode());//go right
				}
				else {//if the right node is null, set the right node to the new interval
					IntervalNode<T> temp = new IntervalNode<T>(interval);
					//temp.setMaxEnd(recalculateMaxEnd(temp));
					System.out.println(interval.toString());
					root.setRightNode(temp);
				}
			}
			else if(root.getInterval().getStart().compareTo(interval.getStart()) == 0) {
				if(root.getInterval().getEnd().compareTo(interval.getEnd()) < 0) {
					if(root.getRightNode() != null) {//if the right node is not null, go right
						insert(interval, root.getRightNode());//go right
					}
					else {//if the right node is null, set the right node to the new interval
						IntervalNode<T> temp = new IntervalNode<T>(interval);
						//temp.setMaxEnd(recalculateMaxEnd(temp));
						System.out.println(interval.toString());
						root.setRightNode(temp);
					}
				}
				else if(root.getInterval().getEnd().compareTo(interval.getEnd()) == 0) {
					throw new IllegalArgumentException();
				}
			}
			T a = recalculateMaxEnd(root);
			System.out.println("MaxEnd for "+ root.getInterval().toString() +" "+ a.toString());
			root.setMaxEnd(a);
	}

	
	/**
	 * Delete the node containing the specified interval in the tree.
	 * Delete operations must also update the maxEnd of interval nodes
	 * that change as a result of deletion.  
	 *  
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
		// TODO Auto-generated method stub
		//If node is null, throw IntervalNotFoundException
	 	// If interval is found in this node, delete it and replace it 
	 	// with leftMost in right subtree.
		if(node == null) {
			throw new IllegalArgumentException();
		}
		if(this.contains(interval) == false) {//if the interval is not in the tree
			throw new IntervalNotFoundException(interval.toString());
		}
		
		System.out.println("node " + node.getInterval().toString());
		
		
		if(node.getInterval().compareTo(interval) == 0) {
			System.out.println("match: " + node.getInterval().toString() + " " + interval.toString());
			
			if(node.getRightNode() != null) {
				IntervalNode<T> successor = node.getSuccessor();
				System.out.println("successor " + successor.getInterval().toString());
				node.setInterval(successor.getInterval());
				//deleteHelper(successor, successor.getInterval());
				IntervalNode<T> x = deleteHelper(node.getRightNode(), successor.getInterval());
				node.setRightNode(x);
				//node = deleteHelper(successor, successor.getInterval());
				//update maxEnd
				node.setMaxEnd(recalculateMaxEnd(node));
				return node;
			}
			else if(node.getRightNode() == null) {
				return node.getLeftNode();
			}
		}
		else if (node.getInterval().compareTo(interval) < 0) {
			System.out.println("go right");
			System.out.println("node we are setting:" + node.getInterval().toString());
			//Set right child to result of calling deleteHelper on right child.
			node.setRightNode(deleteHelper(node.getRightNode(), interval));
			//Update the maxEnd if necessary
			node.setMaxEnd(recalculateMaxEnd(node));
			//Return the node
             return node;
         }
        else {
        	System.out.println("go left");
        	System.out.println("node we are setting:" + node.getInterval().toString());
        	//Set left child to result of calling deleteHelper on left child.
        	node.setLeftNode(deleteHelper(node.getLeftNode(), interval));
        	//Update the maxEnd if necessary
        	node.setMaxEnd(recalculateMaxEnd(node));
        	//Return the node
             return node;
         }
		
		
		//update maxEnd
		return this.root;
	}
	
	private T recalculateMaxEnd(IntervalNode<T> node) {
		T maxEnd = node.getMaxEnd();
		if(node.getRightNode() != null && node.getLeftNode() == null) {
			if(node.getMaxEnd().compareTo(node.getRightNode().getMaxEnd()) < 0) {
				maxEnd = node.getRightNode().getMaxEnd();
			}
		}
		else if(node.getLeftNode() != null && node.getRightNode() == null) {
			if(node.getMaxEnd().compareTo(node.getLeftNode().getMaxEnd()) < 0) {
				maxEnd = node.getLeftNode().getMaxEnd();
			}
		}
		else if(node.getLeftNode() != null && node.getRightNode() != null) {
			if(node.getMaxEnd().compareTo(node.getRightNode().getMaxEnd()) < 0 || 
					node.getMaxEnd().compareTo(node.getLeftNode().getMaxEnd()) < 0) {
					if(node.getLeftNode().getMaxEnd().compareTo(node.getRightNode().getMaxEnd())
							< 0) {
						maxEnd = node.getRightNode().getMaxEnd();
							}
					else if(node.getLeftNode().getMaxEnd().compareTo(node.getRightNode().getMaxEnd())
							> 0) {
						maxEnd = node.getLeftNode().getMaxEnd();
					}
					else 
						maxEnd = node.getRightNode().getMaxEnd();
				
			}
			}
		 return maxEnd;
	 }

	/**
	 * Find and return a list of all intervals that overlap with the given interval. 
	 * 
	 * @param interval the interval to search for overlapping
	 * 
	 * @return list of intervals that overlap with the input interval.
	 */
	public List<IntervalADT<T>> findOverlapping(
					IntervalADT<T> interval) {
		// TODO Auto-generated method stub
		List<IntervalADT<T>> list = new ArrayList<IntervalADT<T>>();
		findOverlappingHelper(this.root, interval, list);
		return list;
	}
	private void findOverlappingHelper(IntervalNode<T> node, IntervalADT<T> interval, List<IntervalADT<T>> result) {
		if(node == null) {
			return;
		}
		if(node.getInterval().getStart().compareTo(interval.getStart()) <= 0 && 
				node.getInterval().getEnd().compareTo(interval.getStart()) >= 0) {
			result.add(node.getInterval());//add interval to list
		}
		else if(node.getInterval().getStart().compareTo(interval.getEnd()) <= 0 && 
				node.getInterval().getEnd().compareTo(interval.getEnd()) >= 0) {
			result.add(node.getInterval());
		}
		else if(node.getInterval().getStart().compareTo(interval.getStart()) >= 0 &&
				node.getInterval().getEnd().compareTo(interval.getEnd()) <= 0) {
			result.add(node.getInterval());
		}
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
	 * <p>and the input point is 16, it will return a list containing the intervals:</p>
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
		// TODO Auto-generated method stub
		List<IntervalADT<T>> list = new ArrayList<IntervalADT<T>>();
		
		if(root.getInterval().getStart().compareTo(point) <= 0 &&
				root.getInterval().getEnd().compareTo(point) >= 0) {
			list.add(root.getInterval());
		}
		List<IntervalADT<T>> answer = searchPoint(root, point, list);
		
		return answer;
	}
	private List<IntervalADT<T>> searchPoint(IntervalNode<T> root, T point, List<IntervalADT<T>> list) {
		if(root == null) {
			return list;
		}
		IntervalNode<T> right = root.getRightNode();
		IntervalNode<T> left = root.getLeftNode();
		
		if(left != null) {
			if(left.getInterval().getStart().compareTo(point) <= 0 && 
					left.getInterval().getEnd().compareTo(point) >= 0) {
				list.add(left.getInterval());
			}
			searchPoint(left, point, list);
		}
		if(right != null) {
			if(right.getInterval().getStart().compareTo(point) <= 0 && 
					right.getInterval().getEnd().compareTo(point) >= 0) {
				list.add(right.getInterval());
			}
			searchPoint(right, point, list);
		}
		return list;
		
	}

	/**
	 * Get the size of the interval tree. The size is the total number of
	 * nodes present in the tree. 
	 * 	 * 
	 * @return int number of nodes in the tree.
	 */
	public int getSize() {
		// TODO Auto-generated method stub
		return getSize(root);
	}
	private int getSize(IntervalNode<T> root) {
		  if (root == null) {
			  return 0;
		  }
		  else { 
		    return 1 + getSize(root.getLeftNode()) + getSize(root.getRightNode()); 
		  } 
	}
	/**
	 * Return the height of the interval tree at the root of the tree. 
	 * 
	 * @return the height of the interval tree
	 */
	public int getHeight() {
		// TODO Auto-generated method stub
		return getHeight(root);
	}
	private int getHeight(IntervalNode<T> interval) {
		
		 if (interval == null) {
		        return 0;
		    }
		 else {
		        return 1 +  Math.max(getHeight(interval.getLeftNode()), getHeight(interval.getRightNode()));
		    }
	}

	/**
	 * Returns true if the tree contains an exact match for the start and end of the given interval.
	 * The label is not considered for this operation.
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
		// TODO Auto-generated method stub
		if(interval == null) {
			throw new IllegalArgumentException();
		}
		return contains(interval, root);
	}
	private boolean contains(IntervalADT<T> interval, IntervalNode<T> root) {
		if(root == null) {
			return false;
		}
		
		if(interval.compareTo(root.getInterval()) == 0) {//if the incoming node == the root
			return true;
		}
		if(root.getInterval().compareTo(interval) < 0) {//if the root is smaller than the incoming node
			return contains(interval, root.getRightNode());//check the right subtree
		}
		else if(root.getInterval().compareTo(interval) > 0) {
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
		// TODO Auto-generated method stub
		System.out.println("-----------------------------------------");
		System.out.println("Height: " + this.getHeight());
		System.out.println("Size: " + this.getSize());
		System.out.println("-----------------------------------------");

	}
	}



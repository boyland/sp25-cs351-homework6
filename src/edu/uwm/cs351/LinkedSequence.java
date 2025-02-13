package edu.uwm.cs351;


import java.util.function.Consumer;

// This is a Homework Assignment for CS 351 at UWM

/**
 * A cyclic doubly-linked list implementation of the Java Collection interface.
 * The iterators returned are {@link Addable} and strictly fail fast.
 * @param E element type of the collection
 */
public class LinkedSequence<E> // TODO: implements what?
    implements Cloneable // ### \subsection{Implements}
{
	private static Consumer<String> reporter = (s) -> System.out.println("Invariant error: "+ s);
	
	/**
	 * Used to report an error found when checking the invariant.
	 * By providing a string, this will help debugging the class if the invariant should fail.
	 * @param error string to print to report the exact error found
	 * @return false always
	 */
	private boolean report(String error) {
		reporter.accept(error);
		return false;
	}

	// #(# \subsection{Node Class}
	private static class Node<T> {
		T data;
		Node<T> next, prev;

		@SuppressWarnings("unchecked")
		public Node() {
			next = prev = this;
			data = (T)this;
		}

		public Node(T data, Node<T> prev, Node<T> next) {
			this.data = data;
			this.prev = prev;
			this.next = next;
		}
	}
	//#)
	// TODO: Declare the private static generic Node class with fields data, prev and next.
	// The class should be private, static and generic.
	// Please use a different name for its generic type parameter Node<> <--
	// It should have a constructor or two (at least the default constructor) but no methods.
	// The no-argument constructor can construct a dummy node if you would like.
	// The fields of Node should have "default" access (neither public, nor private)
	// Remember the dummy node should have a type-cast reference to itself for its data
	// So we should have dummy.data == dummy.
	// You are allowed @SuppressWarnings("unchecked") on the default constructor.

	//#(# \subsection{Data Structure}
	private Node<E> dummy;
	private int count;
	private Node<E> cursor;
	//#)
	// TODO: Declare the private fields (See homework assignment)

	private LinkedSequence(boolean ignored) {} // DO NOT CHANGE THIS

	// #(# \subsection{wellFormed}
	private boolean wellFormed() {
		// Invariant:
		// 1. dummy node is not null.  Its data should be itself, cast (unsafely) data = (T)this;.
		// 2. each link must be correctly double linked.
		// 3. size is number of nodes in list, other than the dummy.
		// 4. the list must cycle back to the dummy node.
		// 5. none of the nodes inside the list has a data pointing to itself
		if (dummy == null) return report("dummy node is null");
		if (dummy.data != dummy) return report("dummy's data is wrong");

		int count_elements = 0;
		boolean foundCursor = cursor == dummy;
		Node<E> prev = dummy;
		for (Node<E> p = dummy.next; p != dummy; p = p.next) {
			if (p == cursor) foundCursor = true;
			if (p == null) return report("found null in list after " + count_elements + " nodes");
			if (p.data == p) return report("Found dummy node inside of the list");
			if (p.prev != prev) return report("prev link bad after " + count_elements + " nodes");
			++count_elements;
			prev = p;
		}
		if (dummy.prev != prev) return report("dummy's prev link is wrong");
		if (count != count_elements) return report("size is " + count + " when it should be " + count_elements);
		
		if (!foundCursor) return report("cursor is not a node in linked list");
		
		// If no problems found, then return true:
		return true;
	}
	// #)
	// TODO: Declare "wellFormed" and check the data structure
	// You should be able to figure out what checks to make.
	// We have internal tests to help you have confidence that you
	// are checking the right things.

	/**
	 * Create an empty sequence
	 */
	public LinkedSequence() {
		// #(# \subsection{No-Argument Constructor}
		dummy = new Node<E>();
		count = 0;
		cursor = dummy;
		// #)
		//TODO: implement this
		assert wellFormed() : "invariant failed in constructor";
	}

	// #(# \subsection{Body of class}
	/**
	 * Determine the number of elements in this sequence.
	 * @return
	 *   the number of elements in this sequence
	 **/ 
	public int size() {
		assert wellFormed() : "invariant broken at start of size()";
		return count;
	}
	
	@Override // implementation (optional)
	public String toString() {
		assert wellFormed() : "invariant broken at start of toString";
		return "{Collection of size: " + Integer.toString(size()) + "}";
	}

	/**
	 * The first element (if any) of this sequence is now current.
	 * @postcondition
	 *   The front element of this sequence (if any) is now the current element (but 
	 *   if this sequence has no elements at all, then there is no current 
	 *   element).
	 **/ 
	public void start( )
	{
		assert wellFormed() : "invariant failed at start of start";
		cursor = dummy;
		assert wellFormed() : "invariant failed at end of start";
	}

	/**
	 * Accessor method to determine whether this sequence has a specified 
	 * current element (a HexTile or null) that can be retrieved with the 
	 * getCurrent method. This depends on the status of the cursor.
	 * @return
	 *   true (there is a current element) or false (there is no current element at the moment)
	 **/
	public boolean isCurrent( )
	{
		assert wellFormed() : "invariant failed at start of isCurrent";
		return cursor != dummy;
	}
	
	/**
	 * Accessor method to get the current element of this sequence. 
	 * @precondition
	 *   isCurrent() returns true.
	 * @return
	 *   the current element of this sequence, possibly null
	 * @exception IllegalStateException
	 *   Indicates that there is no current element, so 
	 *   getCurrent may not be called.
	 **/
	public E getCurrent( )
	{
		assert wellFormed() : "invariant failed at start of getCurrent";
		if (!isCurrent()) throw new IllegalStateException("no current");
		return cursor.data;
	}

	/**
	 * Move forward, so that the next element is now the current element in
	 * this sequence.
	 * @precondition
	 *   isCurrent() returns true. 
	 * @postcondition
	 *   If the current element was already the end element of this sequence 
	 *   (with nothing after it), then there is no longer any current element. 
	 *   Otherwise, the new current element is the element immediately after the 
	 *   original current element.
	 * @exception IllegalStateException
	 *   If there was no current element, so 
	 *   advance may not be called (the precondition was false).
	 **/
	public void advance( )
	{
		assert wellFormed() : "invariant failed at start of advance";
		if (!isCurrent()) throw new IllegalStateException("advancing past end");
		cursor = cursor.next;
		assert wellFormed() : "invariant failed at end of advance";
	}

	/**
	 * Remove the current element from this sequence.
	 * @precondition
	 *   isCurrent() returns true.
	 * @postcondition
	 *   The current element has been removed from this sequence, and the 
	 *   following element (if there is one) is now the new current element. 
	 *   If there was no following element, then there is now no current 
	 *   element.
	 * @exception IllegalStateException
	 *   Indicates that there is no current element, so 
	 *   removeCurrent may not be called. 
	 **/
	public void removeCurrent( )
	{
		assert wellFormed() : "invariant failed at start of removeCurrent";
		if (!isCurrent()) throw new IllegalStateException("no current to remove");
		cursor.prev.next = cursor.next;
		cursor.next.prev = cursor.prev;
		cursor = cursor.next;
		--count;
		assert wellFormed() : "invariant failed at end of removeCurrent";
	}

	/**
	 * Add a new element to this sequence, before the current element. 
	 * @param element
	 *   the new element that is being added, it is allowed to be null
	 * @postcondition
	 *   A new copy of the element has been added to this sequence. If there was
	 *   a current element, then the new element is placed before the current
	 *   element. If there was no current element, then the new element is placed
	 *   at the start of the sequence. In all cases, the new element becomes the
	 *   new current element of this sequence. 
	 **/
	public void addBefore(E x) {
		assert wellFormed() : "invariant broken at start of addAfter";
		if (cursor == dummy) cursor = cursor.next; 
		Node<E> n = new Node<E>(x,cursor.prev,cursor);
		cursor = cursor.prev = cursor.prev.next = n;
		++count;
		assert wellFormed() : "invariant broken at end of addAfter";
	}

	/**
	 * Add a new element to this sequence, after the current element. 
	 * @param element
	 *   the new element that is being added, may be null
	 * @postcondition
	 *   A new copy of the element has been added to this sequence. If there was
	 *   a current element, then the new element is placed after the current
	 *   element. If there was no current element, then the new element is placed
	 *   at the end of the sequence. In all cases, the new element becomes the
	 *   new current element of this sequence. 
	 **/
	public void addAfter(E x) {
		assert wellFormed() : "invariant broken at start of addAfter";
		if (cursor == dummy) cursor = cursor.prev; 
		Node<E> n = new Node<E>(x,cursor,cursor.next);
		cursor = cursor.next = cursor.next.prev = n;
		++count;
		assert wellFormed() : "invariant broken at end of addAfter";
	}

	/**
	 * Place the contents of another sequence at the end of this sequence.
	 * @param addend
	 *   a sequence whose contents will be placed at the end of this sequence
	 * @precondition
	 *   The parameter, addend, is not null. 
	 * @postcondition
	 *   The elements from addend have been placed at the end of 
	 *   this sequence. The current element of this sequence if any,
	 *   remains unchanged.   The addend is unchanged.
	 * @exception NullPointerException
	 *   Indicates that addend is null. 
	 **/
	public void addAll(LinkedSequence<E> c) {
		assert wellFormed() : "invariant broken at start of addAll";
		if (c.size() > 0) {
			c = c.clone();
			dummy.prev.next = c.dummy.next;
			c.dummy.next.prev = dummy.prev;
			c.dummy.prev.next = dummy;
			dummy.prev = c.dummy.prev;
			count += c.count;
			c.dummy.prev = c.dummy.next = c.dummy;
		}
		assert wellFormed() : "invariant broken by addAll";
	}

	/**
	 * Generate a copy of this sequence.
	 * @param - none
	 * @return
	 *   The return value is a copy of this sequence. Subsequent changes to the
	 *   copy will not affect the original, nor vice versa.
	 **/ 
	@SuppressWarnings("unchecked")
	public LinkedSequence<E> clone( )
	{  // Clone a HexTileSeq object.
		assert wellFormed() : "invariant failed at start of clone";
		LinkedSequence<E> result;

		try
		{
			result = (LinkedSequence<E>) super.clone( );
		}
		catch (CloneNotSupportedException e)
		{  // This exception should not occur. But if it does, it would probably
			// indicate a programming error that made super.clone unavailable.
			// The most common error would be forgetting the "Implements Cloneable"
			// clause at the start of this class.
			throw new RuntimeException
			("This class does not implement Cloneable");
		}

		Node<E> newDummy = new Node<>();
		Node<E> newCursor = newDummy;
		Node<E> prev = newDummy;
		for (Node<E> n = dummy.next; n != dummy; n = n.next) {
			Node<E> copy = new Node<>(n.data, prev, null);
			prev.next = copy;
			if (cursor == n) newCursor = copy;
			prev = copy;
		}
		newDummy.prev = prev;
		
		result.cursor = newCursor;
		result.dummy = newDummy;

		assert wellFormed() : "invariant failed at end of clone";
		assert result.wellFormed() : "invariant failed for clone";

		return result;
	}
	// #)

	// do not change this class -- it's used for internal testing:
	public static class Spy {
		/**
		 * Return the sink for invariant error messages
		 * @return current reporter
		 */
		public Consumer<String> getReporter() {
			return reporter;
		}

		/**
		 * Change the sink for invariant error messages.
		 * @param r where to send invariant error messages.
		 */
		public void setReporter(Consumer<String> r) {
			reporter = r;
		}
		
		/**
		 * A debugging node class for use in testing only, not in client code.
		 * @param E data type
		 */
		public static class Node<E> extends LinkedSequence.Node<E> {
			public Node(E d) {
				super();
				this.data = d;
				this.prev = null;
				this.next = null;
			}
		}
		
		/**
		 * Make a debugging node with the given data element
		 * @param d data from the node
		 * @return new debugging node
		 */
		public <E> Node<E> makeNode(E d) {
			return new Node<>(d);
		}
		
		/**
		 * Return a debugging node whose data refers to itself
		 * @return new debugging node
		 */
		@SuppressWarnings("unchecked")
		public <E> Node<E> makeSelfRef() {
			Node<E> result = new Node<>(null);
			result.data = (E)result;
			return result;
		}
		
		/**
		 * Change the data field of a debugging node
		 * @param n node to change
		 * @param x new value of the data
		 */
		@SuppressWarnings("unchecked")
		public <E> void assignData(Node<E> n, Object x) {
			n.data = (E)x;
		}
		
		/**
		 * Link the nodes in the forward direction.
		 * @param first node to point forward to the first of the rest
		 * @param rest remaining nodes to be linked
		 */
		public <E> void linkForward(Node<E> first, @SuppressWarnings("unchecked") Node<E>... rest) {
			Node<E> last = first;
			for (Node<E> n : rest) {
				last.next = n;
				last = n;
			}
		}
		
		/**
		 * Link the nodes in the reverse direction.
		 * @param first node to point the first of the rest nodes back to
		 * @param rest remaining node to link
		 */
		public <E> void linkBackward(Node<E> first, @SuppressWarnings("unchecked") Node<E>... rest) {
			Node<E> last = first;
			for (Node<E> n : rest) {
				n.prev = last;
				last = n;
			}
		}
		
		/**
		 * Create an instance of LinkedCollection so that we can test the invariant checker.
		 * @param d dummy node
		 * @param s count
		 * @param c cursor
		 * @return instance of LinkedCollection that has not been checked.
		 */
		public <E> LinkedSequence<E> newInstance(Node<E> d, int s, Node<E> c) {
			LinkedSequence<E> result = new LinkedSequence<>(false);
			result.dummy = d;
			result.count = s;
			result.cursor = c;
			return result;
		}
		
		/**
		 * Check a linked collection's data structure
		 * @param lc linked collection to check, must not be null
		 * @return whether the data structure is deemed OK
		 */
		public <E> boolean wellFormed(LinkedSequence<E> lc) {
			return lc.wellFormed();
		}
	}
}

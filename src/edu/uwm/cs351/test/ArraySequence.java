// This is an assignment for students to complete after reading Chapter 3 of
// "Data Structures and Other Objects Using Java" by Michael Main.

package edu.uwm.cs351.test;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ListIterator;

import edu.uwm.cs351.test.util.Option;

public class ArraySequence<E> implements ISequence<E> {

	List<E> contents = new ArrayList<E>();
	Option<E> current;
	ListIterator<E> cursor; // elements after current (if any)
	
	protected void resetCursor() {
		int index = cursor.nextIndex();
		if (current == null) {
			cursor = contents.listIterator(contents.size());
		} else {
			cursor = contents.listIterator(index-1);
			cursor.next();
		}
	}
	
	public ArraySequence() {
		cursor = contents.listIterator();
	}
	
	@Override
	public ISequence<E> newInstance() {
		return new ArraySequence<>();
	}

	@Override
	public void addBefore(E element) {
		if (current == null) {
			contents.add(0, element);
			cursor = contents.listIterator();
			cursor.next();
		} else {
			if (cursor.previous() != current.getValue()) {
				System.out.println("Internal error in Reference Implementation!");
			} else {
				cursor.add(element);
				//cursor.next();
			}
		}
		current = new Option<>(element);
		resetCursor();
	}

	@Override
	public void addAfter(E element) {
		try {
			cursor.add(element);
			current = new Option<>(element);
		} catch (ConcurrentModificationException e) {
			System.err.println("Internal Error in reference implementation");
			e.printStackTrace();
		}
		resetCursor();
	}

	@Override
	public void advance() {
		try {
			if (!isCurrent()) throw new IllegalStateException("no more");
			if (cursor.hasNext()) current = new Option<>(cursor.next());
			else current = null;
		} catch (ConcurrentModificationException e) {
			System.err.println("Internal Error in reference implementation");
			e.printStackTrace();
		}
	}

	public ArraySequence<E> clone() {
		ArraySequence<E> result = new ArraySequence<>();
		result.contents = new ArrayList<>(contents);
		result.current = current;
		int index = cursor.nextIndex();
		result.cursor = result.contents.listIterator(index);
		result.resetCursor();
		return result;
	}

	@Override
	public void addAll(ISequence<? extends E> addend) {
		int index = cursor.nextIndex();
		contents.addAll(((ArraySequence<? extends E>)addend).contents);
		cursor = contents.listIterator(index);
		resetCursor();
	}
	
	@Override
	public E getCurrent() {
		if (current == null) throw new IllegalStateException("no current");
		return current.getValue();
	}

	@Override
	public boolean isCurrent() {
		return current != null;
	}

	@Override
	public void removeCurrent() {
		if (!isCurrent()) throw new IllegalStateException("no current");
		try {
			cursor.remove();
			if (cursor.hasNext()) {
				current = new Option<>(cursor.next());
			} else {
				current = null;
			}
		} catch (Exception e) {
			System.out.println("Internal error in reference implementation:");
			e.printStackTrace();
		}
	}

	@Override
	public int size() {
		return contents.size();
	}

	@Override
	public void start() {
		try {
			cursor = contents.listIterator();
			current = null;
			if (cursor.hasNext()) current = new Option<>(cursor.next());
		} catch (Exception e) {
			System.out.println("Internal error in Reference Implementation:");
			e.printStackTrace();
		}
	}
	
	/*
	@Override
	public String toString() {
		return contents.toString() + "@" + cursor.nextIndex() + "->" + current;
	}*/
	
	public static void main(String[] args) {
		ArraySequence<Integer> s = new ArraySequence<>();
		s.start();
		s.addAfter(null);
	}
}